package demo.seriousface.com.WheelView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import demo.seriousface.com.WheelView.adapter.NumericWheelAdapter;
import demo.seriousface.com.WheelView.view.WheelView;

/**
 * Created by 3N on 2017/11/7.
 */

public class WheelViewUtil extends PopupWindow {
    private Context context ;
    private WheelView year , month;

    private View mMenuView;

    public WheelViewUtil(final Context context , final BtnAction btnAction , final int minYear , final int maxYear ,final int curYear , final int curMonth ){
        super(context);
        this.context = context ;

        LayoutInflater inflater = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.view_time_dialog, null);
        year = (WheelView) mMenuView.findViewById(R.id.year);
        initYear(minYear , maxYear);
        month = (WheelView) mMenuView.findViewById(R.id.month);
        initMonth();
        TextView ok = (TextView) mMenuView.findViewById(R.id.set);
        TextView cancel = (TextView) mMenuView.findViewById(R.id.cancel);

        year.setCurrentItem(curYear);
        month.setCurrentItem(curMonth);

        // 设置监听

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAction.action(year.getCurrentItem() + minYear , month.getCurrentItem()+1);
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //test
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 初始化年
     */
    private void initYear(int minYear , int maxYear) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(context,minYear, maxYear, "%02d");
        numericWheelAdapter.setItemResource(R.layout.wheelviewitem_year);
        numericWheelAdapter.setItemTextResource(R.id.tv_year);
        year.setViewAdapter(numericWheelAdapter);
        year.setCyclic(true);
        year.setDrawShadows(true);
    }

    /**
     * 初始化月
     */
    private void initMonth() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(context,1, 12, "%02d");
        numericWheelAdapter.setItemResource(R.layout.wheelviewitem_month);
        numericWheelAdapter.setItemTextResource(R.id.tv_month);
        month.setViewAdapter(numericWheelAdapter);
        month.setCyclic(true);
        month.setDrawShadows(true);
    }

    public interface BtnAction{
        public void action(int year, int month);
    }
}
