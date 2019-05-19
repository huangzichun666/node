package com.example.nodepad;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class EditImgActivity  extends AppCompatActivity implements View.OnClickListener{

    private SQLiteDatabase db;
    private Button completeButton;
    private Button putUpButton;
    private EditText nodeTitle;
    private EditText nodeContent;
    private ImageView image;
    private Uri uri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image_edit);

        init();
    }
    public void init(){
        completeButton=findViewById(R.id.complete_img);
        completeButton.setOnClickListener(this);
        putUpButton = findViewById(R.id.putUp_img);
        putUpButton.setOnClickListener(this);
        image=findViewById(R.id.node_content_edit_img);
        nodeTitle=findViewById(R.id.node_title_img);
        nodeContent=findViewById(R.id.node_content_img);

        String path="node2.db";
        SQLiteOpenHelper helper=new SQLiteOpenHelper(this,path,null,19) {
                @Override
            public void onCreate(SQLiteDatabase db) {
                //创建
                String sql = "create table node2(nodeID integer primary key autoincrement," + "nodeTitle varchar(50)," + "nodeContent varchar(600),"+"saveDate date,"+"changeDate date,"+"image BLOB)";
                db.execSQL(sql);
            }
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                //升级
            }
        };
        this.db=helper.getReadableDatabase();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.complete_img:
/*                try {*/

/*                    //获取标题
                    String editTitle=nodeTitle.getText().toString();
                    //获取内容
                    String editContent=nodeContent.getText().toString();
                    //获取当前日期
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date currentDate = new Date(System.currentTimeMillis());*/

/*
                    String insertSql="insert into node2(nodeTitle)values(?)";
                    db.execSQL(insertSql,new String[]{editTitle});

                    String sql= "select * from node2 where nodeTitle="+"'"+editTitle+"'" ;
                    Cursor cursor = db.rawQuery(sql, null);
                    cursor.moveToFirst();
                    int rId=Integer.parseInt(cursor.getString(cursor.getColumnIndex("nodeID")));


                    String deleteSql="delete from node2 where nodeID="+"'"+rId+"'";
                    db.execSQL(deleteSql);
*/

/*
                    Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);*/

                    /*
                    ContentValues values = new ContentValues();
                    values.put("nodeID",1);
                    values.put("nodeTitle",editTitle);
                    values.put("nodeContent",editContent);
                    values.put("saveDate",simpleDateFormat.format(currentDate));
                    values.put("changeDate",simpleDateFormat.format(currentDate));
                    values.put("image", os.toByteArray());*/
/*
                    String insertSql="insert into node2(nodeTitle,nodeContent,saveDate)values(?,?,?)";
                    db.execSQL(insertSql,new Object[]{editTitle,editContent,simpleDateFormat.format(currentDate)});*/
                    startActivity(new Intent(this, MainActivity.class));
/*
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                break;

            case R.id.putUp_img:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                uri = data.getData();
                image.setImageURI(uri);
                try {
                    Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                    ContentValues values = new ContentValues();

                    //获取标题
                    String editTitle=nodeTitle.getText().toString();
                    //获取内容
                    String editContent=nodeContent.getText().toString();
                    //获取当前日期
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date currentDate = new Date(System.currentTimeMillis());

                    String insertSql="insert into node2(nodeTitle,nodeContent,saveDate,image)values(?,?,?,?)";
                    db.execSQL(insertSql,new Object[]{editTitle,editContent,simpleDateFormat.format(currentDate),os.toByteArray()});


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}