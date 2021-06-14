package com.example.gap_mvil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Collator;

public class SegundaVentana extends AppCompatActivity {

    TextView L1,L2,L3,L4,M1,M2,M3,M4,X1,X2,X3,X4,J1,J2,J3,J4,V1,V2,V3,V4,S1,S2,S3,S4,D1,D2,D3,D4;
    Button bt_perfil, bt_empresa;
    String DNI, nombre_horario="";
    //public static String constante = "http://192.168.1.21/gap/api/";
    public static String constante = "https://gap.datagram.es/api/";
    JSONObject tramo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_ventana);



        L1 = (TextView) findViewById(R.id.L1);
        L2 = (TextView) findViewById(R.id.L2);
        L3 = (TextView) findViewById(R.id.L3);
        L4 = (TextView) findViewById(R.id.L4);
        M1 = (TextView) findViewById(R.id.M1);
        M2 = (TextView) findViewById(R.id.M2);
        M3 = (TextView) findViewById(R.id.M3);
        M4 = (TextView) findViewById(R.id.M4);
        X1 = (TextView) findViewById(R.id.X1);
        X2 = (TextView) findViewById(R.id.X2);
        X3 = (TextView) findViewById(R.id.X3);
        X4 = (TextView) findViewById(R.id.X4);
        J1 = (TextView) findViewById(R.id.J1);
        J2 = (TextView) findViewById(R.id.J2);
        J3 = (TextView) findViewById(R.id.J3);
        J4 = (TextView) findViewById(R.id.J4);
        V1 = (TextView) findViewById(R.id.V1);
        V2 = (TextView) findViewById(R.id.V2);
        V3 = (TextView) findViewById(R.id.V3);
        V4 = (TextView) findViewById(R.id.V4);
        S1 = (TextView) findViewById(R.id.S1);
        S2 = (TextView) findViewById(R.id.S2);
        S3 = (TextView) findViewById(R.id.S3);
        S4 = (TextView) findViewById(R.id.S4);
        D1 = (TextView) findViewById(R.id.D1);
        D2 = (TextView) findViewById(R.id.D2);
        D3 = (TextView) findViewById(R.id.D3);
        D4 = (TextView) findViewById(R.id.D4);

        bt_empresa = (Button) findViewById(R.id.bt_empresa);
        bt_perfil = (Button) findViewById(R.id.bt_perfil);

        DNI = getIntent().getStringExtra("dni");


        try {
            obtenerNombreHorario(constante, DNI);
            Thread.sleep(2*1000);
        } catch (Exception e) {
            System.out.println(e);
        }

        bt_empresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SegundaVentana.this, EmpresaActivity.class);
                i.putExtra("DNI", DNI);
                startActivity(i);

            }
        });

        bt_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SegundaVentana.this, TrabajadorActivity.class);
                i.putExtra("DNI", DNI);
                startActivity(i);

            }
        });
    }

    public void mensaje(String m){Toast.makeText(getBaseContext(),m,Toast.LENGTH_SHORT).show(); }

    public void asociarInformación(TextView T1, TextView T2, TextView T3, TextView T4, String HE1, String HS1, String HE2, String HS2) throws JSONException {
        T1.setText(HE1);
        T2.setText(HS1);
        T3.setText(HE2);
        T4.setText(HS2);
    }

    public void cargarTramo(String URL, String dia){

        String url = URL+"Tramos/consultarTramoHorario?dia="+dia+"&horario="+nombre_horario;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    String resp = response.toString();

                    try {
                        tramo = new JSONObject(resp);
                        String HE1 = tramo.getString("HE1"), HE2 = tramo.getString("HE2"), HS1 = tramo.getString("HS1"),HS2 = tramo.getString("HS2");
                        switch (dia){
                            case "Lunes": asociarInformación(L1, L2, L3, L4,HE1,HS1, HE2, HS2) ; break;
                            case "Martes": asociarInformación(M1, M2, M3, M4,HE1,HS1, HE2, HS2) ; break;
                            case "Miércoles": asociarInformación(X1, X2, X3, X4,HE1,HS1, HE2, HS2) ; break;
                            case "Jueves": asociarInformación(J1, J2, J3, J4,HE1,HS1, HE2, HS2) ; break;
                            case "Viernes": asociarInformación(V1, V2, V3, V4,HE1,HS1, HE2, HS2) ; break;
                            case "Sábado": asociarInformación(S1, S2, S3, S4,HE1,HS1, HE2, HS2) ; break;
                            case "Domingo": asociarInformación(D1, D2, D3, D4,HE1,HS1, HE2, HS2) ; break;
                        }
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

    public void obtenerNombreHorario( String URL, String DNI){
        String url = URL+"Horario/consultarHorario?DNI="+DNI;
       // String url = "https://gap.datagram.es/api/Horario/consultarHorario?DNI=49113708Z";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String resp = response.toString();
                        String[] parts = resp.split(" ");
                        nombre_horario = parts[0] + "%20" + parts[1];
                        carga();
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

    public void carga(){
        try {
            cargarTramo(constante,"Lunes");
            cargarTramo(constante,"Martes");
            cargarTramo(constante,"Miércoles");
            cargarTramo(constante,"Jueves");
            cargarTramo(constante,"Viernes");
            cargarTramo(constante,"Sábado");
            cargarTramo(constante,"Domingo");
        } catch (Exception e) {
            e.printStackTrace();
            mensaje("Error");
        }
    }
}