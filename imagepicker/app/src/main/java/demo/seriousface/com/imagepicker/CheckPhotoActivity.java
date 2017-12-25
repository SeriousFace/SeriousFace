package demo.seriousface.com.imagepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import demo.seriousface.com.imagepicker.View.ImageCycleView;

/**
 *  查看图片
 */

public class CheckPhotoActivity extends AppCompatActivity {
    private ImageCycleView mAdView;

    private ArrayList<String> mImageUrl = null;
    private String[] ImgUrlGroup ;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_photo);

        ImgUrlGroup = getIntent().getStringArrayExtra("url");
        position = getIntent().getIntExtra("position",1);
        mAdView = (ImageCycleView)findViewById(R.id.ad_view);

        mImageUrl = new ArrayList<String>();
        if(ImgUrlGroup!=null){
            for(int i=0;i<ImgUrlGroup.length;i++){
                mImageUrl.add(ImgUrlGroup[i]);
            }
        }
        mAdView.setImageResources(mImageUrl , null , position);
    }
}
