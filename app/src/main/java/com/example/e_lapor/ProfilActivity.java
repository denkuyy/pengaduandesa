package com.example.e_lapor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfilActivity extends AppCompatActivity {
CardView gpass;
TextView tnik, tnama, ttgl, tjk,talamat, tagama, tpekerjaan;
String nik_, nama_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        bacaPreferensi();
        gpass=findViewById(R.id.cv_gantipass);
        tnik=findViewById(R.id.tp_nik);
        tnama=findViewById(R.id.tp_nama);
        ttgl=findViewById(R.id.tp_ttl);
        tjk=findViewById(R.id.tp_jk);
        talamat=findViewById(R.id.tp_alamat);
        tagama=findViewById(R.id.tp_agama);
        tpekerjaan=findViewById(R.id.tp_pekerjaan);

        tnik.setText(nik_);
        tnama.setText(nama_);

        tampil();
        gpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gp=new Intent(ProfilActivity.this, GantipassActivity.class);
                startActivity(gp);
            }
        });
    }

    public void tampil(){
        final StringRequest list = new StringRequest(Request.Method.POST, koneksi.profil,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("complaint", "onResponse: "+response );
                        try {
                            JSONObject jsonObject =new JSONObject(response);
                            JSONArray hasil=jsonObject.getJSONArray("Hasil");
                            for (int i = 0; i <hasil.length() ; i++) {
                                JSONObject c=hasil.getJSONObject(i);
                                String tgl=c.getString(koneksi.tanggal);
                                String tempat=c.getString(koneksi.tempat);
                                String jk=c.getString(koneksi.jenis_kelamin);
                                String agama=c.getString(koneksi.agama);
                                String pekerjaan=c.getString(koneksi.pekerjaan);
                                String alamat=c.getString(koneksi.alamat);
                                if (jk.equals("L")){
                                    tjk.setText("Laki-Laki");
                                }else if(jk.equals("P")){
                                    tjk.setText("Perempuan");
                                }
                                String ttl=tempat+", "+tgl;
                                ttgl.setText(ttl);
                                tagama.setText(agama);
                                tpekerjaan.setText(pekerjaan);
                                talamat.setText(alamat);
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("complain", "onErrorResponse: "+error.getMessage() );
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param=new HashMap<>();
                param.put("nik",nik_);
                return param;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(list);
    }
    private void bacaPreferensi() {
        SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
        nik_ = pref.getString(koneksi.nik, "0");
        nama_ = pref.getString(koneksi.nama, "0");
    }
}