package com.example.e_lapor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    CardView profil, pengaduan, history, keluar;
    String hariIni,nik_, nama_, tgll, alamat_;
    TextView dat;
    Animation animTv;
    TextView t_nik, t_nama, t_salam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        bacaPreferensi();
        profil=findViewById(R.id.cv_profil);
        pengaduan=findViewById(R.id.cv_pengaduan);
        history=findViewById(R.id.cv_history);
        keluar=findViewById(R.id.cv_keluar);
        dat=findViewById(R.id.tvDate);
        t_nik=findViewById(R.id.td_nik);
        t_nama=findViewById(R.id.td_nama);
        t_salam=findViewById(R.id.td_salam);

        t_nik.setText(""+nik_);
        t_nama.setText(""+nama_);

        profil.setOnClickListener(this);
        pengaduan.setOnClickListener(this);
        history.setOnClickListener(this);
        keluar.setOnClickListener(this);

        Date dateNow = Calendar.getInstance().getTime();
        animTv = AnimationUtils.loadAnimation(this, R.anim.anim_tv);
        hariIni = (String) DateFormat.format("EEEE", dateNow);
        dat.setAnimation(animTv);
        t_salam.startAnimation(animTv);
        if (hariIni.equalsIgnoreCase("sunday")) {
            hariIni = "Minggu";
        } else if (hariIni.equalsIgnoreCase("monday")) {
            hariIni = "Senin";
        } else if (hariIni.equalsIgnoreCase("tuesday")) {
            hariIni = "Selasa";
        } else if (hariIni.equalsIgnoreCase("wednesday")) {
            hariIni = "Rabu";
        } else if (hariIni.equalsIgnoreCase("thursday")) {
            hariIni = "Kamis";
        } else if (hariIni.equalsIgnoreCase("friday")) {
            hariIni = "Jumat";
        } else if (hariIni.equalsIgnoreCase("saturday")) {
            hariIni = "Sabtu";
        }

        getToday();
        setSalam();
    }
    private void setSalam() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 12) {
            t_salam.setText("Selamat Pagi");
        } else if (timeOfDay >= 12 && timeOfDay < 15) {
            t_salam.setText("Selamat Siang");
        } else if (timeOfDay >= 15 && timeOfDay < 18) {
            t_salam.setText("Selamat Sore");
        } else if (timeOfDay >= 18 && timeOfDay < 24) {
            t_salam.setText("Selamat Malam");
        }
    }


    private void getToday() {
        Date date = Calendar.getInstance().getTime();
        String tanggal = (String) DateFormat.format("d", date);
        String monthNumber = (String) DateFormat.format("M", date);
        String year = (String) DateFormat.format("yyyy", date);

        int month = Integer.parseInt(monthNumber);
        String bulan = null;
        if (month == 1) {
            bulan = "Januari";
        } else if (month == 2) {
            bulan = "Februari";
        } else if (month == 3) {
            bulan = "Maret";
        } else if (month == 4) {
            bulan = "April";
        } else if (month == 5) {
            bulan = "Mei";
        } else if (month == 6) {
            bulan = "Juni";
        } else if (month == 7) {
            bulan = "Juli";
        } else if (month == 8) {
            bulan = "Agustus";
        } else if (month == 9) {
            bulan = "September";
        } else if (month == 10) {
            bulan = "Oktober";
        } else if (month == 11) {
            bulan = "November";
        } else if (month == 12) {
            bulan = "Desember";
        }
        tgll= tanggal + " " + bulan + " " + year;
        String formatFix = hariIni + ", " +tgll ;
        dat.setText(formatFix);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv_profil:
                Intent pro=new Intent(DashboardActivity.this, ProfilActivity.class);
                startActivity(pro);

                break;
            case R.id.cv_pengaduan:
                Intent pen=new Intent(DashboardActivity.this, PengaduanActivity.class);
                pen.putExtra(koneksi.tgl, tgll);
                startActivity(pen);

                break;
            case R.id.cv_history:
                Intent his=new Intent(DashboardActivity.this, HistoryActivity.class);
                startActivity(his);
                break;
            case R.id.cv_keluar:
                Intent keluar = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(keluar);
                SharedPreferences pref = getSharedPreferences("akun",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(koneksi.nik, "0");
                editor.commit();
                finish();
                break;
        }
    }
    private void bacaPreferensi() {
        SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
        nik_ = pref.getString(koneksi.nik, "0");
        nama_ = pref.getString(koneksi.nama, "0");
        alamat_ = pref.getString(koneksi.alamat, "0");
    }
}