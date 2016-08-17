package com.dachen.common.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dachen.common.R;
import com.dachen.common.utils.ToastUtil;

/**
 * 密码控件
 * Created by TianWei on 2016/6/7.
 */
public class PasswordView extends RelativeLayout implements View.OnClickListener, TextWatcher {
    private boolean isShowing;
    private ClearEditText mClearEditText;
    private ImageView mShowPassWordIv;
    private Context mContext;

    public PasswordView(Context context) {
        this(context, null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.layout_password_view, this);
        mContext = context;
        mClearEditText = (ClearEditText) findViewById(R.id.password_view_edit);
        mShowPassWordIv = (ImageView) findViewById(R.id.password_view_show);
        mShowPassWordIv.setOnClickListener(this);
        mClearEditText.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mShowPassWordIv) {
            if (isShowing) {
                isShowing = false;
                mShowPassWordIv.setImageResource(R.drawable.password_eye_close);
                mClearEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mClearEditText.setSelection(mClearEditText.getText().toString().length());
            } else {
                isShowing = true;
                mShowPassWordIv.setImageResource(R.drawable.password_eye_open);
                mClearEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mClearEditText.setSelection(mClearEditText.getText().toString().length());
            }

        }
    }

    private String sFilter(String str) {
        return str.replaceAll(" ", "").trim();
    }

    public void setHint(String hint) {
        mClearEditText.setHint(hint);
    }

    public void setText(String text) {
        mClearEditText.setText(text);
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        mClearEditText.addTextChangedListener(textWatcher);
    }

    public Editable getText() {
        return mClearEditText.getText();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String s1 = mClearEditText.getText().toString();
        String s2 = sFilter(mClearEditText.getText().toString());
        if (!s1.equals(s2)) {
            ToastUtil.showToast(mContext, "密码中不可以有空格");
            mClearEditText.setText(s2);
            mClearEditText.setSelection(s2.length());
        }


    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void hideEyeImageView(boolean b) {
        if (b) {
            mShowPassWordIv.setVisibility(GONE);
        } else {
            mShowPassWordIv.setVisibility(VISIBLE);
        }
    }

    public EditText getEditView(){
        return mClearEditText;
    }
}
