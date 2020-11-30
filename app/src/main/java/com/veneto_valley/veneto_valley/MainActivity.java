package com.veneto_valley.veneto_valley;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.veneto_valley.veneto_valley.qr.GeneraQR;
import com.veneto_valley.veneto_valley.qr.ScanQR;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Homepage");
        
    }
    
    public void generaTavolo(View view) {
        Intent intent = new Intent(MainActivity.this, GeneraQR.class);
        startActivity(intent);
    }
    
    public void unisciTavolo(View view) {
        Intent intent = new Intent(MainActivity.this, ScanQR.class);
        startActivity(intent);
    }
}