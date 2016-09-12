package com.dachen.community.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pqixi on 2016/9/12 0012.
 */
public class GridRadioGroup extends LinearLayout implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup[] mRadioGroups;

    private RadioButton mLastCheckButton;

    private List<String> mSelects;

    private OnCheckChanged mListener;

    /**
     * 列数
     */
    private int lineNumber = 2;

    public GridRadioGroup(Context context) {
        this(context, null);
    }

    public GridRadioGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
        mLastCheckButton = null;
        removeAllViews();
        initView();
    }

    public List<String> getmSelects() {
        return mSelects;
    }

    public OnCheckChanged getmListener() {
        return mListener;
    }

    public void setmListener(OnCheckChanged mListener) {
        this.mListener = mListener;
    }

    public void setmSelects(List<String> mSelects) {
        if (mSelects == null) {
            mSelects = new ArrayList<>();
        }
        this.mSelects = mSelects;
        mLastCheckButton = null;
        for (int i = 0; i < lineNumber; i++) {
            mRadioGroups[i].removeAllViews();
        }
        for (String select : mSelects) {
            if (TextUtils.isEmpty(select))
                continue;
            addView(createRadioButton(select));
        }

    }

    /**
     * @param select
     * @return
     */
    private RadioButton createRadioButton(String select) {
        RadioButton radioButton = new RadioButton(getContext());
        radioButton.setText(select);
        return radioButton;
    }

    /**
     * 添加两个groupbutton
     */
    private void initView() {
        setOrientation(HORIZONTAL);

        mRadioGroups = new RadioGroup[lineNumber];

        RadioGroup radioGroup;
        for (int i = 0; i < lineNumber; i++) {
            radioGroup = createRadioGroup();
            mRadioGroups[i] = radioGroup;
            super.addView(radioGroup);
            radioGroup.setOnCheckedChangeListener(this);
        }

    }

    @Override
    public void addView(View child) {
        if (child instanceof RadioButton) {
            changeCheckedButton((RadioButton) child);
            getTargetGroup().addView(child);
        } else {
            super.addView(child);
        }
    }

    private void changeCheckedButton(RadioButton checked) {
        if (checked != null && checked.isChecked()) {
            if (mLastCheckButton != null) {
                mLastCheckButton.setChecked(false);
            }
            mLastCheckButton = checked;
        }
    }

    private RadioGroup getTargetGroup() {
        int tagetGroup = 0;
        int mLastGroupChildCount = mRadioGroups[0].getChildCount();
        for (int i = 1; i < mRadioGroups.length; i++) {
            if (mRadioGroups[i].getChildCount() < mLastGroupChildCount) {
                tagetGroup = i;
                mLastGroupChildCount = mRadioGroups[i].getChildCount();
            }
        }
        return mRadioGroups[tagetGroup];
    }

    /**
     * 生成RadioGroup对象
     *
     * @return
     */
    private RadioGroup createRadioGroup() {
        RadioGroup radioGroup = new RadioGroup(this.getContext());
        radioGroup.setOrientation(VERTICAL);
        LinearLayout.LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        radioGroup.setLayoutParams(params);
        return radioGroup;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton mButton = (RadioButton) group.findViewById(checkedId);
        changeCheckedButton(mButton);//切换选择项
        if (mListener != null) {
            mListener.onCheckedChanged(mButton, getCheckText());
        }
    }

    public String getCheckText() {
        if (mLastCheckButton != null) {
            return mLastCheckButton.getText().toString();
        }
        return null;
    }


    public interface OnCheckChanged {
        public void onCheckedChanged(RadioButton checkButton, String text);
    }
}
