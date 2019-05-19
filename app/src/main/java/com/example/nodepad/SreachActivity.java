package com.example.nodepad;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SreachActivity  extends AppCompatActivity implements View.OnClickListener{


    private EditText sreachEdit;
    private Button buttton_sreach;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sreach);
        init();

    }

    public void init() {
        sreachEdit = findViewById(R.id.sreacch);
        buttton_sreach = findViewById(R.id.button_sreach);
        buttton_sreach.setOnClickListener(this);

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


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_sreach:
                String sreachEditString = sreachEdit.getText().toString();
                String sql= "select * from node2 where nodeTitle="+"'"+sreachEditString+"'" ;
                Cursor cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
                if(cursor.getCount()!=0){

                    //从数据库中提取图片信息
                    Bitmap img = null;
                    byte[] bytes;
                    if ((bytes = cursor.getBlob(cursor.getColumnIndex("image"))) != null) {
                        img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    }
                    if(img==null){
                        int rId=Integer.parseInt(cursor.getString(cursor.getColumnIndex("nodeID")));
                        Intent intent=new Intent(SreachActivity.this,DetailActivity.class);
                        intent.putExtra("nodeId",rId);
                        startActivity(intent);
                    }else{
                        int rId=Integer.parseInt(cursor.getString(cursor.getColumnIndex("nodeID")));
                        Intent intent=new Intent(SreachActivity.this,DetailImgActivity.class);
                        intent.putExtra("nodeId",rId);
                        startActivity(intent);
                    }
                }else {
                    Toast.makeText(this, "查询笔记题目为空,请从新输入", Toast.LENGTH_LONG).show();
                    sreachEdit.setText("");
                }break;
        }
    }
}
