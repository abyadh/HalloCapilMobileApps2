package com.example.hallocapilmobileapps;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class form_admindesa extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView selection;
    Resources res = getResources();
    String[] pelayanan = res.getStringArray(R.array.pelayanan_akta);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_desa);
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, pelayanan);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Toast.makeText(this, "Anda Memilih: " + pelayanan[position],Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Silahkan Pilih Admin", Toast.LENGTH_LONG).show();
    }


}