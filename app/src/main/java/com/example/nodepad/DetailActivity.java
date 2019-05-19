package com.example.nodepad;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    private int nodeId;
    private SQLiteDatabase db;
    private EditText editTitle;
    private EditText editContent;
    private TextView date;
    private Button complete;
    private Button delete;
    private Button lead_out;
    private int rId;
    private  String rtitle;
    private String rcontent;
    private String rDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detail);
        init();
        show();
    }

    public void init(){
        editTitle = findViewById(R.id.node_title_detail);
        editContent = findViewById(R.id.node_content_detail);
        date = findViewById(R.id.detail_text_date);
        complete=findViewById(R.id.detail_complete);
        complete.setOnClickListener(this);
        delete = findViewById(R.id.detail_delete);
        delete.setOnClickListener(this);
        lead_out = findViewById(R.id.detail_lead_out);
        lead_out.setOnClickListener(this);
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
            }
        };
        this.db=helper.getReadableDatabase();

        String sql="select * from node2 where nodeId="+"'"+nodeId+"'";
        Cursor cursor= db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                rId=Integer.parseInt(cursor.getString(cursor.getColumnIndex("nodeID")));
                rtitle=cursor.getString(cursor.getColumnIndex("nodeTitle"));
                rcontent=cursor.getString(cursor.getColumnIndex("nodeContent"));
                //从字符串转换为data
                rDate=cursor.getString(cursor.getColumnIndex("saveDate"));

                editTitle.setText(rtitle);
                editContent.setText(rcontent);
                date.setText(rDate);
            }while(cursor.moveToNext());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_lead_out:
                String basePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/1/";
                String fileName="file"+rId+".txt";
                String leadOutConet="标题：\n"+rtitle+"内容: \n"+editContent+"时间: \n"+rDate;
                Toast.makeText(this, "以成功导出文件，备份成功", Toast.LENGTH_LONG).show();
                FileUtils.writeTxtToFile(leadOutConet, basePath, fileName);
                break;
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
            case R.id.detail_delete:
                String deleteSql="delete from node2 where nodeId="+"'"+nodeId+"'";
                db.execSQL(deleteSql);
                Toast.makeText(this, "删除成功", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
