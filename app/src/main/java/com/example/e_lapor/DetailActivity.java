package com.example.e_lapor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

public class DetailActivity extends AppCompatActivity {
    TextView tgl, tanggapan, isi, stat, bidang;
    ImageView gmb;
    CardView cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list);
        gmb=findViewById(R.id.dialog_foto);
        tgl=findViewById(R.id.dialog_tanggal);
        bidang=findViewById(R.id.dialog_kategori);
       isi=findViewById(R.id.dialog_isi);
        tanggapan=findViewById(R.id.dialog_tanggapan);
        stat=findViewById(R.id.dialog_status);
        cd=findViewById(R.id.dialog_cd);

        Intent intent=getIntent();
        String us=intent.getStringExtra(koneksi.tanggal);
        String tanggap=intent.getStringExtra(koneksi.tanggapan);
        String is=intent.getStringExtra(koneksi.isi);
        String gm=intent.getStringExtra(koneksi.gambar);
        String status=intent.getStringExtra(koneksi.status);
        String kat=intent.getStringExtra(koneksi.bidang);
        String wr=intent.getStringExtra("warna");
        tgl.setText(us);
        stat.setText(status);
        tanggapan.setText(tanggap);
        isi.setText(is);
        bidang.setText(kat);
        Glide.with(DetailActivity.this).load(gm).
                transition(new DrawableTransitionOptions().crossFade()).
                apply(new RequestOptions())
                .placeholder(R.color.colorPrimary)
                .into(gmb);

        cd.setCardBackgroundColor(Color.parseColor(wr));
        stat.setBackgroundColor(Color.parseColor(wr));
    }

}