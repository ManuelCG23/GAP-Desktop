package com.example.gap_mvil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

public class EmpresaActivity extends AppCompatActivity {

    String DNI, nombre_empresa="",dir;
    TextView nombre, direccion, fecha, sector, pais, juridica, correo, pagina;
    public static String constante = "https://gap.datagram.es/api/";
    JSONObject tramo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        nombre = (TextView) findViewById(R.id.titulo);
        direccion = (TextView) findViewById(R.id.lbl_dni);
        fecha = (TextView) findViewById(R.id.lbl_nombre);
        correo = (TextView) findViewById(R.id.lbl_direccion);
        pais = (TextView) findViewById(R.id.lbl_fecha_nacimiento);
        sector = (TextView) findViewById(R.id.lbl_sexo);
        juridica = (TextView) findViewById(R.id.lbl_apellidos);
        pagina = (TextView) findViewById(R.id.lbl_correo);

        DNI = getIntent().getStringExtra("DNI");

        //llamar a la consulta para rellenar los parámetros
        try {
            obtenerNombreEmpresa(constante, DNI);
            //Thread.sleep(2*1000);
        } catch (Exception e) {
            System.out.println(e);
        }

        pagina.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(dir.length()>0){
                    Uri uri = Uri.parse(dir);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

    }

    public void obtenerNombreEmpresa( String URL, String DNI){
        String url = URL+"NEmpresa/ConsultarNombreEmpresa?DNI="+DNI;
        // String url = "https://gap.datagram.es/api/Horario/consultarHorario?DNI=49113708Z";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String resp = response.toString();
                        nombre_empresa = resp;
                       // mensaje(nombre_empresa);
                        cargarInforEmpresa(URL);
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

    public void cargarInforEmpresa(String URL){

        String url = URL+"Empresas/GetEmpresa?empresa="+nombre_empresa;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String resp = response.toString();

                        try {
                            tramo = new JSONObject(resp);
                             nombre.setText(nombre_empresa);
                             direccion.setText(tramo.getString("direccion"));
                             fecha.setText(tramo.getString("fecha_fundacion"));
                             juridica.setText(tramo.getString("estructuraJuridica"));
                             sector.setText(tramo.getString("sector"));
                             pais.setText(tramo.getString("pais"));
                             correo.setText(tramo.getString("correo"));
                             pagina.setText(tramo.getString("pagina"));
                            dir=  pagina.getText().toString();

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

    public void mensaje(String m){ Toast.makeText(getBaseContext(),m,Toast.LENGTH_SHORT).show(); }
}