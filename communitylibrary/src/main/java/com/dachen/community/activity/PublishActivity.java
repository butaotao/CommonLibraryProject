package com.dachen.community.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.dachen.community.R;
import com.dachen.community.views.ChatFaceView;

/**
 * Created by Administrator on 2016/9/8.
 */
public class PublishActivity extends Activity implements View.OnClickListener{

    private ChatFaceView mChatFaceView;
    private boolean showFace;
    private EditText edit_content;
    private EditText edit_title;
    private ImageView btn_face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_layout);

        initView();
    }

    private void initView() {
        edit_title  = (EditText)findViewById(R.id.edit_title);
        edit_content = (EditText)findViewById(R.id.edit_content);
        btn_face = (ImageView) findViewById(R.id.btn_face);
        btn_face.setOnClickListener(this);
        mChatFaceView = (ChatFaceView)findViewById(R.id.chat_face_view);
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
            showFace = !showFace;
            changeChatFaceView(showFace);
        }
    }
}
