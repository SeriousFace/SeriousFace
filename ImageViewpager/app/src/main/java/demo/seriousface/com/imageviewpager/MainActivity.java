package demo.seriousface.com.imageviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

	private ImageCycleView mAdView;
	private ArrayList<String> mImageUrl = null;
	private String imageUrl1 = "http://p0.so.qhmsg.com/bdr/_240_/t012125208fe1464c1f.jpg";
	private String imageUrl2 = "http://p1.so.qhimgs1.com/bdr/_240_/t01bc941e94aafef1bd.jpg";
	private String imageUrl3 = "http://p5.so.qhimgs1.com/bdr/_240_/t013ea35aa905e0053b.jpg";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mImageUrl = new ArrayList<String>();
		mImageUrl.add(imageUrl1);
		mImageUrl.add(imageUrl2);
		mImageUrl.add(imageUrl3);
		mAdView = (ImageCycleView) findViewById(R.id.ad_view);
		mAdView.setImageResources(mImageUrl , mAdCycleViewListener);
	}

	private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
		@Override
		public void onImageClick(int position, View imageView) {
			Toast.makeText(MainActivity.this, "点击了"+position, Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		mAdView.startImageCycle();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mAdView.pushImageCycle();
	}
}
