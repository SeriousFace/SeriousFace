package demo.seriousface.com.imagepicker.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

import demo.seriousface.com.imagepicker.R;

/**
 *  GridView 适配器
 */
public class UploadPhotoAdapter extends BaseAdapter {
    //上下文对象
    private Context context;
    private List<Map<String,String>> list ;
    private Action action;

    public UploadPhotoAdapter(Context context , List<Map<String,String>> list , Action action){
        this.context = context;
        this.list = list;
        this.action = action;
    }
    public int getCount() {
        return list.size();
    }

    public Object getItem(int item) {
        return item;
    }

    public long getItemId(int id) {
        return id;
    }

    //创建View方法
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;

        convertView = LayoutInflater.from(context).inflate(R.layout.gridview_upload_photo,parent,false);
        vh = new ViewHolder();
        vh.img_one = (ImageView) convertView.findViewById(R.id.img_one);
        vh.img_delete_one = (ImageView) convertView.findViewById(R.id.img_delete_one);

        Map<String,String> map = list.get(position);

        if(map.get("status").equalsIgnoreCase("1")){
            vh.img_one.setImageResource(R.drawable.ico70_postimg);
            vh.img_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    action.setPhoto();
                }
            });
        }else{
            vh.img_one.setScaleType(ImageView.ScaleType.CENTER);
            vh.img_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    action.setCheckPhoto(position);
                }
            });
            Glide.with(context).load(map.get("url")).into(vh.img_one);
            vh.img_delete_one.setVisibility(View.VISIBLE);
            vh.img_delete_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    action.setAction(position);
                }
            });
        }

        return convertView;
    }

    class ViewHolder{
        ImageView img_one , img_delete_one ;
    }

    public interface Action{
        public void setAction(int position);
        public void setPhoto();
        public void setCheckPhoto(int position);
    }
}


