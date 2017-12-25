package demo.seriousface.com.imagepicker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.seriousface.com.imagepicker.Adapter.UploadPhotoAdapter;

public class MainActivity extends AppCompatActivity {
    private GridView gv;

    private UploadPhotoAdapter adapter ;
    private static AndroidImagePicker mInstance ;
    private List<Map<String,String>> list = new ArrayList();
    /**
     * 是否获取系统权限状态量
     */
    private boolean Is_GetPermission = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gv = (GridView)findViewById(R.id.gv);

        //Android 系统6.0以上版本，在使用到系统权限的地方需要独立发起申请，此处对系统发起获取查看图库的权限
        ActivityCompat.requestPermissions(MainActivity.this , new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 10000);

        mInstance = AndroidImagePicker.getInstance(true);
        AndroidImagePicker.getInstance().setMaxSelect(9);//设置最大选择图片数
        AndroidImagePicker.getInstance().setSelectLimit(9);//设置还可以选择图片数

        //给gridview的list添加一个初始的数据，就是刚进入页面看到的那个默认获取图片资源按钮
        Map<String,String> map  = new HashMap<>();
        map.put("url" , "");
        map.put("status","1");
        list.add(map);

        adapter = new UploadPhotoAdapter(MainActivity.this, list, action);
        gv.setAdapter(adapter);
    }

    //gridview 适配器的回掉类，整个girdview的item所有操作都在这里
    UploadPhotoAdapter.Action action= new  UploadPhotoAdapter.Action(){
        @Override
        //点击右上角删除按钮，删除图片操作
        public void setAction(int position) {
            if(Is_GetPermission){//已经获取系统权限，查看图库
                Map<String,String> map = list.get(list.size()-1);
                //特殊情况，选择图片已经到达上限，此情况默认的获取图片资源按钮会被消失，当删除一张已选照片时，该默认的获取图片资源按钮要重新添加
                if (list.size() == AndroidImagePicker.getInstance().getMaxSelect() && !map.get("status").equalsIgnoreCase("1")){
                    list.remove(position);
                    Map<String,String> map2  = new HashMap<>();
                    map2.put("url" , "");
                    map2.put("status","1");
                    list.add(map2);
                }else {//正常情况，选择图片未到达上限，直接删除list中该item
                    list.remove(position);
                }

                AndroidImagePicker.getInstance().setChooseNum(list.size()-1);//更新控件中已经选择图片数
                AndroidImagePicker.getInstance().setSelectLimit(AndroidImagePicker.getInstance().getMaxSelect() - list.size() + 1);//更新控件还可以选择图片数
                adapter.notifyDataSetChanged();//刷新gridview适配器
            }else {//未获取获取系统权限，弹出提示
                Toast.makeText(MainActivity.this , "获取权限失败" , Toast.LENGTH_SHORT).show();
            }
        }
        //点击默认获取图片资源按钮，进行图片选择操作
        @Override
        public void setPhoto() {
            if(Is_GetPermission){//已经获取系统权限，查看图库
                mInstance.pickMulti(MainActivity.this , true, new AndroidImagePicker.OnImagePickCompleteListener() {
                    @Override
                    public void onImagePickComplete(List<ImageItem> items) {
                        if(items != null && items.size() > 0){
                            int chooseNum = 0;//初始化本次选择的图片数
                            list.remove(list.size()-1);//去除list尾部的默认获取图片资源按钮
                            for(int i=0;i<items.size();i++){//循环获取本次选择的图片资源，添加到list中
                                Map<String,String> map = new HashMap<>();
                                map.put("url",items.get(i).path);
                                map.put("status","0");
                                list.add(map);
                            }

                            if(list.size()<AndroidImagePicker.getInstance().getMaxSelect()){//如果选择图片未到达上限，在list尾部添加默认获取图片资源按钮
                                Map<String,String> map  = new HashMap<>();
                                map.put("url" , "");
                                map.put("status","1");
                                list.add(map);

                                chooseNum = list.size()-1;
                            }else{//如果选择图片已经到达上限
                                chooseNum = list.size();
                            }

                            AndroidImagePicker.getInstance().setChooseNum(chooseNum);//更新控件中已经选择图片数
                            AndroidImagePicker.getInstance().setSelectLimit(AndroidImagePicker.getInstance().getMaxSelect() - chooseNum);//更新控件还可以选择图片数
                            adapter.notifyDataSetChanged();//刷新gridview适配器
                        }
                    }
                });
            }else {//未获取获取系统权限，弹出提示
                Toast.makeText(MainActivity.this , "获取权限失败" , Toast.LENGTH_SHORT).show();
            }
        }

        //点击已经选择好的图片，进行查看图片操作
        @Override
        public void setCheckPhoto(int position) {
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

    //系统权限获取情况回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 10000:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //已获取权限
                    Is_GetPermission = true;
                }else{
                    //权限被拒绝
                    Is_GetPermission = false;
                }
                break;
        }
    }
}
