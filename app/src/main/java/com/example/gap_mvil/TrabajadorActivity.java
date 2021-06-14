package com.example.gap_mvil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class TrabajadorActivity extends AppCompatActivity {

    String DNI;
    TextView DNI_T, nombre, apellidos, fecha, sexo, direccion, nacionalidad, correo;
    JSONObject tramos = null;
    public static String constante = "https://gap.datagram.es/api/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajador);

        DNI_T = (TextView) findViewById(R.id.lbl_dni);
        nombre = (TextView) findViewById(R.id.lbl_nombre);
        apellidos = (TextView) findViewById(R.id.lbl_apellidos);
        fecha = (TextView) findViewById(R.id.lbl_fecha_nacimiento);
        correo = (TextView) findViewById(R.id.lbl_correo);
        nacionalidad = (TextView) findViewById(R.id.lbl_nacionalidad);
        sexo = (TextView) findViewById(R.id.lbl_sexo);
        direccion = (TextView) findViewById(R.id.lbl_direccion);

        DNI = getIntent().getStringExtra("DNI");

        cargarInforEmpresa(constante);
    }


    public void cargarInforEmpresa(String URL){

        String url = URL+"Trabajador/GetTrabajador?DNI="+DNI;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String resp = response.toString();

                        try {
                            tramos = new JSONObject(resp);
                            DNI_T.setText(DNI);
                            nombre.setText(tramos.getString("nombre"));
                            apellidos.setText(tramos.getString("apellidos"));
                            fecha.setText(tramos.getString("fecha_nac"));
                            correo.setText(tramos.getString("correo"));
                            nacionalidad.setText(tramos.getString("nacionalidad"));
                            sexo.setText(tramos.getString("sexo"));
                            direccion.setText(tramos.getString("direccion"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int i = 0;
                    }
                }
                );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}