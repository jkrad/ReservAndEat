package com.appspot.reservandeat_171704.backend.mensajeria;

import com.appspot.reservandeat_171704.backend.entidades.Dispositivo;
import com.appspot.reservandeat_171704.backend.entidades.Subscripcion;
import com.fasterxml.jackson.jr.ob.JSON;

import net.ramptors.datastore.Consulta;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Base64;
import org.jose4j.lang.JoseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java8.util.Optional;
import java8.util.function.Consumer;
import java8.util.function.Function;
import java8.util.stream.Collectors;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;

import static net.ramptors.base.UtilBase.isPresent;
import static net.ramptors.base.UtilIO.leeString;

/**
 * @author Gilberto Pacheco Gallegos
 */
public class Mensajeria {

    /**
     * Time To Live de notificaciones GCM
     */
    private static final int TTL = 255;
    private static final String GCM_API_KEY = "AAAA_rRDxr8:APA91bGhWwDN-j2DSy2P4t"
            + "Ga5Aeyrsk7nwYRTmDGS5xVL9O4RM9fJ1secfHsPtVcIgkPVKl7u4xsN"
            + "auVzL7aUp067d2JC9cRdynchfdN-v99AuWb8BYKrboDqaClMZmxyPGTt"
            + "RoIFLxxetEX8jEm0AQ3wcfPFrfutQ";

    public static void notifica(final Map<String, String> data) {
        // Agrega BouncyCastle como un proveedor de algoritmos.
        if (!isPresent(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME))) {
            Security.addProvider(new BouncyCastleProvider());
        }
        notificaDispositivos(data);
        notificaSubscripciones(data);
    }

    private static void notificaSubscripciones(final Map<String, String> data) {
        new Consulta<Subscripcion>() {
        }.lista()
                .forEach(new Consumer<Subscripcion>() {
                    @Override
                    public void accept(Subscripcion s) {
                        try {
                            final PushService pushService = new PushService();
                            final Notification notification;
                            if (s.getKey().
                                    startsWith("https://android.googleapis.com/gcm/send")) {
                                notification = new Notification(s.getKey(),
                                        getUserPublicKey(s), Base64.decode(s.getAuth()),
                                        JSON.std.asBytes(data), TTL);
                                pushService.setGcmApiKey(GCM_API_KEY);
                            } else {
                                notification = new Notification(s.getKey(),
                                        getUserPublicKey(s), Base64.decode(s.getAuth()),
                                        JSON.std.asBytes(data));
                            }
                            pushService.send(notification);
                            Logger.getLogger(Mensajeria.class.getName())
                                    .log(Level.INFO, "Mensaje enviado a {0}", s.getKey());
                        } catch (NoSuchAlgorithmException | InvalidKeySpecException
                                | NoSuchProviderException | IOException | NoSuchPaddingException
                                | InvalidKeyException | BadPaddingException
                                | IllegalBlockSizeException | InvalidAlgorithmParameterException
                                | JoseException | RuntimeException e) {
                            Logger.getLogger(Mensajeria.class.getName())
                                    .log(Level.SEVERE, "Error enviando mensaje web.", e);
                        }
                    }
                });
    }

    private static void notificaDispositivos(Map<String, String> data) {
        final List<String> registration_ids
                = new Consulta<Dispositivo>() {
                }.lista()
                        .map(new Function<Dispositivo, String>() {
                            @Override
                            public String apply(Dispositivo d) {
                                return d.getKey();
                            }
                        }).collect(Collectors.<String>toList());
        Optional<HttpURLConnection> con = Optional.empty();
        try {
            if (registration_ids.size() > 0) {
                final HttpURLConnection c = (HttpURLConnection) new URL(
                        "https://fcm.googleapis.com/fcm/send").openConnection();
                con = Optional.of(c);
                c.setUseCaches(false);
                c.setDoOutput(true);
                c.setRequestMethod("POST");
                c.setRequestProperty("Content-Type", "application/json");
                c.setRequestProperty("Authorization", "key=" + GCM_API_KEY);
                final Mensaje mensaje = new Mensaje(registration_ids, data);
                JSON.std.write(mensaje, c.getOutputStream());
                leeString(c.getInputStream()).ifPresent(new Consumer<String>() {
                    @Override
                    public void accept(String respuesta) {
                        Logger.getLogger(Mensajeria.class.getName())
                                .log(Level.INFO, respuesta);
                    }
                });
            }
        } catch (IOException | RuntimeException e) {
            Logger.getLogger(Mensajeria.class.getName())
                    .log(Level.SEVERE, "Error enviando mensaje a móvil.", e);
        } finally {
            con.ifPresent(new Consumer<HttpURLConnection>() {
                @Override
                public void accept(HttpURLConnection c) {
                    c.disconnect();
                }
            });
        }
    }

    private static PublicKey getUserPublicKey(Subscripcion s)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchProviderException {
        final KeyFactory kf
                = KeyFactory.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME);
        final ECNamedCurveParameterSpec ecSpec
                = ECNamedCurveTable.getParameterSpec("secp256r1");
        final ECPoint point
                = ecSpec.getCurve().decodePoint(Base64.decode(s.getLlave()));
        final ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);
        return kf.generatePublic(pubSpec);
    }
}
