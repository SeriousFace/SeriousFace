package demo.seriousface.com.imagepicker.Util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.pizidea.imagepicker.AndroidImagePicker;
import com.tbruyelle.rxpermissions2.Permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadPhotoUtil {
    private Context context;
    private Activity Myactivity;
    private ChoosePhoto MychoosePhoto;

    //初始化上传图片控件相关参数
    public void initView(Context context , int MaxSelect , List<Map<String,String>> list){
        this.context = context ;
        AndroidImagePicker.getInstance().setMaxSelect(MaxSelect);//最大选择数
        AndroidImagePicker.getInstance().setSelectLimit(MaxSelect);//还可以选择数

        Map<String,String> map  = new HashMap<>();
        map.put("url" , "");
        map.put("status","1");
        list.add(map);
    }

    //获取查看图片权限
    public void getPhototPermission(Activity activity , final ChoosePhoto choosePhoto) {
        this.Myactivity = activity;
        this.MychoosePhoto = choosePhoto;
        PermissionUtil.requestEach(Myactivity, "android.permission.WRITE_EXTERNAL_STORAGE" ,new PermissionUtil.OnPermissionListener() {
            @Override
            public void callback(Permission permission) {
                if (permission.granted) {
                    getCAMERAPermission();
                }else if (permission.shouldShowRequestPermissionRationale == true) {
                    //禁止授权
                    Toast.makeText(context , "您拒绝了授权，无法正常使用" , Toast.LENGTH_SHORT).show();
                } else if (permission.shouldShowRequestPermissionRationale == false) {
                    //禁止授权且不再询问
                    Toast.makeText(context , "您禁止了授权，请在手机设置里面授权" , Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //获取摄像机权限
    private void getCAMERAPermission() {
        PermissionUtil.requestEach(Myactivity, "android.permission.CAMERA" ,new PermissionUtil.OnPermissionListener() {
            @Override
            public void callback(Permission permission) {
                if (permission.granted) {
                    MychoosePhoto.action();
                }else if (permission.shouldShowRequestPermissionRationale == true) {
                    //禁止授权
                    Toast.makeText(context , "您拒绝了授权，无法正常使用" , Toast.LENGTH_SHORT).show();
                } else if (permission.shouldShowRequestPermissionRationale == false) {
                    //禁止授权且不再询问
                    Toast.makeText(context , "您禁止了授权，请在手机设置里面授权" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface ChoosePhoto{
        public void action();
    }
}
