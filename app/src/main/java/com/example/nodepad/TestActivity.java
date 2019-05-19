package com.example.nodepad;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    private SQLiteDatabase db;
    private Button button;
    private Button selectbtn;
    private ImageView imageView;
    private ImageView selectimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        createSQL();
        button = (Button) findViewById(R.id.choose);
        imageView = (ImageView) findViewById(R.id.imageview);
        selectbtn=findViewById(R.id.select);
        selectimg = findViewById(R.id.selectimageview);
        button.setOnClickListener(this);
        selectbtn.setOnClickListener(this);
    }

    public void createSQL(){
        String path="test.db";
        SQLiteOpenHelper helper=new SQLiteOpenHelper(this,path,null,2) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                //创建
                String sql = "create table test(id integer primary key autoincrement," + "img BLOB)";
                db.execSQL(sql);
            }
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                //升级
                String sql1="drop table test";
                db.execSQL(sql1);

                String sql = "create table test(id integer primary key autoincrement," + "img BLOB)";
                db.execSQL(sql);
            }
        };
        this.db=helper.getReadableDatabase();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choose:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
                break;
            case R.id.select:
                selectimg.setImageBitmap(readImageFromDb(4));
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                imageView.setImageURI(uri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                    ContentValues values = new ContentValues();
                    values.put("id",4);
                    values.put("img", os.toByteArray());
                    db.insert("test", null, values);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap readImageFromDb(Integer id) {
        Bitmap img = null;
        byte[] bytes;
        String sql = "SELECT * FROM test WHERE id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{id.toString()});
        if (cursor.moveToFirst()) {
            if ((bytes = cursor.getBlob(cursor.getColumnIndex("img"))) != null) {
                img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }
        cursor.close();
        return img;
    }

}
