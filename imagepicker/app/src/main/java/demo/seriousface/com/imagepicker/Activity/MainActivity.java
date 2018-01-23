package demo.seriousface.com.imagepicker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.seriousface.com.imagepicker.Adapter.UploadPhotoAdapter;
import demo.seriousface.com.imagepicker.R;
import demo.seriousface.com.imagepicker.Util.UploadPhotoUtil;

public class MainActivity extends AppCompatActivity {
    private GridView gv;

    private UploadPhotoUtil uploadPhotoUtil = new UploadPhotoUtil();
    private int MaxSelect = 8;

    private UploadPhotoAdapter adapter ;
    private static AndroidImagePicker mInstance ;
    private List<Map<String,String>> list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gv = (GridView)findViewById(R.id.gv);

        //初始化上传图片控件
        mInstance = AndroidImagePicker.getInstance(true);
        uploadPhotoUtil.initView(this , MaxSelect , list);
        adapter = new UploadPhotoAdapter(MainActivity.this, list, action);
        gv.setAdapter(adapter);
    }

    //上传图片适配器回调函数
    UploadPhotoAdapter.Action action = new UploadPhotoAdapter.Action() {
        //删除图片
        @Override
        public void setDeleteAction(int position) {
            Map<String,String> map = list.get(list.size()-1);
            if (list.size() == MaxSelect && !map.get("status").equalsIgnoreCase("1")){
                list.remove(position);
                Map<String,String> map2  = new HashMap<>();
                map2.put("url" , "");
                map2.put("status","1");
                list.add(map2);
            }else {
                list.remove(position);
            }

            AndroidImagePicker.getInstance().setChooseNum(list.size()-1);
            AndroidImagePicker.getInstance().setSelectLimit(AndroidImagePicker.getInstance().getMaxSelect() - list.size() + 1);
            adapter.notifyDataSetChanged();
        }

        //选择图片
        @Override
        public void setChoosePhotoAction() {
            //请求读写SD卡授权
            uploadPhotoUtil.getPhototPermission(MainActivity.this ,choosePhoto);
        }

        //点击已经选择好的图片，进行查看图片操作
        @Override
        public void setCheckPhotoAction(int position) {
            String[] ImgUrlGroup ;//初始化有效图片资源数组
            if(list.get(list.size()-1).get("status").equalsIgnoreCase("1")){//如果gridview中包含默认获取图片资源按钮，则不把该数据保持到数组中
                ImgUrlGroup = new String[list.size()-1];
            }else {
                ImgUrlGroup = new String[list.size()];
            }
            if (ImgUrlGroup!=null && ImgUrlGroup.length>0){
                for(int i=0;i<list.size();i++){//循环将list中的有效图片资源保存到数组中
                    if(list.get(i).get("status").equalsIgnoreCase("0")){{
                        ImgUrlGroup[i] = list.get(i).get("url");
                    }}
                }
            }

            Intent intent = new Intent(MainActivity.this , CheckPhotoActivity.class);
            intent.putExtra("url" , ImgUrlGroup);
            intent.putExtra("position",position);
            startActivity(intent);
        }
    };

    //选择图片接口
    UploadPhotoUtil.ChoosePhoto choosePhoto = new UploadPhotoUtil.ChoosePhoto() {
        @Override
        public void action() {
            mInstance.pickMulti(MainActivity.this , true, new AndroidImagePicker.OnImagePickCompleteListener() {
                @Override
                public void onImagePickComplete(List<ImageItem> items) {
                    if(items != null && items.size() > 0){
                        int chooseNum = 0;

                        list.remove(list.size()-1);
                        for(int i=0;i<items.size();i++){
                            Map<String,String> map = new HashMap<>();
                            map.put("url",items.get(i).path);
                            map.put("status","0");
                            list.add(map);
                        }

                        if(list.size()<MaxSelect){
                            Map<String,String> map  = new HashMap<>();
                            map.put("url" , "");
                            map.put("status","1");
                            list.add(map);

                            chooseNum = list.size()-1;
                        }else{
                            chooseNum = list.size();
                        }

                        AndroidImagePicker.getInstance().setChooseNum(chooseNum);
                        AndroidImagePicker.getInstance().setSelectLimit(AndroidImagePicker.getInstance().getMaxSelect() - chooseNum);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };
}
