package com.dachen.community.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.dachen.community.R;

public class BaseActivity extends FragmentActivity implements View.OnClickListener{

    public ProgressDialog mDialog;
    protected ViewFlipper mContentView;
    protected RelativeLayout layout_head;
    protected TextView btn_left;
    protected TextView btn_right;
    protected TextView tv_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.common_base);

        //初始化公共头部
        mContentView = (ViewFlipper) super.findViewById(R.id.layout_container);
        layout_head = (RelativeLayout) super.findViewById(R.id.layout_head);
        btn_left = (TextView) super.findViewById(R.id.btn_left);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_right = (TextView) super.findViewById(R.id.btn_right);
        tv_title = (TextView) super.findViewById(R.id.tv_title);

        initProgressDialog();
    }

    private void initProgressDialog() {
        mDialog = new ProgressDialog(this, R.style.IMDialog);
        // mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage("正在加载");
    }

    @Override
    public void setContentView(View view) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        mContentView.addView(view, lp);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(view);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getViewById(View v, int id) {
        return (T) v.findViewById(id);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        // empty
    }

    /**
     * 设置头部是否可见
     * @param visibility
     */
    public void setHeadVisibility(int visibility) {
        layout_head.setVisibility(visibility);
    }

    /**
     *设置标题
     */
    public void setTitle(int titleId) {
        tv_title.setText(getString(titleId));
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    /**
     * 设置左按钮图标
     * @param resid
     */
    public void setBtnLeft(int resid) {
        Drawable img = getResources().getDrawable(resid);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        btn_left.setCompoundDrawables(img, null, null, null);
        btn_left.setText("");
        btn_left.setVisibility(View.VISIBLE);
    }

    /**
     * 设置左按钮文字
     * @param txt
     */
    public void setBtnLeft(String txt) {
        btn_left.setCompoundDrawables(null, null, null, null);
        btn_left.setText(txt);
        btn_left.setVisibility(View.VISIBLE);
    }

    /**
     * 设置左按钮图标
     * @param resid
     */
    public void setBtnRight(int resid) {
        Drawable img = getResources().getDrawable(resid);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        btn_right.setCompoundDrawables(img, null, null, null);
        btn_right.setText("");
        btn_right.setVisibility(View.VISIBLE);
    }

    /**
     * 设置左按钮文字
     * @param txt
     */
    public void setBtnRight(String txt) {
        btn_right.setCompoundDrawables(null, null, null, null);
        btn_right.setText(txt);
        btn_right.setVisibility(View.VISIBLE);
    }


    /**
     * 设置返回
     */
    public void setBtnBack() {
        Drawable img = getResources().getDrawable(R.drawable.title_bar_back);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        btn_left.setCompoundDrawables(img, null, null, null);
        btn_left.setText("");
        btn_left.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {

    }

}
