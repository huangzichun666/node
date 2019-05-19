package com.example.nodepad;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

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
