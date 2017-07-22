package com.appspot.reservandeat_171704.reservandeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class R_registrada extends AppCompatActivity implements View.OnClickListener {
    Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_registrada);
        btnMenu = (Button) findViewById(R.id.btnMenu);
        TextView tvtermino = (TextView) findViewById(R.id.tvInfoRes);

        tvtermino.setText("Con estos datos podras hacer valida tu reservacion  " +
                "en el restaurante que elegiste!!");

        btnMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==btnMenu){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
