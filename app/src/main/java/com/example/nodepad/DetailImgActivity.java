package com.example.nodepad;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class DetailImgActivity extends AppCompatActivity implements View.OnClickListener{

    private int nodeId;
    private SQLiteDatabase db;
    private EditText editTitle;
    private EditText editContent;
    private ImageView imageView;
    private TextView date;
    private Button complete;
    private Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image_detail);
        init();
        show();
    }

    public void init(){
        editTitle = findViewById(R.id.node_title_detail_img);
        editContent = findViewById(R.id.node_content_detail_img);
        date = findViewById(R.id.detail_text_date_img);
        imageView=findViewById(R.id.node_content_img);
        complete=findViewById(R.id.detail_complete_img);
        complete.setOnClickListener(this);
        delete = findViewById(R.id.detail_delete_img);
        delete.setOnClickListener(this);
    }
    public void show(){
        Intent intent=getIntent();
        nodeId=intent.getIntExtra("nodeId",0);

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
                String sql1="drop table node2";
                db.execSQL(sql1);
                String sql = "create table node2(nodeID integer primary key autoincrement," + "nodeTitle varchar(50)," + "nodeContent varchar(600),"+"sex varchar(20),"+"saveDate date,"+"changeDate date,"+"image BLOB)";
                db.execSQL(sql);
            }
        };
        this.db=helper.getReadableDatabase();

        String sql="select * from node2 where nodeId="+"'"+nodeId+"'";
        Cursor cursor= db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                String rtitle=cursor.getString(cursor.getColumnIndex("nodeTitle"));
                String rcontent=cursor.getString(cursor.getColumnIndex("nodeContent"));
                //从字符串转换为data
                String rDate=cursor.getString(cursor.getColumnIndex("saveDate"));

                //从数据库中提取图片信息
                Bitmap img = null;
                byte[] bytes;
                if ((bytes = cursor.getBlob(cursor.getColumnIndex("image"))) != null) {
                    img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                }


                editTitle.setText(rtitle);
                imageView.setImageBitmap(img);
                editContent.setText(rcontent);
                date.setText(rDate);
            }while(cursor.moveToNext());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_complete:
                String cdeleteSql="delete from node2 where nodeId="+"'"+nodeId+"'";
                db.execSQL(cdeleteSql);

                //获取标题
                String editTitletext=editTitle.getText().toString();
                //获取内容
                String editContenttext=editContent.getText().toString();
                //获取当前日期
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date currentDate = new Date(System.currentTimeMillis());

                String insertSql="insert into node2(nodeTitle,nodeContent,saveDate)values(?,?,?)";

                db.execSQL(insertSql,new String[]{editTitletext,editContenttext,simpleDateFormat.format(currentDate)});
                Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();

                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.detail_delete_img:
                String deleteSql="delete from node2 where nodeId="+"'"+nodeId+"'";
                db.execSQL(deleteSql);
                Toast.makeText(this, "删除成功", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
