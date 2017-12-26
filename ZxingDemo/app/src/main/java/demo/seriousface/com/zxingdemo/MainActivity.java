package demo.seriousface.com.zxingdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import demo.seriousface.com.zxingdemo.Zxing.CaptureActivity;
import demo.seriousface.com.zxingdemo.Zxing.QRCodeUtil;

public class MainActivity extends AppCompatActivity {
    private ImageView img ;
    private ImageView img_QR_code;
    private EditText edt;

    /**
     * 是否获取系统权限状态量
     */
    private boolean Is_GetPermission = false ;
    private final int REQUEST_CODE = 0xa1;
    /**
     * 二维码生产工具
     */
    private QRCodeUtil qrCodeUtil = new QRCodeUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Android 系统6.0以上版本，在使用到系统权限的地方需要独立发起申请，此处对系统发起获取调用摄像头权限
        ActivityCompat.requestPermissions(MainActivity.this , new String[]{"android.permission.CAMERA"}, 10000);

        img = (ImageView)findViewById(R.id.img_scan);
        img_QR_code = (ImageView)findViewById(R.id.img_QR_code);
        edt = (EditText)findViewById(R.id.edt);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Is_GetPermission){
                    Intent intent = new Intent(MainActivity.this , CaptureActivity.class);
                    startActivityForResult(intent , REQUEST_CODE);
                }else {
                    Toast.makeText(MainActivity.this , "该应用未被授权调用系统相机权限，暂时无法使用扫码功能" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEND||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(MainActivity.this.getWindow().getDecorView().getWindowToken(), 0);
                    }

                    Toast.makeText(MainActivity.this , "输入的内容已经生成如下二维码", Toast.LENGTH_SHORT).show();

                    Resources res=getResources();
                    Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.seriour_face);
                    qrCodeUtil.createQRImage(img_QR_code , edt.getText().toString() , 100 , 100 , bmp);

                    return true;
                }
                return false;
            }
        });
    }

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

    //二维码扫描回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //我们需要的结果返回
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //result就是二维码扫描的结果。
            String result = data.getStringExtra("RESULT");
            Toast.makeText(MainActivity.this , "扫描的结果" + result + "已经生成如下二维码" , Toast.LENGTH_SHORT).show();

            //将扫描得到的结果生成自己的二维码
            Resources res=getResources();
            Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.seriour_face);
            qrCodeUtil.createQRImage(img_QR_code , result , 100 , 100 , bmp);
        }
    }
}
