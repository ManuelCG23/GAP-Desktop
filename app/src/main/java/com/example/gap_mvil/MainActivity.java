package com.example.gap_mvil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText usuario, contraseña;
    Button login;
    //public static String constante = "http://192.168.1.21/gap/api/";
    public static String constante = "https://gap.datagram.es/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = (EditText) findViewById(R.id.usuario);
        contraseña = (EditText) findViewById(R.id.contraseña);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    login(constante, usuario.getText().toString(), contraseña.getText().toString());
                }catch (Exception e){
                    mensaje("Error");
                }

            }
        });
    }

    public void login( String URL, String usu, String contra){
        String url = URL+"Login/ConsultarAcceso?usuario="+usu+"&password="+contra;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean respuesta = Boolean.parseBoolean(response.toString());

                        if(respuesta){
                             Intent i = new Intent(MainActivity.this, SegundaVentana.class);
                             i.putExtra("dni", usuario.getText().toString());
                             startActivity(i);
                        }else mensaje("Usuario o contraseña incorrecta");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensaje(error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void mensaje(String m){
        Toast.makeText(getBaseContext(),m,Toast.LENGTH_SHORT).show();
    }


}