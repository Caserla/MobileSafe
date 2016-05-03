package com.wangdiaozhu.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangdiaozhu.mobilesafe.R;

/**
 * Created by Administrator on 2016/4/12.
 */
public class SettingItemClickView extends RelativeLayout{


    private TextView tvTitle;
    private TextView tvDesc;


    public SettingItemClickView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public SettingItemClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();



    }

    public SettingItemClickView(Context context) {
        super(context);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        View child = View.inflate(getContext(), R.layout.setting_item_click_view,
                null);// 初始化组合控件布局

        tvTitle = (TextView) child.findViewById(R.id.tv_title);
        tvDesc = (TextView) child.findViewById(R.id.tv_desc);


        this.addView(child);// 将布局添加给当前的RelativeLayout对象
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {

        tvTitle.setText(title);
    }

    /**
     * 设置表述
     *
     * @param desc
     */
    public void setDesc(String desc) {
        tvDesc.setText(desc);
    }


}
