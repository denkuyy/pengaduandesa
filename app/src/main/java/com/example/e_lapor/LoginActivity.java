package com.example.e_lapor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button masuk;
    EditText user, pass;
    String nik_, nama_, alamat_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        masuk=findViewById(R.id.bt_masuk);
        user=findViewById(R.id.ed_username);
        pass=findViewById(R.id.ed_password);
        bacaPreferensi();
        masuk.setOnClickListener(this);
        if (nik_.equals("0")) {

        } else {
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            finish();
        }

    }
    private void bacaPreferensi() {
        SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
        nik_ = pref.getString(koneksi.nik, "0");
        nama_ = pref.getString(koneksi.nama, "0");
        alamat_ = pref.getString(koneksi.alamat, "0");
    }

    @Override
    public void onClick(View v) {
        String token = FirebaseInstanceId.getInstance().getToken();
        //Finally we need to implement a method to store this unique id to our server

        String ur=user.getText().toString();
        String pw=pass.getText().toString();
        if (ur.equals("")) {
            user.setError("Belum diisi");
            user.requestFocus();
        } else if (pw.equals("")) {
            pass.setError("Belum diisi");
            pass.requestFocus();
        } else {
            Dialog dialog=new Dialog(this);
            dialog.setContentView(R.layout.loading);
            if (dialog.getWindow()!=null){
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            dialog.show();
            dialog.setCancelable(false);
            StringRequest simpan = new StringRequest(Request.Method.POST, koneksi.login,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) { try {
                            if (response.contains("1")) {
                            JSONObject jsonObject =new JSONObject(response);
                            JSONArray hasil=jsonObject.getJSONArray("login");
                                for (int i = 0; i < hasil.length(); i++) {
                                    JSONObject c = hasil.getJSONObject(i);
                                    String nm = c.getString(koneksi.nama);
                                    String id = c.getString(koneksi.nik);
                                    String alm = c.getString(koneksi.alamat);
                                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                    SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString(koneksi.nik, id.toString());
                                    editor.putString(koneksi.nama, nm.toString());
                                    editor.putString(koneksi.alamat, alm.toString());
                                    editor.commit();
                                    finish();
                                }
                            }else   if (response.contains("0")) {
                                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("NIK atau Password anda salah!")
                                        .show();
                                dialog.dismiss();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Jaringan Tidak Ada")
                            .show();
                    dialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put(koneksi.nik, ur);
                    param.put(koneksi.password, pw);
                    param.put("token", token);
                    return param;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(simpan);
        }
    }


}