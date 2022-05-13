package com.example.e_lapor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import static android.content.Context.MODE_PRIVATE;

public class FragmentDiproses extends Fragment {
    RecyclerView rv;
    ArrayList<HashMap<String, String>> tampil = new ArrayList<HashMap<String, String>>();
    adapter adapter;
    String warna, nik_;

    public FragmentDiproses() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frag_diterima = inflater.inflate(R.layout.fragment_diproses, container, false);
        bacaPreferensi();
        rv = frag_diterima.findViewById(R.id.list_v);
        rv.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(getActivity());
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(lim);
        tampil();

        return frag_diterima;

    }

    private void tampil() {

        final StringRequest list = new StringRequest(Request.Method.POST, koneksi.list_proses,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("complaint", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray hasil = jsonObject.getJSONArray("Hasil");
                            tampil.clear();
                            for (int i = 0; i < hasil.length(); i++) {
                                JSONObject c = hasil.getJSONObject(i);
                                String tgl = c.getString(koneksi.tgl);
                                String ket = c.getString(koneksi.isi);
                                String kategori = c.getString(koneksi.bidang);
                                String status = c.getString(koneksi.status);
                                String gambar = c.getString(koneksi.gambar);
                                String tanggap = c.getString(koneksi.tanggapan);

                                if (c.getString(koneksi.status).equals("Ditanggapi")) {
                                    warna = "#B3AF1A1A";
                                } else if (c.getString(koneksi.status).equals("Proses")) {
                                    warna = "#B303A9F4";
                                } else if (c.getString(koneksi.status).equals("Selesai")) {
                                    warna = "#FF9800";
                                }

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(koneksi.tanggal, tgl);
                                map.put(koneksi.isi, ket);
                                map.put(koneksi.bidang, kategori);
                                map.put(koneksi.status, status);
                                map.put(koneksi.gambar, gambar);
                                map.put(koneksi.tanggapan, tanggap);
                                map.put("warna", warna);
                                tampil.add(map);
                            }
                            adapter = new adapter(getActivity(), tampil);
                            rv.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("complain", "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put(koneksi.nik, nik_.toString());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(list);
    }
    private void bacaPreferensi() {
        SharedPreferences pref = getContext().getSharedPreferences("akun", MODE_PRIVATE);
        nik_ = pref.getString(koneksi.nik, "0");

    }
}


