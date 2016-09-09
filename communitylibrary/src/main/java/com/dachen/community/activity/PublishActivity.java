package com.dachen.community.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.common.utils.CommonUtils;
import com.dachen.common.widget.NoScrollerGridView;
import com.dachen.community.Adapter.GridImgAdapter;
import com.dachen.community.R;
import com.dachen.community.model.PictureModel;
import com.dachen.community.views.ChatFaceView;
import com.dachen.gallery.CustomGalleryActivity;
import com.dachen.gallery.GalleryAction;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * [发帖页面]
 *
 * @author zhouyuandong
 * @date 2016-9-9
 *
 **/
public class PublishActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private static final int REQUEST_PICK = 0;
    private ChatFaceView mChatFaceView;
    private EditText edit_content;
    private EditText edit_title;
    private ImageView btn_face;
    private TextView tv_title;
    private Button btn_right;
    private Button btn_left;
    private ImageView btn_img;
    private String ADDPIC = "add";
    private ArrayList<PictureModel> selectedPicture = new ArrayList<PictureModel>();
    private NoScrollerGridView gridView;
    private GridImgAdapter mAdapter;
    private int mUploadingPictureCount;//正在上传的图片数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_layout);

        initView();
    }

    private void initView() {
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("发帖");
        btn_right = getViewById(R.id.btn_right);
        btn_right.setOnClickListener(this);
        btn_right.setText(getString(R.string.send));
        btn_right.setVisibility(View.VISIBLE);
        btn_left = getViewById(R.id.btn_left);
        btn_left.setOnClickListener(this);
        edit_title  = getViewById(R.id.edit_title);
        edit_content = getViewById(R.id.edit_content);
        btn_face = getViewById(R.id.btn_face);
        btn_face.setOnClickListener(this);
        btn_img = getViewById(R.id.btn_img);
        btn_img.setOnClickListener(this);
        gridView = getViewById(R.id.gridView);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
        mAdapter = new GridImgAdapter(this);
        gridView.setAdapter(mAdapter);
        mChatFaceView = getViewById(R.id.chat_face_view);
        mChatFaceView.setEmotionClickListener(new ChatFaceView.EmotionClickListener() {
            @Override
            public void onNormalFaceClick(SpannableString ss) {
                if (edit_content.hasFocus()) {
                    edit_content.getText().insert(edit_content.getSelectionStart(), ss);
                } else {
                    edit_content.getText().insert(edit_content.getText().toString().length(), ss);
                }
            }

            @Override
            public void onGifFaceClick(String resName) {
                // 发送GIF图片的回调
            }
        });

    }

    /**
     * 显示或隐藏表情布局
     *
     * @param show
     */
    private void changeChatFaceView(boolean show) {
        boolean isShowing = mChatFaceView.getVisibility() != View.GONE;
        if (isShowing == show) {
            return;
        }
        if (show) {
            mChatFaceView.setVisibility(View.VISIBLE);
        } else {
            mChatFaceView.setVisibility(View.GONE);
        }
    }
    public void hideChatFaceView(){
        changeChatFaceView(false);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.btn_face){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_content.getWindowToken(), 0);
            changeChatFaceView(true);
        }else if(view.getId()== R.id.btn_right){

        }else if(view.getId()== R.id.btn_left){
            finish();
        }else if(view.getId()==R.id.btn_img){
            CustomGalleryActivity.openUi(this, true, REQUEST_PICK, 8 - getPicNum());
        }
    }

    private int getPicNum() {
        int num = selectedPicture.size();
        if (selectedPicture.contains(ADDPIC))
            num -= 1;
        return num;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK:
                    String[] all_path = intent.getStringArrayExtra(GalleryAction.INTENT_ALL_PATH);
                    if (all_path == null || all_path.length == 0)
                        return;
                    uploadImages(all_path);
                    break;
                default:
                    break;
            }
        }

    }

    private void uploadImages(String[] paths) {
        if (paths == null || paths.length == 0) {
            return;
        }

        if(selectedPicture.size()>0 && selectedPicture.size()<=8){
            if(selectedPicture.get(selectedPicture.size()-1).getLocalImg().equals("add")){
                selectedPicture.remove(selectedPicture.size()-1);
            }
        }

//        mUploadingPictureCount += paths.length;
        //显示本地图片
        for(int i=0;i<paths.length;i++){
            PictureModel model = new PictureModel();
            model.setLocalImg(paths[i]);
            model.setNetImg("");
            selectedPicture.add(model);
        }

        PictureModel model = new PictureModel();
        model.setLocalImg(ADDPIC);
        model.setNetImg("");
        selectedPicture.add(model);

        if (selectedPicture.size() > 8) {
            selectedPicture.remove(selectedPicture.size()-1);
        }
        mAdapter.removeAll();
        mAdapter.addData(selectedPicture);
        mAdapter.notifyDataSetChanged();

//        for (String path : paths) {
//            uploadImage(path,paths);
//        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(!inRangeOfView(event)){
                mChatFaceView.setVisibility(View.GONE);
                CommonUtils.hideKeyboard(PublishActivity.this);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public boolean inRangeOfView(MotionEvent ev){
        int[] location = new int[2];
        mChatFaceView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if(ev.getX() < x || ev.getX() > (x + mChatFaceView.getWidth()) || ev.getY() < y || ev.getY() > (y + mChatFaceView.getHeight())){
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }
}
