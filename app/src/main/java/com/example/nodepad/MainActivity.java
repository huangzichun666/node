package com.example.nodepad;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private Node currentNode;
    private SQLiteDatabase db;
    List<Node> listNodes;
    //悬浮窗
    private FloatingActionButton fab_setting;
    private ListView list_node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_general_view);

        init();
    }

    public void init(){
        list_node=findViewById(R.id.list);
        list_node.setOnItemClickListener(this);
        fab_setting=findViewById(R.id.fab_setting);
        fab_setting.setOnClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String text=listNodes.get(position).getNodeTitle();
        int toid = listNodes.get(position).getNodeID();
        Bitmap bitmap=listNodes.get(position).getImage();
        if(bitmap!=null){
            Intent intent=new Intent(MainActivity.this,DetailImgActivity.class);
            intent.putExtra("nodeId",toid);
            startActivity(intent);
        }else {
            Intent intent=new Intent(MainActivity.this,DetailActivity.class);
            intent.putExtra("nodeId",toid);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

    @Override
    protected void onResume()  {
        super.onResume();
        listNodes = new ArrayList<>();
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
                //升级
                String sql1="drop table node2";
                db.execSQL(sql1);
                String sql = "create table node2(nodeID integer primary key autoincrement," + "nodeTitle varchar(50)," + "nodeContent varchar(600),"+"saveDate date,"+"changeDate date,"+"image BLOB)";
                db.execSQL(sql);
            }
        };
        this.db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from node2 order by nodeID desc",null);
        if(cursor.moveToFirst()){
            do{
                int rId=Integer.parseInt(cursor.getString(cursor.getColumnIndex("nodeID")));
                String rtitle=cursor.getString(cursor.getColumnIndex("nodeTitle"));
                String rcontent=cursor.getString(cursor.getColumnIndex("nodeContent"));

                //从字符串转换为data
                String rDate=cursor.getString(cursor.getColumnIndex("saveDate"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                //从数据库中提取图片信息
                Bitmap img = null;
                byte[] bytes;
                if ((bytes = cursor.getBlob(cursor.getColumnIndex("image"))) != null) {
                    img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Log.d("huang","1");
                }

                Date dateTime = null;
                try {
                    dateTime = new Date(format.parse(rDate).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                currentNode=new Node(rtitle,rcontent,dateTime,rId,img);
                listNodes.add(currentNode);
            }while(cursor.moveToNext());
        }
        NodeAdapter nodeAdapter=new NodeAdapter(this,listNodes);
        list_node.setAdapter(nodeAdapter);
    }
}
