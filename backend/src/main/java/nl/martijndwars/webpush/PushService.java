package nl.martijndwars.webpush;

import com.google.common.io.BaseEncoding;

import org.apache.commons.fileupload.util.Streams;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java8.util.Optional;
import java8.util.function.Consumer;

import static net.ramptors.base.UtilIO.leeString;

public class PushService {

    /**
     * The Google Cloud Messaging API getKey (for pre-VAPID in Chrome)
     */
    private String gcmApiKey;
    /**
     * Subject used in the JWT payload (for VAPID)
     */
    private String subject;
    /**
     * The public getKey (for VAPID)
     */
    private PublicKey publicKey;
    /**
     * The private getKey (for VAPID)
     */
    private Key privateKey;

    /**
     * Encrypt the payload using the user's public getKey using Elliptic Curve
     * Diffie Hellman cryptography over the prime256v1 curve.
     *
     * @return An Encrypted object containing the public getKey, salt, and
     * ciphertext, which can be sent to the other party.
     */
    private static Encrypted encrypt(byte[] buffer, PublicKey userPublicKey,
            byte[] userAuth, int padSize) throws
            NoSuchProviderException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException,
            InvalidKeySpecException, IOException {
        ECNamedCurveParameterSpec parameterSpec
                = ECNamedCurveTable.getParameterSpec("prime256v1");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDH", "BC");
        keyPairGenerator.initialize(parameterSpec);
        KeyPair serverKey = keyPairGenerator.generateKeyPair();
        Map<String, KeyPair> keys = new HashMap<>();
        keys.put("server-getKey-id", serverKey);
        Map<String, String> labels = new HashMap<>();
        labels.put("server-getKey-id", "P-256");
        byte[] salt = SecureRandom.getSeed(16);
        HttpEce httpEce = new HttpEce(keys, labels);
        byte[] ciphertext = httpEce
                .encrypt(buffer, salt, null, "server-getKey-id", userPublicKey,
                        userAuth, padSize);
        return new Encrypted.Builder()
                .withSalt(salt)
                .withPublicKey(serverKey.getPublic())
                .withCiphertext(ciphertext)
                .build();
    }

    /**
     * Send a notification
     */
    public void send(Notification notification)
            throws NoSuchPaddingException, InvalidKeyException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            NoSuchProviderException, InvalidAlgorithmParameterException, IOException,
            InvalidKeySpecException, JoseException {
        BaseEncoding base64url = BaseEncoding.base64Url();
        Encrypted encrypted = encrypt(
                notification.getPayload(),
                notification.getUserPublicKey(),
                notification.getUserAuth(),
                notification.getPadSize()
        );
        byte[] dh = Utils.savePublicKey((ECPublicKey) encrypted.getPublicKey());
        byte[] salt = encrypted.getSalt();
        Optional<HttpURLConnection> con = Optional.empty();
        try {
            final HttpURLConnection c
                    = (HttpURLConnection) new URL(notification.getEndpoint())
                            .openConnection();
            con = Optional.of(c);
            c.setUseCaches(false);
            c.setDoOutput(true);
            c.setRequestMethod("POST");
            c.setRequestProperty("TTL", String.valueOf(notification.getTTL()));
            boolean usaCryptoKey = false;
            final String cryptoKey = "keyid=p256dh;dh=" + base64url.encode(dh);
            if (notification.hasPayload()) {
                c.setRequestProperty("Content-Type", "application/octet-stream");
                c.setRequestProperty("Content-Encoding", "aesgcm");
                c.setRequestProperty("Encryption",
                        "keyid=p256dh;salt=" + base64url.omitPadding().encode(salt));
                c.setRequestProperty("Crypto-Key", cryptoKey);
                usaCryptoKey = true;
            }
            if (notification.isGcm()) {
                if (gcmApiKey == null) {
                    throw new IllegalStateException(
                            "An GCM API getKey is needed to send a push notification to a "
                            + "GCM endpoint.");
                }
                c.setRequestProperty("Authorization", "getKey=" + gcmApiKey);
            }
            if (vapidEnabled() && !notification.isGcm()) {
                JwtClaims claims = new JwtClaims();
                claims.setAudience(notification.getOrigin());
                claims.setExpirationTimeMinutesInTheFuture(12 * 60);
                claims.setSubject(subject);
                JsonWebSignature jws = new JsonWebSignature();
                jws.setHeader("typ", "JWT");
                jws.setHeader("alg", "ES256");
                jws.setPayload(claims.toJson());
                jws.setKey(privateKey);
                jws.setAlgorithmHeaderValue(
                        AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);
                c.setRequestProperty("Authorization",
                        "Bearer " + jws.getCompactSerialization());
                byte[] pk = Utils.savePublicKey((ECPublicKey) publicKey);
                if (usaCryptoKey) {
                    c.setRequestProperty("Crypto-Key",
                            cryptoKey + ";p256ecdsa=" + base64url.omitPadding().encode(pk));
                } else {
                    c.setRequestProperty("Crypto-Key",
                            "p256ecdsa=" + base64url.encode(pk));
                }
            }
            if (notification.hasPayload()) {
                Streams.copy(new ByteArrayInputStream(encrypted.getCiphertext()),
                        c.getOutputStream(), true);
            }
            leeString(c.getInputStream()).ifPresent(
                    new Consumer<String>() {
                @Override
                public void accept(String s) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, s);
                }
            });
        } finally {
            con.ifPresent(new Consumer<HttpURLConnection>() {
                @Override
                public void accept(HttpURLConnection c) {
                    c.disconnect();
                }
            });
        }
    }

    /**
     * Set the Google Cloud Messaging (GCM) API getKey
     */
    public PushService setGcmApiKey(String gcmApiKey) {
        this.gcmApiKey = gcmApiKey;
        return this;
    }

    /**
     * Set the JWT subject (for VAPID)
     */
    public PushService setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * Set the public getKey (for VAPID)
     */
    public PushService setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    /**
     * Set the private getKey (for VAPID)
     */
    public PushService setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    /**
     * Check if VAPID is enabled
     */
    private boolean vapidEnabled() {
        return publicKey != null && privateKey != null;
    }
}
