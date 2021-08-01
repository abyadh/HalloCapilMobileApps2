package com.example.hallocapilmobileapps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class form_login extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView selection;
    String[] Admin = res.getStringArray(R.array.admin)
    private Button login;
    private EditText username, password;
    private ListView listview_level, listView_visit_beranda;
    ArrayList<employee_visit_beranda> model;
    private TextView sign_up;
    private String usernames, passwords;
    private Urls urls;
    private ProgressBar progressbar;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_login);
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Admin);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position,   long id) {
        Toast.makeText(this, "Anda Memilih: " + Admin[position],Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Silahkan Pilih Admin", Toast.LENGTH_LONG).show();
    }



    /** Called when the user touches the button */
    public void Login(View view) {
        // Do something in response to button click
    }

    public void initView(){
        login = findViewById(R.id.button_login);
        sign_up = findViewById(R.id.sign);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        progressbar = findViewById(R.id.etprogress);
        listview_level = findViewById(R.id.listview_level);
        listView_visit_beranda = findViewById(R.id.listview_visit_beranda);

    }

    login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            login.setVisibility(View.GONE);
            progressbar.setVisibility(View.VISIBLE);
            usernames = username.getText().toString().trim();
            passwords = password.getText().toString().trim();
            if(!usernames.equals("") && !passwords.equals("")){
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_LOGIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            model = new ArrayList<>();
                            Log.e("RESPONSE", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String Status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if(Status.equals("1")){
                                String url = "https://baibaikalbar.000webhostapp.com/menampilkan_visit_beranda.php";
                                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            model = new ArrayList<>();
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray getHasil = jsonObject.getJSONArray("hasil");
                                            for (int i = 0; i<getHasil.length();i++){
                                                JSONObject getData = getHasil.getJSONObject(i);
                                                String visit_beranda = getData.getString("visit_beranda");
                                                model.add(new employee_visit_beranda(visit_beranda));
                                                MainActivity.Adapter adapter =  new Adapter(getApplicationContext(), model);
                                                listView_visit_beranda.setAdapter(adapter);

                                                String tambah_visit = "1";
                                                int bil1,bil2, hasil;
                                                bil1 = (int) Integer.parseInt(visit_beranda);
                                                bil2 = (int) Integer.parseInt(tambah_visit);
                                                hasil = (int) bil1 + bil2;
                                                String hasils = String.valueOf(hasil);

                                                String url= "https://baibaikalbar.000webhostapp.com/mengupdate_visit_beranda.php";
                                                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try{
                                                            Log.e("RESPONSE", response);
                                                            JSONObject jsonObject = new JSONObject(response);
                                                            String Status = jsonObject.getString("status");
                                                            String msg = jsonObject.getString("message");
                                                            if(Status.equals("1")) {
                                                                login.setVisibility(View.VISIBLE);
                                                                progressbar.setVisibility(View.GONE);
                                                                sessionManager.setLogin(true);
                                                                sessionManager.setUsername(no_hps);
                                                                Intent i = new Intent(MainActivity.this, Jelajahi_baibaiActivity.class);
                                                                i.putExtra("no_hp", no_hps);
                                                                startActivity(i);
                                                                Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        }
                                                        catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }){
                                                    @Override
                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                        Map<String, String> data = new HashMap<>();
                                                        data.put("no_hp", no_hps);
                                                        data.put("visit_beranda", hasils);
                                                        return data;
                                                    }
                                                };
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                                requestQueue.add(stringRequest2);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> data = new HashMap<>();
                                        data.put("no_hp", no_hps);
                                        return data;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.add(stringRequest1);


                            }
                            else{
                                login.setVisibility(View.VISIBLE);
                                progressbar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        login.setVisibility(View.VISIBLE);
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        data.put("no_hp", no_hps);
                        data.put("password", passwords);
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);

            }else {
                login.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Field can not be Empty!", Toast.LENGTH_SHORT).show();
            }

        }
    });
        sign_up.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, sign_upActivity.class);
            startActivity(intent);
        }
    });

}

class Adapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    ArrayList<employee_visit_beranda> model;
    Adapter(Context c_context, ArrayList<employee_visit_beranda> c_model){
        this.context = c_context;
        this.model = c_model;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    TextView visit_beranda;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view1 = layoutInflater.inflate(R.layout.list_level,parent,false);
        visit_beranda = view1.findViewById(R.id.textview_visit_beranda);
        visit_beranda.setText(model.get(position).getVisit_beranda());

        return view1;
    }
}
}