package demo.seriousface.com.WheelView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private TextView tv;

    private WheelViewUtil wheelViewUtil ;
    Calendar c = Calendar.getInstance();
    int curYear = c.get(Calendar.YEAR);
    int curMonth = c.get(Calendar.MONTH)+1;
    int minYear = 1900;//时间选择器最小的年份值
    int maxYear = curYear;//时间选择器最大的年份值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wheelViewUtil = new WheelViewUtil(MainActivity.this , btnAction , minYear , maxYear  ,  curYear-minYear , curMonth-1);
                wheelViewUtil.showAtLocation(MainActivity.this.findViewById(R.id.ly), Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
            }
        });
    }

    WheelViewUtil.BtnAction btnAction = new WheelViewUtil.BtnAction() {
        @Override
        public void action(int year , int month) {
            String MyMonth = month>9 ? month+"月" : "0"+month+"月";
            tv.setText(year + "年" + MyMonth);

            curYear = year;
            curMonth = month;
        }
    };
}
