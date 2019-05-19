package com.example.nodepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout linearLayout;
    private LinearLayout sreachLayout;
    private LinearLayout imageLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    public void init(){
        linearLayout = findViewById(R.id.to_text_edit);
        linearLayout.setOnClickListener(this);
        sreachLayout = findViewById(R.id.to_sreach);
        sreachLayout.setOnClickListener(this);
        imageLayout=findViewById(R.id.to_image_edit);
        imageLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_text_edit:
                startActivity(new Intent(this, EditActivity.class));
                break;
            case R.id.to_sreach:
                startActivity(new Intent(this, SreachActivity.class));
                break;
            case R.id.to_image_edit:
                startActivity(new Intent(this,EditImgActivity.class));
                break;
        }
    }

}
