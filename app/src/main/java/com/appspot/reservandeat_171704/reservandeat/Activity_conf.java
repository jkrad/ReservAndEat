package com.appspot.reservandeat_171704.reservandeat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

public class Activity_conf extends AppCompatActivity implements View.OnClickListener {
    Button btnfecha, btnhora, btnsig;
    EditText txtfecha, txthora;

    private int dia, mes, año, hora, mins;
    Calendar dateTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf);

        Spinner spinner = (Spinner) findViewById(R.id.ComboPer);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.personas, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        btnfecha = (Button) findViewById(R.id.btnFecha);
        btnhora = (Button) findViewById(R.id.btnHora);
        btnsig = (Button) findViewById(R.id.btnre);
        txtfecha = (EditText) findViewById(R.id.txtFecha);
        txthora = (EditText) findViewById(R.id.txtHora);



        btnfecha.setOnClickListener(this);
        btnhora.setOnClickListener(this);
        btnsig.setOnClickListener(this);


    }


    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String mes;

            if (month < 10) {
                mes = "0" + (month + 1);
            } else {
                mes = String.valueOf(month + 1);
            }

            txtfecha.setText(dayOfMonth + "/" + mes + "/" + year);
        }
    };

    @Override
    public void onClick(View v) {

        if (v == btnfecha) {
            new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();
        }
        if (v == btnhora) {
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            mins = c.get(Calendar.MINUTE);
            TimePickerDialog dlHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String horario, hora;
                    if (hourOfDay < 12) {
                        horario = "AM";
                    } else {
                        horario = "PM";
                    }
                    if (hourOfDay > 12) {
                        if (hourOfDay == 13) {
                            hora = "1";
                        } else if (hourOfDay == 14) {
                            hora = "2";
                        } else if (hourOfDay == 15) {
                            hora = "3";
                        } else if (hourOfDay == 16) {
                            hora = "4";
                        } else if (hourOfDay == 17) {
                            hora = "5";
                        } else if (hourOfDay == 18) {
                            hora = "6";
                        } else if (hourOfDay == 19) {
                            hora = "7";
                        } else if (hourOfDay == 20) {
                            hora = "8";
                        } else if (hourOfDay == 21) {
                            hora = "9";
                        } else if (hourOfDay == 22) {
                            hora = "10";
                        } else {
                            hora = "11";
                        }
                    } else {
                        hora = String.valueOf(hourOfDay);
                    }
                    txthora.setText(hora + ":" + minute + " " + horario);

                }
            }, hora, mins, false);
            dlHora.show();

        }
        if (v == btnsig) {
            Intent intent = new Intent(Activity_conf.this, R_registrada.class);
            startActivity(intent);
        }
    }
}
