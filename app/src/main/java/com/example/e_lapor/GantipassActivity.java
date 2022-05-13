package com.example.e_lapor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GantipassActivity extends AppCompatActivity {
    TextView nik,nama;
    String nik_, nama_;
    EditText pbaru, plama, pkonf;
    Button gp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantipass);
        bacaPreferensi();
        nik=findViewById(R.id.p_nik);
        nama=findViewById(R.id.p_nama);
        plama=findViewById(R.id.p_lama);
        pbaru=findViewById(R.id.p_baru);
        pkonf=findViewById(R.id.p_konf);
        gp=findViewById(R.id.bt_gpass);
        nik.setText(nik_);
        nama.setText(nama_);

        gp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubahpass();
            }
        });
    }

    private void ubahpass() {
        String lama=plama.getText().toString();
        String baru=pbaru.getText().toString();
        String konfirm=pkonf.getText().toString();
        if (lama.equals("")) {
            plama.setError("Belum diisi");
            plama.requestFocus();
        } else if (baru.equals("")) {
            pbaru.setError("Belum diisi");
            pbaru.requestFocus();
        } else if (konfirm.equals("")) {
            pkonf.setError("Belum diisi");
            pkonf.requestFocus();
        }else if(baru.equals(konfirm)){
            StringRequest simpan = new StringRequest(Request.Method.POST, koneksi.gpass,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("pengaduan", "onResponse: " + response);
                            new SweetAlertDialog(GantipassActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Berhasil")
                                    .setContentText("Password Telah diganti!")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            Intent hc = new Intent(GantipassActivity.this, DashboardActivity.class);
                                            startActivity(hc);

                                        }
                                    })
                                    .show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Pengaduan", "onErrorResponse: " + error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put(koneksi.nik, nik_);
                    param.put(koneksi.plama, lama);
                    param.put(koneksi.pbaru, baru);
                    return param;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(simpan);
            }else {
            new SweetAlertDialog(GantipassActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Gagal")
                    .setContentText("Anda harus memasukkan password yang sama dua kali untuk mengkonfirmasikannya.")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    }).show();
            }
        }




    private void bacaPreferensi() {
        SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
        nik_ = pref.getString(koneksi.nik, "0");
        nama_ = pref.getString(koneksi.nama, "0");
    }
}