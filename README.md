

# 期中实验汇报

## 实验基础功能展示

### 1.列表显示时间戳
为了UI美观只显示了月和日 

![](https://github.com/huangzichun666/node/blob/master/image/1.png)


详细的时间戳在笔记本详情页面中,详情界面表中也可以进行此日记的修改和删除功能

![](https://github.com/huangzichun666/node/blob/master/image/8.png)


### 2.根据标题进行笔记的搜索功能
查询过后如果有则跳转详情界面，如果没有则Toast提示

![](https://github.com/huangzichun666/node/blob/master/image/6.png)


## 实验附加功能展示

### 1.UI美化
主页面采用时间轴布局，并采用悬浮设置按钮，并可以显示图片和文本两种形式的笔记概览

![](https://github.com/huangzichun666/node/blob/master/image/1.png)

设置按钮内容

![](https://github.com/huangzichun666/node/blob/master/image/2.png)


### 2.上传图片
用户可以在设置界面点击图片笔记本跳转到编辑界面，按上传图片按钮，跳转到本地的相册进行图片的选择

![](https://github.com/huangzichun666/node/blob/master/image/4.png)

显示到编辑界面

![](https://github.com/huangzichun666/node/blob/master/image/5.png)

图片笔记详情界面

![](https://github.com/huangzichun666/node/blob/master/image/9.png)


### 3.笔记导出txt文件保存到手机本地的sd卡上

![](https://github.com/huangzichun666/node/blob/master/image/7.png)

### 4.更改笔记编辑背景
在编辑界面点击更换背景，编辑界面将改变背景

![](https://github.com/huangzichun666/node/blob/master/image/3.png)


## 实验重要代码展示

### 1.主界面activity

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


### 2.列表adapter实现

public class NodeAdapter extends BaseAdapter {

    private List<Node> listNode;
    private Context context;
    private LayoutInflater layoutInflater;

    public NodeAdapter(Context context,List<Node> listNode){
        this.context=context;
        this.listNode=listNode;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return listNode.size();
    }

    @Override
    public Object getItem(int position) {
        return listNode.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.layout_overview_item, null);
            viewHolder.nodeTitle = convertView.findViewById(R.id.title);
            viewHolder.nodeContent=convertView.findViewById(R.id.content);
            viewHolder.Savemonth=convertView.findViewById(R.id.month);
            viewHolder.SaveDay = convertView.findViewById(R.id.day);
            viewHolder.image = convertView.findViewById(R.id.content_img);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nodeTitle.setText(listNode.get(position).getNodeTitle());
        viewHolder.nodeContent.setText(listNode.get(position).getNodeContent());

        viewHolder.Savemonth.setText(returnMonthDay(listNode.get(position).getSaveDate())[0]+"月");
        viewHolder.SaveDay.setText(returnMonthDay(listNode.get(position).getSaveDate())[1]);
        if(listNode.get(position).getImage()==null){
            viewHolder.image.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.image.setImageBitmap(listNode.get(position).getImage());
        }
        return convertView;
    }

    public class ViewHolder{
        public TextView nodeTitle;
        public TextView nodeContent;
        public TextView Savemonth;
        public TextView SaveDay;
        public ImageView image;
    }
    public String[] returnMonthDay(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(date==null){
            Log.d("1","hello");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date=new Date(System.currentTimeMillis());
        }
        java.util.Date utilDate = new Date(date.getTime());
        String stringDate = sdf.format(utilDate);
        String[] monthDay=new String[2];
        String[] dates=stringDate.split("-");
        monthDay[0]=dates[1];
        monthDay[1]=dates[2];
        return monthDay;
    }
}


### 3.文件导出工具类

public class FileUtils {
    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    //生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    //生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    //读取指定目录下的所有TXT文件的文件内容
    public static String getFileContent(File file) {
        String content = "";
        if (!file.isDirectory()) {  //检查此路径名的文件是否是一个目录(文件夹)
            if (file.getName().endsWith("txt")) {//文件格式为""文件
                try {
                    InputStream instream = new FileInputStream(file);
                    if (instream != null) {
                        InputStreamReader inputreader
                                = new InputStreamReader(instream, "UTF-8");
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line = "";
                        //分行读取
                        while ((line = buffreader.readLine()) != null) {
                            content += line + "\n";
                        }
                        instream.close();//关闭输入流
                    }
                } catch (java.io.FileNotFoundException e) {
                    Log.d("TestFile", "The File doesn't not exist.");
                } catch (IOException e) {
                    Log.d("TestFile", e.getMessage());
                }
            }
        }
        return content;
    }
}
 
 ### 4.编辑activity
 
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
