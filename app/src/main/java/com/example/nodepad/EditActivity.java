package com.example.nodepad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.text.Bidi;
import java.text.SimpleDateFormat;

public class EditActivity  extends AppCompatActivity implements View.OnClickListener{

    private SQLiteDatabase db;
    private Button completeButton;
    private EditText nodeTitle;
    private EditText nodeContent;
    private Button changeBackground;
    private Boolean IsChange=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit);

        init();
    }
    public void init(){
        completeButton=findViewById(R.id.complete);
        completeButton.setOnClickListener(this);

        changeBackground=findViewById(R.id.change_background);
        changeBackground.setOnClickListener(this);

        nodeTitle=findViewById(R.id.node_title);

        nodeContent=findViewById(R.id.node_content);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_background:
                if(IsChange==false){
                    nodeContent.setBackgroundResource(R.drawable.bachground);
                    IsChange=true;
                }else {
                    nodeContent.setBackgroundColor(Color.parseColor("#ffffff"));
                    IsChange=false;
                }break;
            case R.id.complete:
                String path="node2.db";
                SQLiteOpenHelper helper=new SQLiteOpenHelper(this,path,null,19) {
                    @Override
                    public void onCreate(SQLiteDatabase db) {
                        //创建
                        String sql = "create table node2(nodeID integer primary key autoincrement," + "nodeTitle varchar(50)," + "nodeContent varchar(600),"+"sex varchar(20),"+"saveDate date,"+"changeDate date,"+"image BLOB)";
                        db.execSQL(sql);
                    }
                    @Override
                    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                        //升级
                    }
                };

                this.db=helper.getReadableDatabase();
                //获取标题
                String editTitle=nodeTitle.getText().toString();
                //获取内容
                String editContent=nodeContent.getText().toString();
                //获取当前日期
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date currentDate = new Date(System.currentTimeMillis());

                Bitmap bitmap=null;

                String insertSql="insert into node2(nodeTitle,nodeContent,saveDate)values(?,?,?)";
                db.execSQL(insertSql,new String[]{editTitle,editContent,simpleDateFormat.format(currentDate)});

                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}