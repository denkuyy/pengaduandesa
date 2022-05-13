package com.example.e_lapor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> data;
    ArrayList<HashMap<String, String>> tampil=new ArrayList<HashMap<String, String>>();

    public adapter(Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list, null);
        MyHolder holder= new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        HashMap<String, String> data_tampil= new HashMap<String, String>();
        data_tampil=data.get(position);
        myHolder.mtex1.setText(data_tampil.get(koneksi.tanggal));
        myHolder.mtex2.setText(data_tampil.get(koneksi.bidang));
        myHolder.mtex3.setText(data_tampil.get(koneksi.isi));
        myHolder.mtex4.setText(data_tampil.get(koneksi.status));
        Glide.with(context).load(data_tampil.get(koneksi.gambar)).
                transition(new DrawableTransitionOptions().crossFade()).
                apply(new RequestOptions())
                .placeholder(R.color.colorPrimary)
                .into(myHolder.thubnail);
        String a=data_tampil.get(koneksi.gambar);
        String warna=data_tampil.get("warna");
        myHolder.crd.setCardBackgroundColor(Color.parseColor(warna));
        myHolder.mtex4.setBackgroundColor(Color.parseColor(warna));
        myHolder.mtex5.setText(data_tampil.get(koneksi.tanggapan));
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra(koneksi.tanggal, myHolder.mtex1.getText());
                intent.putExtra(koneksi.isi,myHolder.mtex3.getText());
                intent.putExtra(koneksi.bidang,myHolder.mtex2.getText());
                intent.putExtra(koneksi.gambar,a);
                intent.putExtra(koneksi.status,myHolder.mtex4.getText());
                intent.putExtra(koneksi.tanggapan,myHolder.mtex5.getText());
                intent.putExtra("warna",warna);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

   class MyHolder extends RecyclerView.ViewHolder {
        TextView mtex1, mtex2, mtex3, mtex4, mtex5;
        ImageView thubnail;
        CardView crd;
        public MyHolder(View itemView){
            super(itemView);
             mtex1 = (TextView) itemView.findViewById(R.id.tvTanggal);
             mtex2 = (TextView) itemView.findViewById(R.id.tv_bidang);
            mtex3 = (TextView) itemView.findViewById(R.id.tv_isi);
            mtex4 = (TextView) itemView.findViewById(R.id.tv_status);
            mtex5 = (TextView) itemView.findViewById(R.id.tv_tanggapan);
             thubnail= (ImageView) itemView.findViewById(R.id.tv_img);
             crd= (CardView) itemView.findViewById(R.id.card);
        }

        }

}
