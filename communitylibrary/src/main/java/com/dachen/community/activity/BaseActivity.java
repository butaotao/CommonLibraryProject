package com.dachen.community.activity;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.common.AppManager;
import com.dachen.community.R;


public class BaseActivity extends FragmentActivity implements View.OnClickListener{

    public ProgressDialog mDialog;

    /** 标题 */
    protected TextView titleName,textView,left_text;

    /** 标题栏左边的布局 ,显示正在加载的布局,身驱的布局*/
    protected View title_left_layout, bodyView;

    /** 标题栏的布局*/
    protected RelativeLayout title_layout;

    /** 标题栏右边的布局, 身躯 */
    protected LinearLayout title_right_layout, title_middle_layout;

    /** Framelayout,加载布局上一层的布局,即id = android.R.id.content */
    protected FrameLayout frameLayout;

    /** 加载布局的对象 */
    protected LayoutInflater inflater;

    /** 资源对象 */
    protected Resources resources;
    public final int alert_id = 0x9999999;
    // 屏幕的密度
    private static float scale;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initProgressDialog();
        inflater = getLayoutInflater();
        resources = getResources();
        scale=resources.getDisplayMetrics().density;
        // 背景不设置任何元素
        getWindow().setBackgroundDrawable(null);
        // 没有标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 读取加载布局是的上一层布局控件,既是一个FrameLayout
        frameLayout = (FrameLayout) findViewById(android.R.id.content);
    }

    private void initProgressDialog() {
        mDialog = new ProgressDialog(this, R.style.IMDialog);
        // mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage("正在加载");
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
     * 自定义处理标题头的初始化
     *
     * @param titleViewId
     *            标题栏布局
     * @param bodyViewId
     *            身驱布局
     */
    protected void initAllView(int titleViewId, int bodyViewId) {
        initAllView(inflater.inflate(titleViewId, null), inflater.inflate(bodyViewId, null));
    }

    /**
     * 自定义处理标题头的初始化
     *
     * @param titleView
     * @param bodyView
     */
    protected void initAllView(View titleView, View bodyView) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (int) resources.getDimension(R.dimen.dp_54));
        frameLayout.addView(titleView, layoutParams);
        this.bodyView = bodyView;
        FrameLayout.LayoutParams bodyLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        bodyLayoutParams.setMargins(0, (int) resources.getDimension(R.dimen.dp_54), 0, 0);
        frameLayout.addView(bodyView, bodyLayoutParams);
    }

    protected void initView(int titleStringId, int bodyViewId, int... rightResId) {
        initView(getString(titleStringId), inflater.inflate(bodyViewId, null), rightResId);
    }

    protected void initView(int titleStringId, View bodyView, int... rightResId) {
        initView(getString(titleStringId), bodyView, rightResId);
    }

    protected void initView(String title, int bodyViewId, int... rightResId) {
        initView(title, inflater.inflate(bodyViewId, null), rightResId);
    }

    /**
     *
     * @Title: initView
     * @Description: TODO(初始化标题栏,并添加标题栏下面的布局)
     * @param: title 标题文字
     * @param: bodyView 加入标题栏下面的布局
     * @param: rightResId 标题栏右边的操作集合,可以有多个或者一个,可以传文字也可以传图片
     */
    protected void initView(String title, View bodyView, int... rightResId) {
        inflater.inflate(R.layout.title_base_activity, frameLayout, true);
        title_layout = (RelativeLayout) findViewById(R.id.title_layout);
        registerDoubleClickListener(title_layout);
        this.bodyView = bodyView;
        title_left_layout = findViewById(R.id.title_left_layout);
        // 左边按钮的事件
        title_left_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                leftClick();
            }
        });
        left_text=(TextView)findViewById(R.id.left_text);
        // 标题
        titleName = (TextView) findViewById(R.id.title_text);
        titleName.setText(TextUtils.isEmpty(title)?"":title);
        title_right_layout = (LinearLayout) findViewById(R.id.title_right_layout);
        // 中间布局
        title_middle_layout = (LinearLayout) findViewById(R.id.title_middle_layout);
        // 右边的布局
        resetTitleRightMenu(rightResId);
        // 身躯布局
        if (bodyView != null) {
            // 添加身躯
            FrameLayout.LayoutParams bodyLayoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            bodyLayoutParams.setMargins(0, (int) resources.getDimension(R.dimen.dp_54), 0, 0);
            frameLayout.addView(bodyView, bodyLayoutParams);
        }
    }

    /**
     *
     * @Title: setTitleBackgroundColor
     * @Description: TODO(设置标题栏背景颜色)
     * @param: @param colorResId
     */
    public void setTitleBackgroundColor(int colorResId) {
        title_layout.setBackgroundColor(resources.getColor(colorResId));
    }

    /**
     * 隐藏左边的箭头
     */
    public void setBackBtnDisable(){
        Button leftArrow = (Button) title_left_layout.findViewById(R.id.title_back_image);
        leftArrow.setVisibility(View.GONE);
    }

    /**
     * 隐藏左边文字
     */
    public void setLeftTextDisable(){
        left_text.setVisibility(View.GONE);
    }

    /**
     * 设置左边文字
     * @param leftText
     */
    public void setLeftText(String leftText){
        setBackBtnDisable();
        left_text.setText(leftText);
    }

    /**
     * 左边lyaout隐藏
     */
    public void setLeftLayoutDisable(){
        title_left_layout.setVisibility(View.GONE);
    }


    /**
     *
     * @Title: setTitleVisiable
     * @Description: TODO(设置标题栏是否可见)
     * @param: @param isVisiable
     */
    public void setTitleVisiable(boolean isVisiable) {
        if(isVisiable){
            title_layout.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams bodyLayoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            bodyLayoutParams.setMargins(0, (int) resources.getDimension(R.dimen.dp_54), 0, 0);
            bodyView.setLayoutParams(bodyLayoutParams);
        }else{
            title_layout.setVisibility(View.GONE);
            FrameLayout.LayoutParams bodyLayoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            bodyLayoutParams.setMargins(0, 0, 0, 0);
            bodyView.setLayoutParams(bodyLayoutParams);
        }
    }


    /**
     *
     * @Title: resetTitleRightMenu
     * @Description: TODO(重设标题栏右边的菜单,包含影藏操作)
     * @param:
     */
    public void resetTitleRightMenu(int... resID) {
        int dp_5 = (int) resources.getDimension(R.dimen.dp_5);
        int dp_8 = (int) resources.getDimension(R.dimen.dp_8);
        int size = resID==null?0:resID.length;
        boolean isAllStr = true;
        boolean isAllDra = true;
        for(int i=0; i<size; i++) {
            //判断它是不是图片资源
            if(resID[i]>=0x7f020000&&resID[i]<=0x7f02ffff){
                isAllStr = false;
            } else {
                isAllDra = false;
            }
        }
        if(isAllStr) {
            String[] res = new String[size];
            //全是文字
            for(int i=0; i<size; i++) {
                res[i] = getString(resID[i]);
            }
            resetTitleRightMenu(res);
        } else if(isAllDra) {
            //全是图片
            if (title_right_layout != null) {
                title_right_layout.removeAllViews();
            }
            title_right_layout.setVisibility(View.VISIBLE);
            int dp_10 = (int) resources.getDimension(R.dimen.dp_10);
            for (int i = 0; i < size; i++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout relativeLayout = new RelativeLayout(this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                relativeLayout.setLayoutParams(params);
                relativeLayout.setTag(i);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                ImageView imageView = new ImageView(this);
                imageView.setId(R.id.title_base_img);
                imageView.setPadding(dp_8, dp_10, dp_8, dp_10);
                imageView.setImageResource(resID[i]);
                imageView.setBackgroundResource(R.drawable.selector_btn_back);
                relativeLayout.addView(imageView);
                RelativeLayout.LayoutParams params_child = new RelativeLayout.LayoutParams(dip2px(15),
                        dip2px(15));
                params_child.addRule(RelativeLayout.ALIGN_RIGHT, imageView.getId());
                params_child.setMargins(0, dip2px(2), dip2px(2), 0);
                TextView textView = new TextView(this);
                textView.setLayoutParams(params_child);
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundResource(R.drawable.red_dot);
                textView.setVisibility(View.GONE);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                relativeLayout.addView(textView);
                relativeLayout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        rightClick((Integer) v.getTag());
                    }
                });
                title_right_layout.addView(relativeLayout, layoutParams);
            }
        } else {
            //又有文字又有图片
            if (title_right_layout != null) {
                title_right_layout.removeAllViews();
            }
            title_right_layout.setVisibility(View.VISIBLE);
            int dp_10 = (int) resources.getDimension(R.dimen.dp_10);
            for (int i = 0; i < size; i++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                if(resID[i]>=0x7f020000&&resID[i]<=0x7f02ffff) {
                    RelativeLayout relativeLayout = new RelativeLayout(this);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    relativeLayout.setLayoutParams(params);
                    relativeLayout.setTag(i);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    ImageView imageView = new ImageView(this);
                    imageView.setId(R.id.title_base_img);
                    imageView.setPadding(dp_8, dp_10, dp_10, dp_10);
                    imageView.setImageResource(resID[i]);
                    imageView.setBackgroundResource(R.drawable.selector_btn_back);
                    relativeLayout.addView(imageView);
                    RelativeLayout.LayoutParams params_child = new RelativeLayout.LayoutParams(dip2px(15),
                            dip2px(15));
                    params_child.addRule(RelativeLayout.ALIGN_RIGHT, imageView.getId());
                    params_child.setMargins(0, dip2px(2), dip2px(2), 0);
                    TextView textView = new TextView(this);
                    textView.setLayoutParams(params_child);
                    textView.setGravity(Gravity.CENTER);
                    textView.setBackgroundResource(R.drawable.red_dot);
                    textView.setVisibility(View.GONE);
                    textView.setTextColor(getResources().getColor(R.color.white));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                    relativeLayout.addView(textView);
                    relativeLayout.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            rightClick((Integer) v.getTag());
                        }
                    });
                    title_right_layout.addView(relativeLayout, layoutParams);
                } else {
                    textView = new TextView(this);
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(dp_10, 0, dp_10, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.sp_14));
                    textView.setText(resID[i]);
                    textView.setTextColor(resources.getColor(R.color.white));
                    textView.setBackgroundResource(R.drawable.selector_btn_back);
                    textView.setTag(i);
                    textView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            rightClick((Integer) v.getTag());
                        }
                    });
                    title_right_layout.addView(textView, layoutParams);
                }
            }
        }
    }
    /**
     * 设置右菜单栏的文字是否可以点击
     * @param clickable 是否可以点击
     */
    public void setRightTextviewClickable(boolean clickable){
        if(null!=textView){
            textView.setBackgroundResource(R.color.head_bg_color);
            textView.setClickable(clickable);
        }
    }

    /**
     * 设置菜单栏的消息提醒处理
     * @param index 索引位置
     * @param isShow 是否显示
     */
    public void setRightMenuAlert(int index, boolean isShow){
        if(title_right_layout!=null&&title_right_layout.getChildCount()>0) {
            int dp_8 = (int)resources.getDimension(R.dimen.dp_8);
            View view = title_right_layout.findViewById(alert_id);
            if(view==null){
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dp_8, dp_8);
                layoutParams.gravity = Gravity.TOP;
                layoutParams.topMargin = (int)resources.getDimension(R.dimen.dp_10);
                layoutParams.leftMargin = (int)resources.getDimension(R.dimen._dp_10);
                view = new View(this);
                view.setId(alert_id);
                view.setBackgroundResource(R.drawable.red_dot);
                title_right_layout.addView(view, index + 1, layoutParams);
            }
            view.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    public void resetTitleRightMenu(String[] res) {
        int size = res==null?0:res.length;
        if (title_right_layout != null) {
            title_right_layout.removeAllViews();
        }
        if (size > 0) {
            title_right_layout.setVisibility(View.VISIBLE);
            int dp_10 = (int) resources.getDimension(R.dimen.dp_10);
            for (int i = 0; i < size; i++) {
                TextView textView = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.sp_14));
                textView.setText(res[i]);
                textView.setTextColor(resources.getColor(R.color.white));
                textView.setBackgroundResource(R.drawable.selector_btn_back);
                textView.setPadding(dp_10, 0, dp_10, 0);
                title_right_layout.addView(textView, layoutParams);
                textView.setTag(i);
                textView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        rightClick((Integer) v.getTag());
                    }
                });
            }
        }
    }

    /**
     *
     * @Title: resetTitleRightMenu
     * @Description: TODO(重设标题栏右边的菜单,包含隐藏操作，设置按钮背景，设置按钮字体颜色)
     * @param:
     */
    public void resetTitleRightMenu(int backSrc, int textColor, String titleName) {
        if (title_right_layout != null) {
            title_right_layout.removeAllViews();
        }
        title_right_layout.setVisibility(View.VISIBLE);
        int dp_10 = (int) resources.getDimension(R.dimen.dp_10);
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.sp_14));
        if(textColor!=-1){
            textView.setTextColor(resources.getColor(textColor));
        }
        if(backSrc==-1){
            textView.setBackgroundResource(R.drawable.selector_btn_back);
        }else{
            textView.setBackgroundResource(backSrc);
        }
        textView.setPadding(dp_10, 0, dp_10, 0);
        if(titleName!=null){
            textView.setText(titleName);
        }
        title_right_layout.setGravity(Gravity.CENTER_VERTICAL);
        title_right_layout.addView(textView, layoutParams);
        textView.setTag(0);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rightClick((Integer) v.getTag());
            }
        });
    }

    /**
     * 注册一个双击事件
     * 增加  Handler  处理，如果不加这个，会引起线程安全之类错误。
     */
    private void registerDoubleClickListener(View view){
        view.setOnClickListener(new View.OnClickListener() {
            private static final int DOUBLE_CLICK_TIME = 350;        //双击间隔时间350毫秒
            private boolean waitDouble = true;

            private Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    OnTitleBarSingleClick((View) msg.obj);
                }
            };

            //等待双击
            public void onClick(final View v) {
                if(waitDouble){
                    waitDouble = false;        //与执行双击事件
                    new Thread() {

                        public void run() {
                            try {
                                Thread.sleep(DOUBLE_CLICK_TIME);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }    //等待双击时间，否则执行单击事件
                            if(!waitDouble){
                                //如果过了等待事件还是预执行双击状态，则视为单击
                                waitDouble = true;
                                Message msg = handler.obtainMessage();
                                msg.obj = v;
                                handler.sendMessage(msg);
                            }
                        }

                    }.start();
                }else{
                    waitDouble = true;
                    OnTitleBarDoubleClick(v);    //执行双击
                }
            }
        });
    }

    /**
     * 双击事件
     * @param v
     */
    protected void OnTitleBarDoubleClick(View v) {}

    /**
     * 单击事件
     * @param v
     */
    protected void OnTitleBarSingleClick(View v) {}

    /**
     * 添间整个布局
     * @param viewId
     */
    protected void initView(int viewId) {
        initView(inflater.inflate(viewId, null));
    }

    /**
     *
     * @Title: initView
     * @Description: TODO(不需要标题栏的布局)
     * @param: bodyView
     */
    protected void initView(View bodyView) {
        // 添加身躯
        FrameLayout.LayoutParams bodyLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        frameLayout.addView(bodyView, bodyLayoutParams);
        frameLayout.setBackgroundColor(resources.getColor(android.R.color.white));
    }

    /**
     *
     * @Title: initView
     * @Description: TODO(不需要标题栏的布局)
     * @param: bodyView 布局的最外层的标检是merge
     */
    protected void initMergetView(int layoutId) {
        inflater.inflate(layoutId, frameLayout, true);
        frameLayout.setBackgroundColor(resources.getColor(android.R.color.white));
    }



    /**
     * 标题栏左边点击事件,默认是关闭当前的Activity
     */
    protected void leftClick() {
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     *
     * @Title: rightClick
     * @Description: TODO(标题栏右边菜单的点击事件)
     * @param: index(索引,因为右边的菜单可能是一个或则多个所以传一个索引过来)
     */
    protected void rightClick(int index) {}



    @Override
    public void onClick(View v) {}




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.getAppManager().finishActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        return (int) (dpValue * scale + 0.5f);
    }


}
