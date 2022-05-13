package com.example.e_lapor;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class welcome extends AppCompatActivity {
private ViewPager viewPager;
private LinearLayout dotsLayout;
private MyViewPagerAdapter myViewPagerAdapter;
private TextView[] dots;
private int[] layouts;
private Button btnskip, btnnext;
private tampil prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new tampil(this);
        if ((!prefManager.isFirstTimeLuanch())){
            launchHomeScreen();
            finish();
        }
        if(Build.VERSION.SDK_INT>=21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout=(LinearLayout)findViewById(R.id.layoutDots);
        btnskip=(Button)findViewById(R.id.btn_skip);
        btnnext=(Button)findViewById(R.id.btn_next);

        layouts = new int[]{
                R.layout.activity_slide1,
                R.layout.activity_slide2,
                R.layout.activity_slide3};
        addButtomDots(0);
         myViewPagerAdapter = new MyViewPagerAdapter();
         viewPager.setAdapter(myViewPagerAdapter);
         viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

         btnskip.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 launchHomeScreen();
             }
         });
         btnnext.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 int current = getItem(+1);
                 if(current < layouts.length){
                     viewPager.setCurrentItem(current);
                 }else {
                     launchHomeScreen();
                 }
             }
         });

    }

    private void addButtomDots(int currentpage) {
        dots=new TextView[layouts.length];
        int[]colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[]colorsInActive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInActive[currentpage]);
            dotsLayout.addView(dots[i]);

        }
        if (dots.length >0){
            dots[currentpage].setTextColor(colorsActive[currentpage]);
        }

    }
    private int getItem(int i){
        return viewPager.getCurrentItem()+i;
    }

    private void launchHomeScreen() {
        prefManager.setIsFirstTimeLaunch(false);
        startActivity(new Intent(welcome.this, LoginActivity.class));
        finish();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addButtomDots(position);
            if(position==layouts.length){
                btnnext.setText("SELESAI");
                btnskip.setVisibility(View.GONE);
            }else {
                btnnext.setText("LANJUT");
                btnskip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public  MyViewPagerAdapter(){
            }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view= layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
