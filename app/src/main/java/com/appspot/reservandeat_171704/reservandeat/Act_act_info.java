package com.appspot.reservandeat_171704.reservandeat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class Act_act_info extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Reserv&Eat");
        setContentView(R.layout.activity_act_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivLogo = (ImageView) findViewById(R.id.ivLogo);
        TextView tvNombre = (TextView) findViewById(R.id.tvNOmbre);
        TextView tvTipo = (TextView) findViewById(R.id.tvTipoComidaBD);

        tvNombre.setText(getIntent().getStringExtra("nombre"));
        tvTipo.setText("Tipo de comida: " + getIntent().getStringExtra("tipo"));
        ivLogo.setImageResource(getIntent().getIntExtra("logo", R.drawable.ic_home_black_24dp));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if(v==fab){
            Intent intent = new Intent(Act_act_info.this,Activity_conf.class);
            startActivity(intent);

        }
    }
}
