package com.example.e_lapor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.nj.imagepicker.ImagePicker;
import com.nj.imagepicker.listener.ImageResultListener;
import com.nj.imagepicker.result.ImageResult;
import com.nj.imagepicker.utils.DialogConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pl.aprilapps.easyphotopicker.EasyImage;

public class PengaduanActivity extends AppCompatActivity {

    Button save;
    ImageView gambar, kamera;
    public static final int REQUEST_CODE_CAMERA = 001;
    public static final int REQUEST_CODE_GALLERY = 002;
    String nik_, nama_, date, alamat_;
    Bitmap bitmap;
    EditText nik, nama, keterangan;
    Spinner bidang;
    List<String> datakategori = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaduan);
        bacaPreferensi();
        kamera=findViewById(R.id.bt_cam);
        save=findViewById(R.id.bt_kirim);
        gambar=findViewById(R.id.img_foto);
        nik=findViewById(R.id.pd_nik);
        nama=findViewById(R.id.pd_nama);
        keterangan=findViewById(R.id.pd_ket);
        bidang=findViewById(R.id.spinner);
        Intent intent=getIntent();
        date=intent.getStringExtra(koneksi.tgl);

        nik.setText(nik_);

        nama.setText(nama_);

        
        kamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambilgambar();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirim();
            }
        });
        view("kategori");
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {



                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(PengaduanActivity.this, "You should accept permission", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

    }

    private void kirim() {
        if (bidang.getSelectedItem().toString().equals("Pilih Bidang")
                || keterangan.getText().toString().isEmpty()) {
                 new SweetAlertDialog(PengaduanActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Data Tidak Boleh Kosong")
                    .show();
        }else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Mengirim data. . .");
            progressDialog.show();
            String bid=bidang.getSelectedItem().toString();
            StringRequest simpan = new StringRequest(Request.Method.POST, koneksi.pengaduan,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            Log.e("pengaduan", "onResponse: " + response);
                            new SweetAlertDialog(PengaduanActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Data Terkirim")
                                    .setContentText("Pengaduan Anda Telah Terkirim!")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            Intent hc=new Intent(PengaduanActivity.this, DashboardActivity.class);
                                            startActivity(hc);
                                            finish();

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
                    param.put(koneksi.nama, nama_);
                    param.put(koneksi.tgl,date);
                    param.put(koneksi.alamat, alamat_);
                    param.put(koneksi.isi, keterangan.getText().toString());
                    param.put(koneksi.bidang, bid);
                    param.put(koneksi.gambar, imageTostring(bitmap));
                    return param;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(simpan);
        }
    }

    private void ambilgambar() {
        CharSequence[] item = {"Kamera", "Galeri"};
        AlertDialog.Builder request = new AlertDialog.Builder(this)
                .setTitle("Tambah Gambar")
                .setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                //Membuka Kamera Untuk Mengambil Gambar
                                EasyImage.openCamera(PengaduanActivity.this, REQUEST_CODE_CAMERA);
                                break;
                            case 1:
                                //Membuaka Galeri Untuk Mengambil Gambar
                                EasyImage.openGallery(PengaduanActivity.this, REQUEST_CODE_GALLERY);
                                break;
                        }
                    }
                });
        request.create();
        request.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Method Ini Digunakan Untuk Menghandle Error pada Image
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Method Ini Digunakan Untuk Menghandle Image
                switch (type) {
                    case REQUEST_CODE_CAMERA:
                        Glide.with(PengaduanActivity.this)
                                .asBitmap()
                                .load(imageFile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        gambar.setImageBitmap(resource);
                                        bitmap = resource;
                                    }
                                });
                        break;

                    case REQUEST_CODE_GALLERY:
                        Glide.with(PengaduanActivity.this)
                                .asBitmap()
                                .load(imageFile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        gambar.setImageBitmap(resource);
                                        bitmap = resource;
                                    }
                                });

                        break;
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }
    public void kamera(){
        Intent kamera= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(kamera, REQUEST_CODE_CAMERA);
    }
            private String imageTostring(Bitmap bm){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[]imaBytes=outputStream.toByteArray();
        String encodeImage= Base64.encodeToString(imaBytes,Base64.DEFAULT);
        return encodeImage;
    }

    private void view(String status){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.kategori,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("Hasil");

                                if (status.equals(koneksi.bidang)) {
                                    datakategori.clear();
                                    for (int i = 0; i < result.length(); i++) {
                                        JSONObject c = result.getJSONObject(i);
                                        datakategori.add(c.getString("nama"));
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PengaduanActivity.this, R.layout.item_spinner, datakategori);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    bidang.setAdapter(adapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Tidak ada", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("bidang", status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void bacaPreferensi() {
        SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
        nik_ = pref.getString(koneksi.nik, "0");
        nama_ = pref.getString(koneksi.nama, "0");
        alamat_ = pref.getString(koneksi.alamat, "0");
    }


}