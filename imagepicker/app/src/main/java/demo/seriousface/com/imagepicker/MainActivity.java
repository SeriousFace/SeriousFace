package demo.seriousface.com.imagepicker;

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

public class MainActivity extends AppCompatActivity {
    private GridView gv;

    private UploadPhotoAdapter adapter ;
    private static AndroidImagePicker mInstance ;
    private List<Map<String,String>> list = new ArrayList();
    private boolean Is_GetPermission = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gv = (GridView)findViewById(R.id.gv);

        //Android 系统6.0以上版本，在使用到系统权限的地方需要独立发起申请，此处对系统发起获取查看图库的权限
        ActivityCompat.requestPermissions(MainActivity.this , new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 10000);

        mInstance = AndroidImagePicker.getInstance(true);
        AndroidImagePicker.getInstance().setMaxSelect(9);
        AndroidImagePicker.getInstance().setSelectLimit(9);

        Map<String,String> map  = new HashMap<>();
        map.put("url" , "");
        map.put("status","1");
        list.add(map);
        adapter = new UploadPhotoAdapter(MainActivity.this, list, action);
        gv.setAdapter(adapter);
    }

    UploadPhotoAdapter.Action action= new  UploadPhotoAdapter.Action(){
        @Override
        public void setAction(int position) {
            if(Is_GetPermission){
                Map<String,String> map = list.get(list.size()-1);
                if (list.size() == AndroidImagePicker.getInstance().getMaxSelect() && !map.get("status").equalsIgnoreCase("1")){
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
            }else {
                Toast.makeText(MainActivity.this , "获取权限失败" , Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void setPhoto() {
            if(Is_GetPermission){
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

                            if(list.size()<AndroidImagePicker.getInstance().getMaxSelect()){
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
            }else {
                Toast.makeText(MainActivity.this , "获取权限失败" , Toast.LENGTH_SHORT).show();
            }
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
