package com.dachen.imsdk.vchat.control;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.entity.GroupInfo2Bean.Data.UserInfo;
import com.dachen.imsdk.utils.ImUtils;
import com.dachen.imsdk.vchat.ImVideo;
import com.dachen.imsdk.vchat.MemberInfo;
import com.dachen.imsdk.vchat.R;
import com.dachen.imsdk.vchat.VChatManager;
import com.dachen.imsdk.vchat.VChatManager.VChatLayoutEvent;
import com.dachen.imsdk.vchat.VChatManager.VChatUserChangeEvent;
import com.dachen.imsdk.vchat.VChatManager.VChatUserEmptyEvent;
import com.dachen.imsdk.vchat.VChatManager.VChatUserRemoveEvent;
import com.dachen.imsdk.vchat.VChatUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.av.opengl.GraphicRendererMgr;
import com.tencent.av.opengl.gesturedetectors.MoveGestureDetector;
import com.tencent.av.opengl.gesturedetectors.MoveGestureDetector.OnMoveGestureListener;
import com.tencent.av.opengl.ui.GLRootView;
import com.tencent.av.opengl.ui.GLView;
import com.tencent.av.opengl.ui.GLViewGroup;
import com.tencent.av.opengl.utils.Utils;
import com.tencent.av.sdk.AVEndpoint;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.av.sdk.AVView;
import com.tencent.av.utils.QLog;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot1.event.EventBus;

;

public class AVUIControl extends GLViewGroup {
	static final String TAG = "VideoLayerUI";

	boolean mIsLocalHasVideo = false;// 自己是否有视频画面

	Context mContext = null;
	GraphicRendererMgr mGraphicRenderMgr = null;

	View mRootView = null;
	int mTopOffset = 0;
	int mBottomOffset = 0;

	GLRootView mGlRootView = null;
	RelativeLayout mPreviewContainer = null;
	GLVideoView mGlVideoView[] = null;

	int mClickTimes = 0;
	int mTargetIndex = -1;
	OnTouchListener mTouchListener = null;
	GestureDetector mGestureDetector = null;
	MoveGestureDetector mMoveDetector = null;
	ScaleGestureDetector mScaleGestureDetector = null;
	private QavsdkControl mQavsdkControl;
	
	private int localViewIndex = -1;
	private int remoteViewIndex = -1;
	private String mRemoteIdentifier = "";
    private String mLocalIdentifier = "";
    public String enlargeUserId;

	private boolean isSupportMultiVideo = false;

	private SurfaceView mSurfaceView = null;
	private SurfaceHolder.Callback mSurfaceHolderListener = new SurfaceHolder.Callback() {
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			mContext.sendBroadcast(new Intent(VChatUtil.ACTION_SURFACE_CREATED));
			mCameraSurfaceCreated = true;

			QavsdkControl qavsdk = ImVideo.getInstance().mQavsdkControl;
			qavsdk.getAVContext().setRenderMgrAndHolder(mGraphicRenderMgr, holder);
			Log.e("memoryLeak", "memoryLeak surfaceCreated");
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			if (holder.getSurface() == null) {
				return;
			}
			holder.setFixedSize(width, height);
			Log.e("memoryLeak", "memoryLeak surfaceChanged");
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.e("memoryLeak", "memoryLeak surfaceDestroyed");
		}
	};
    private Handler mHandler;

	public AVUIControl(Context context, View rootView) {
		mContext = context;
		mQavsdkControl = ImVideo.getInstance().mQavsdkControl;
		mRootView = rootView;
		mGraphicRenderMgr = new GraphicRendererMgr();
//		mManager=VChatManager.getInstance();
        mHandler=new Handler(context.getMainLooper());
		initQQGlView();
		initCameraPreview();
		initVideoParam();
        EventBus.getDefault().register(this);
	}
	
	private void initVideoParam() {
		QavsdkControl qavsdkControl = ImVideo.getInstance().mQavsdkControl;
		if (null != qavsdkControl && qavsdkControl.getIsSupportMultiView()) {
			isSupportMultiVideo = true;
		}
		
		if (QLog.isColorLevel()) {
			QLog.d(TAG, QLog.CLR, "isSupportMultiVideo: " + isSupportMultiVideo);
		}
	}

    private Runnable layoutRunnable=new Runnable() {
        @Override
        public void run() {
            layoutVideoView(false);
        }
    };

	@Override
	protected void onLayout(boolean flag, int left, int top, int right, int bottom) {
		if (QLog.isColorLevel()) {
			QLog.d(TAG, QLog.CLR, "onLayout|left: " + left + ", top: " + top + ", right: " + right + ", bottom: " + bottom);
		}
        mHandler.post(layoutRunnable);

	}

	public void showGlView() {
		if (mGlRootView != null) {
			mGlRootView.setVisibility(View.VISIBLE);
		}
	}

	public void hideGlView() {
		if (mGlRootView != null) {
			mGlRootView.setVisibility(View.GONE);
		}
	}

	public void onResume() {
		if (mGlRootView != null) {
			mGlRootView.onResume();
		}

		setRotation(mCacheRotation);
	}

	public void onPause() {
		if (mGlRootView != null) {
			mGlRootView.onPause();
		}
	}

	public void onDestroy() {
		Log.e("memoryLeak", "memoryLeak AVUIControl onDestroy");
        EventBus.getDefault().unregister(this);
		unInitCameraaPreview();
		mContext = null;
		mRootView = null;

		removeAllView();
		for (int i = 0; i < mGlVideoView.length; i++) {
			mGlVideoView[i].flush();
			mGlVideoView[i].clearRender();
			mGlVideoView[i] = null;
		}
		mGlRootView.setOnTouchListener(null);
		mGlRootView.setContentPane(null);

		mTouchListener = null;
		mGestureDetector = null;
		mMoveDetector = null;
		mScaleGestureDetector = null;

		mGraphicRenderMgr = null;

		mGlRootView = null;
		mGlVideoView = null;
	}

	public void enableDefaultRender() {
		QavsdkControl qavsdk = ImVideo.getInstance().mQavsdkControl;
		qavsdk.getAVContext().setRenderFunctionPtr(mGraphicRenderMgr.getRecvDecoderFrameFunctionptr()); 
	}
	
	public boolean setLocalHasVideo(boolean isLocalHasVideo, boolean forceToBigView, String identifier) {
		if (mContext == null)
			return false;

		if (Utils.getGLVersion(mContext) == 1) {
			return false;
		}
		
				
		if (isLocalHasVideo) {// 打开摄像头
            mLocalIdentifier=identifier;
			GLVideoView view = null;
			int index = getViewIndexById(identifier, AVView.VIDEO_SRC_TYPE_CAMERA);		
			if (index < 0) {
				index = getIdleViewIndex(0);					
				if (index >= 0) {
					view = mGlVideoView[index];
					view.setRender(identifier, AVView.VIDEO_SRC_TYPE_CAMERA);
					localViewIndex = index;
                    boolean front=mQavsdkControl.getAVVideoControl().getIsFrontCamera();
                    closePreview(identifier);
                    if(front)
                        view.setMirror(true);
//                    view.setText("点击切换镜头",mContext.getResources().getDimension(R.dimen.me_camera_tip_text_size),0x88fffFFF);
                }
			} else {
				view = mGlVideoView[index];
			}
			if (view != null) {
				view.setIsPC(false);
				view.enableLoading(false);
				// if (isFrontCamera()) {
				// view.setMirror(true);
				// } else {
				// view.setMirror(false);
				// }
				view.setVisibility(GLView.VISIBLE);
			}
			if (forceToBigView && index > 0) {
				switchVideo(0, index);				
			}
		} else if (!isLocalHasVideo) {// 关闭摄像头
			int index = getViewIndexById(identifier, AVView.VIDEO_SRC_TYPE_CAMERA);			
			if (index >= 0) {
				closeVideoView(index);
				localViewIndex = -1;
			}
		}
		mIsLocalHasVideo = isLocalHasVideo;
        layoutVideoView(false);
		return true;
	}

	public void setRemoteHasVideo(String identifier, int videoSrcType, boolean isRemoteHasVideo, boolean forceToBigView, boolean isPC) {
		boolean needForceBig = forceToBigView;
		if (mContext == null)
			return;
		if (Utils.getGLVersion(mContext) == 1) {
			isRemoteHasVideo = false;
			return;
		}
		if (!forceToBigView && !isLocalFront()) {
			forceToBigView = true;
		}
				
		if (isRemoteHasVideo) {// 打开对方画面
            closePreview(identifier);
			GLVideoView view = null;
			int index = getViewIndexById(identifier, videoSrcType);			
			if (index < 0) {
				index = getIdleViewIndex(0);			
				if (index >= 0) {
					view = mGlVideoView[index];
					view.setRender(identifier, videoSrcType);
					remoteViewIndex = index;
					mRemoteIdentifier = identifier;
				}
			} else {
				view = mGlVideoView[index];
			}
			if (view != null) {
				view.setIsPC(isPC);
				view.setMirror(false);
				view.enableLoading(true);
				view.setVisibility(GLView.VISIBLE);
			}
			if (forceToBigView && index > 0) {
				switchVideo(0, index);
			}
		} else {// 关闭对方画面
			int index = getViewIndexById(identifier, videoSrcType);		
			if (index >= 0) {
				closeVideoView(index);
				remoteViewIndex = -1;
			}
		}
	}

	int mRotation = 0;
	int mCacheRotation = 0;

	public void setRotation(int rotation) {		
		if (mContext == null) {
			return;
		}

		if ((rotation % 90) != (mRotation % 90)) {
			mClickTimes = 0;
		}

		mRotation = rotation;
		mCacheRotation = rotation;
		
		// layoutVideoView(true);
		QavsdkControl qavsdk =ImVideo.getInstance().mQavsdkControl;
		if ((qavsdk != null) && (qavsdk.getAVVideoControl() != null)) {		
			qavsdk.getAVVideoControl().setRotation(rotation);			
		}
		switch (rotation) {
		case 0:
			for (int i = 0; i < getChildCount(); i++) {
				GLView view = getChild(i);
				if(view != null)
					view.setRotation(0);
			}
			break;
		case 90:
			for (int i = 0; i < getChildCount(); i++) {
				GLView view = getChild(i);
				if(view != null)				
					view.setRotation(90);
			}
			break;
		case 180:
			for (int i = 0; i < getChildCount(); i++) {
				GLView view = getChild(i);
				if(view != null)				
					view.setRotation(180);
			}
			break;
		case 270:
			for (int i = 0; i < getChildCount(); i++) {
				GLView view = getChild(i);
				if(view != null)				
					view.setRotation(270);
			}
			break;
		default:
			break;
		}
	}
	public String getQualityTips() {
		QavsdkControl qavsdk = ImVideo.getInstance().mQavsdkControl;
		String tipsAudio = "";
		String tipsVideo = "";
		String tipsRoom = "";
		
		if (qavsdk != null) {
			if (qavsdk.getAVAudioControl() != null) {
				tipsAudio = qavsdk.getAVAudioControl().getQualityTips();
			}
			if (qavsdk.getAVVideoControl() != null) {
				tipsVideo = qavsdk.getAVVideoControl().getQualityTips();
			}
			
			if (qavsdk.getRoom() != null) {
				tipsRoom = qavsdk.getRoom().getQualityTips();
			}
		}
				
		String tipsAll = "";
		
		if(tipsRoom != null && tipsRoom.length() > 0)
		{
			tipsAll += tipsRoom + "\n";
		}
		
		if(tipsAudio != null && tipsAudio.length() > 0)
		{
			tipsAll += tipsAudio + "\n";
		}
		
		if(tipsVideo != null && tipsVideo.length() > 0)
		{
			tipsAll += tipsVideo;
		}
		
		return tipsAll;		
	}
	public void setOffset(int topOffset, int bottomOffset) {
		if (QLog.isColorLevel()) {
			QLog.d(TAG, QLog.CLR, "setOffset topOffset: " + topOffset + ", bottomOffset: " + bottomOffset);
		}
		mTopOffset = topOffset;
		mBottomOffset = bottomOffset;
		// refreshUI();
		layoutVideoView(true);
	}

	public void setText(String identifier, int videoSrcType, String text, float textSize, int color) {
		int index = getViewIndexById(identifier, videoSrcType);
		if (index < 0) {
			index = getIdleViewIndex(0);
			if (index >= 0) {
				GLVideoView view = mGlVideoView[index];
				view.setRender(identifier, videoSrcType);
			}
		}
		if (index >= 0) {
			GLVideoView view = mGlVideoView[index];
			view.setVisibility(GLView.VISIBLE);
			view.setText(text, textSize, color);
		}
		if (QLog.isColorLevel()) {
			QLog.d(TAG, QLog.CLR, "setText identifier: " + identifier + ", videoSrcType: " + videoSrcType + ", text: " + text + ", textSize: " + textSize + ", color: " + color + ", index: " + index);
		}
	}

	public void setBackground(String identifier, int videoSrcType, Bitmap bitmap, boolean needRenderVideo) {
		int index = getViewIndexById(identifier, videoSrcType);
		if (index < 0) {
			index = getIdleViewIndex(0);
			if (index >= 0) {
				GLVideoView view = mGlVideoView[index];
				view.setVisibility(GLView.VISIBLE);
				view.setRender(identifier, videoSrcType);
			}
		}
		if (index >= 0) {
			GLVideoView view = mGlVideoView[index];
			view.setBackground(bitmap);
			view.setNeedRenderVideo(needRenderVideo);
			if (!needRenderVideo) {
				view.enableLoading(false);
			}
		}
		if (QLog.isColorLevel()) {
			QLog.d(TAG, QLog.CLR, "setBackground identifier: " + identifier + ", videoSrcType: " + videoSrcType + ", index: " + index + ", needRenderVideo: " + needRenderVideo);
		}
	}
	
	boolean isLocalFront() {
		boolean isLocalFront = true;
		String selfIdentifier = ImUtils.getLoginUserId();
		GLVideoView view = mGlVideoView[0];
		if (view.getVisibility() == GLView.VISIBLE && selfIdentifier.equals(view.getIdentifier())) {
			isLocalFront = false;
		}
		return isLocalFront;
	}
			
	int getViewCount() {
		int count = 0;
		for (int i = 0; i < mGlVideoView.length; i++) {
			GLVideoView view = mGlVideoView[i];
			if (view.getVisibility() == GLView.VISIBLE && null != view.getIdentifier()) {
				count++;
			}
		}
		return count;
	}

	int getIdleViewIndex(int start) {
		int index = -1;
		for (int i = start; i < mGlVideoView.length; i++) {
			GLVideoView view = mGlVideoView[i];
			if (null == view.getIdentifier() || view.getVisibility() == GLView.INVISIBLE) {
				index = i;
				break;
			}
		}
		return index;
	}

	int getViewIndexById(String identifier, int videoSrcType) {
		int index = -1;
		if (null == identifier) {
			return index;
		}
		for (int i = 0; i < mGlVideoView.length; i++) {
			GLVideoView view = mGlVideoView[i];
			if ((identifier.equals(view.getIdentifier()) && view.getVideoSrcType() == videoSrcType) && view.getVisibility() == GLView.VISIBLE) {
				index = i;
				break;
			}
		}
		return index;
	}
	int getViewIndexById(String identifier) {
		int index = -1;
		if (null == identifier) {
			return index;
		}
		for (int i = 0; i < mGlVideoView.length; i++) {
			GLVideoView view = mGlVideoView[i];
			if ( ( identifier.equals(view.getIdentifier()) ) && view.getVisibility() == GLView.VISIBLE) {
				index = i;
				break;
			}
		}
		return index;
	}

	void layoutVideoView(boolean virtical) {
//		if (QLog.isColorLevel()) {
//			QLog.d(TAG, QLog.CLR, "layoutVideoView virtical: " + virtical);
//		}
		if (mContext == null)
			return;
        int width = getWidth();
        int height = getHeight();
        List<UserInfo> list=getUsers();
        if(enlargeUserId!=null){
            int enlargeIdx=getViewIndexById(enlargeUserId);
            if(enlargeIdx>=0){
                for (int i = 0; i < list.size(); i++) {
                    int idx=getViewIndexById(list.get(i).id);
                    if(enlargeUserId.equals(list.get(i).id) ){
                        if(idx>=0){
                            mGlVideoView[idx].layout(0,0,width,height);
                        }else{
                            View v=addPreview(list.get(i) );
//                    v.layout(x * size, size * y, x * size + size, size * y + size);
                            layoutPreview(v,0,0,width,height,list.get(i));
                        }
                    }else{
                        if(idx>=0){
                            mGlVideoView[idx].layout(-100,0,0,100);
                        }else{
                            View v=addPreview(list.get(i) );
//                    v.layout(x * size, size * y, x * size + size, size * y + size);
                            layoutPreview(v,-100,0,0,100,list.get(i));
                        }
                    }

                }
				EventBus.getDefault().post(new VChatLayoutEvent());
                return;
            }
        }
        enlargeUserId=null;
        EventBus.getDefault().post(new VChatLayoutEvent());

		Log.d(TAG, "width: " + getWidth() + "height: " + getHeight());
        int size=width/3;
		if (QLog.isColorLevel()) {
			QLog.d(TAG, QLog.CLR, "SupportMultiVideo");
		}


		if(list.size()==0)return;
		if(list.size()<=2){
			for (int i = 0; i < list.size(); i++) {
				size=width/2;
				int x = i % 2, y = i / 2;
				int idx=getViewIndexById(list.get(i).id);
				int xExtra=0,yExtra=size/2;
				if(idx>=0){
					mGlVideoView[idx].layout(x * size +xExtra, size * y+yExtra, x * size + size +xExtra, size * y + size+yExtra);
				}else{
					View v=addPreview(list.get(i) );
//                    v.layout(x * size, size * y, x * size + size, size * y + size);
					layoutPreview(v,x * size +xExtra, size * y+yExtra, x * size + size +xExtra, size * y + size+yExtra,list.get(i));
				}
			}
//			旧版大小画面逻辑
//            int idx0=getViewIndexById(list.get(0).id);
//            if(idx0>=0){
//                mGlVideoView[idx0].layout(0,0,width,height);
//                mGlVideoView[idx0].setZOrder(0);
//            }
//			if(list.size()==2){
//                int idx1=getViewIndexById(list.get(1).id);
//                if(idx1>=0){
//                    mGlVideoView[idx1].layout(size*2,size/2,width,size/2+size);
//                    mGlVideoView[idx1].setZOrder(1);
//                }else{
//                    View v=addPreview(list.get(1) );
//                    layoutPreview(v,size*2,size/2,width,size/2+size,list.get(1));
//                }
//			}
		} else if (list.size() <= 4) {
			for (int i = 0; i < list.size(); i++) {
				size=width/2;
				int x = i % 2, y = i / 2;
                int idx=getViewIndexById(list.get(i).id);
                int xExtra=0;
                if(i==2&&list.size()==3){
                    xExtra=size/2;
                }
                if(idx>=0){
                    mGlVideoView[idx].layout(x * size +xExtra, size * y, x * size + size +xExtra, size * y + size);
                }else{
                    View v=addPreview(list.get(i) );
//                    v.layout(x * size, size * y, x * size + size, size * y + size);
                    layoutPreview(v,x * size +xExtra, size * y, x * size + size +xExtra, size * y + size,list.get(i));
                }
			}
		}else {
			for(int i=0;i<list.size();i++){
                int idx=getViewIndexById(list.get(i).id);
                int x=i%3,y=i/3;
                if(idx>=0){
                    mGlVideoView[idx].layout(x * size, size * y, x * size + size, size * y + size);
                }else{
                    View v=addPreview(list.get(i) );
//                    v.layout(x * size, size * y, x * size + size, size * y + size);
                    layoutPreview(v,x * size, size * y, x * size + size, size * y + size,list.get(i));
                }
			}
		}
		invalidate();
//        mPreviewContainer.post(new Runnable() {
//            @Override
//            public void run() {
//                mPreviewContainer.invalidate();
//            }
//        });
        mPreviewContainer.requestLayout();
        mPreviewContainer.postInvalidate();
	}
	private List<UserInfo> getUsers(){
		ArrayList<UserInfo> extraList=new ArrayList<>();
		VChatManager man=VChatManager.getInstance();
		for(GLVideoView view:mGlVideoView){
			if(view.getIdentifier()==null||view.getIdentifier().equals(ImUtils.getLoginUserId()))
				continue;
			int idx=man.getUserIndex(view.getIdentifier());
			if(idx>=0)continue;
			UserInfo u=new UserInfo(view.getIdentifier());
			extraList.add(u);
			view.layout(-100,0,0,100);
		}
		ArrayList<UserInfo> result=new ArrayList<>();
		for(UserInfo u:man.allUserList){
			if(ImUtils.getLoginUserId().equals(u.id))continue;
			result.add(u);
		}
//		result.addAll(man.allUserList);
		result.addAll(extraList);
//		if(result.size()==1){
//            UserInfo u=result.get(0);
//            if(getViewIndexById(u.id)<0){
//                result.add(0,new UserInfo(ImUtils.getLoginUserId()));
//            }else{
//                result.add(new UserInfo(ImUtils.getLoginUserId()));
//            }
//		}else {
//            result.add(new UserInfo(ImUtils.getLoginUserId()));
//		}
		if(result.size()==0)
			EventBus.getDefault().post(new VChatUserEmptyEvent());
		result.add(new UserInfo(ImUtils.getLoginUserId()));
		return result;
	}
    private void layoutPreview(View v,int l,int t,int r,int b,UserInfo u){
        LayoutParams params= (LayoutParams) v.getLayoutParams();
        params.width=r-l;
        params.height=b-t;
        params.leftMargin=l;
        params.topMargin=t;
		AVRoomMulti avRoomMulti = ((AVRoomMulti) mQavsdkControl.getRoom());
		AVEndpoint endpoint=avRoomMulti.getEndpointById(u.id);
        // TODO: 2016/5/31
        if(endpoint==null){
            v.findViewById(R.id.anim).setVisibility(View.VISIBLE);
            v.findViewById(R.id.voice).setVisibility(View.INVISIBLE);
		}else{
            v.findViewById(R.id.anim).setVisibility(View.INVISIBLE);
            v.findViewById(R.id.voice).setVisibility(View.VISIBLE);
		}
        v.setLayoutParams(params);
    }
    private void reArrangeView(){
        GLVideoView[] temp=new GLVideoView[mGlVideoView.length];
        int frontIndex=0,backIndex=mGlVideoView.length-1,meIndex=-1;
        for(int i=0;i<mGlVideoView.length;i++){
            GLVideoView v=mGlVideoView[i];
            if(v.getIdentifier()==null||v.getVisibility()==INVISIBLE){
                temp[backIndex]=v;
                backIndex--;
            }else if(v.getIdentifier().equals(mLocalIdentifier)){
                meIndex=i;
            }else{
                temp[frontIndex]=v;
                frontIndex++;
            }
        }
        if(meIndex>=0){
            temp[frontIndex]=mGlVideoView[meIndex];
			localViewIndex=frontIndex;
        }else{
            localViewIndex=meIndex;
        }
        mGlVideoView=temp;
    }

	void closeVideoView(int index) {
		if (QLog.isColorLevel()) {
			QLog.d(TAG, QLog.CLR, "closeVideoView index: " + index);
		}
		GLVideoView view = mGlVideoView[index];
        if(view.getIdentifier()==null||view.getVisibility()==INVISIBLE)
            return;

        view.setVisibility(GLView.INVISIBLE);
		view.setNeedRenderVideo(true);
		view.enableLoading(false);
		view.setIsPC(false);
		view.clearRender();

//		for (int i = 0; i < mGlVideoView.length - 1; i++) {
//			GLVideoView view1 = mGlVideoView[i];
//			for (int j = i + 1; j < mGlVideoView.length; j++) {
//				GLVideoView view2 = mGlVideoView[j];
//				if (view1.getVisibility() == GLView.INVISIBLE && view2.getVisibility() == GLView.VISIBLE) {
//					String openId = view2.getOpenId();
//					int videoSrcType = view2.getVideoSrcType();
//					boolean isPC = view2.isPC();
//					boolean isMirror = view2.isMirror();
//					boolean isLoading = view2.isLoading();
//					view1.setRender(openId, videoSrcType);
//					view1.setIsPC(isPC);
//					view1.setMirror(isMirror);
//					view1.enableLoading(isLoading);
//					view1.setVisibility(GLView.VISIBLE);
//					view2.setVisibility(GLView.INVISIBLE);
//				}
//			}
//		}

		layoutVideoView(false);
	}

	void initQQGlView() {
		if (QLog.isColorLevel()) {
			QLog.d(TAG, QLog.CLR, "initQQGlView");
		}
		mGlRootView = (GLRootView) mRootView.findViewById(R.id.av_video_glview);
		mPreviewContainer= (RelativeLayout) mRootView.findViewById(R.id.av_preview_container);

        mGlVideoView = new GLVideoView[VChatUtil.VIDEO_MAX_COUNT];

		// for (int i = 0; i < mGlVideoView.length; i++) {
		// mGlVideoView[i] = new GLVideoView(mVideoController, mContext.getApplicationContext());
		// mGlVideoView[i].setVisibility(GLView.INVISIBLE);
		// addView(mGlVideoView[i]);
		// }
		mGlVideoView[0] = new GLVideoView(mContext.getApplicationContext(), mGraphicRenderMgr);
		mGlVideoView[0].setVisibility(GLView.INVISIBLE);
		addView(mGlVideoView[0]);
		for (int i = VChatUtil.VIDEO_MAX_COUNT - 1; i >= 1; i--) {
			mGlVideoView[i] = new GLVideoView(mContext.getApplicationContext(), mGraphicRenderMgr);
			mGlVideoView[i].setVisibility(GLView.INVISIBLE);
			addView(mGlVideoView[i]);
		}
		mGlRootView.setContentPane(this);
		setBackground(R.drawable.video_consult_bg);
		// set bitmap ,reuse the backgroud BitmapDrawable,mlzhong
		// setBackground(UITools.getBitmapFromResourceId(mContext, R.drawable.qav_gaudio_bg));

		mScaleGestureDetector = new ScaleGestureDetector(mContext, new ScaleGestureListener());
		mGestureDetector = new GestureDetector(mContext, new GestureListener());
		mMoveDetector = new MoveGestureDetector(mContext, new MoveListener());
		mTouchListener = new TouchListener();
		setOnTouchListener(mTouchListener);
	}

	boolean mCameraSurfaceCreated = false;

	void initCameraPreview() {
		
//		SurfaceView localVideo = (SurfaceView) mRootView.findViewById(R.id.av_video_surfaceView);
//		SurfaceHolder holder = localVideo.getHolder();
//		holder.addCallback(mSurfaceHolderListener);
//		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 3.0以下必须在初始化时调用，否则不能启动预览
//		localVideo.setZOrderMediaOverlay(true);
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = 1;
        layoutParams.height = 1;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        // layoutParams.flags |= LayoutParams.FLAG_NOT_TOUCHABLE;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.windowAnimations = 0;// android.R.style.Animation_Toast;
        layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        //layoutParams.setTitle("Toast");
        try {
        	mSurfaceView = new SurfaceView(mContext);
            SurfaceHolder holder = mSurfaceView.getHolder();
            holder.addCallback(mSurfaceHolderListener);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 3.0以下必须在初始化时调用，否则不能启动预览
            mSurfaceView.setZOrderMediaOverlay(true);
            windowManager.addView(mSurfaceView, layoutParams);
        } catch (IllegalStateException e) {
            windowManager.updateViewLayout(mSurfaceView, layoutParams);
            if (QLog.isColorLevel()) {
                QLog.d(TAG, QLog.CLR, "add camera surface view fail: IllegalStateException." + e);
            }
        } catch (Exception e) {
            if (QLog.isColorLevel()) {
                QLog.d(TAG, QLog.CLR, "add camera surface view fail." + e);
            }
        }		
		Log.e("memoryLeak", "memoryLeak initCameraPreview");
	}
	
	void unInitCameraaPreview() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        try {
            windowManager.removeView(mSurfaceView);
            mSurfaceView = null;
        } catch(Exception e) {
            if (QLog.isColorLevel()) {
                QLog.e(TAG, QLog.CLR, "remove camera view fail.", e);
            }
        }
	}

	void switchVideo(int index1, int index2) {
		if (QLog.isColorLevel()) {
			QLog.d(TAG, QLog.CLR, "switchVideo index1: " + index1 + ", index2: " + index2);
		}
		if (index1 == index2 || index1 < 0 || index1 >= mGlVideoView.length || index2 < 0 || index2 >= mGlVideoView.length) {
			return;
		}
		
		if (GLView.INVISIBLE == mGlVideoView[index1].getVisibility() || GLView.INVISIBLE == mGlVideoView[index2].getVisibility()) {
			Log.d("switchVideo", "can not switchVideo");
			return;
		}

		String identifier1 = mGlVideoView[index1].getIdentifier();
		int videoSrcType1 = mGlVideoView[index1].getVideoSrcType();
		boolean isPC1 = mGlVideoView[index1].isPC();
		boolean isMirror1 = mGlVideoView[index1].isMirror();
		boolean isLoading1 = mGlVideoView[index1].isLoading();
		String identifier2 = mGlVideoView[index2].getIdentifier();
		int videoSrcType2 = mGlVideoView[index2].getVideoSrcType();
		boolean isPC2 = mGlVideoView[index2].isPC();
		boolean isMirror2 = mGlVideoView[index2].isMirror();
		boolean isLoading2 = mGlVideoView[index2].isLoading();

		mGlVideoView[index1].setRender(identifier2, videoSrcType2);
		mGlVideoView[index1].setIsPC(isPC2);
		mGlVideoView[index1].setMirror(isMirror2);
		mGlVideoView[index1].enableLoading(isLoading2);
		mGlVideoView[index2].setRender(identifier1, videoSrcType1);
		mGlVideoView[index2].setIsPC(isPC1);
		mGlVideoView[index2].setMirror(isMirror1);
		mGlVideoView[index2].enableLoading(isLoading1);
		
		int temp = localViewIndex;
		localViewIndex = remoteViewIndex;
		remoteViewIndex = temp;
	}

	class Position {
		final static int CENTER = 0;
		final static int LEFT_TOP = 1;
		final static int RIGHT_TOP = 2;
		final static int RIGHT_BOTTOM = 3;
		final static int LEFT_BOTTOM = 4;
	}

	public void setSmallVideoViewLayout(boolean isRemoteHasVideo, String remoteIdentifier, int videoSrcType) {
		if (QLog.isColorLevel()) {
			QLog.d(TAG, QLog.CLR, "setSmallVideoViewLayout position: " + mPosition);
		}
		if (mContext == null) {
			return;
		}
		
		int left = 0;
		int top = 0;
		int right = 0;
		int bottom = 0;
		int width = getWidth();
		int height = getHeight();
		int w = mContext.getResources().getDimensionPixelSize(R.dimen.video_small_view_width);
		int h = mContext.getResources().getDimensionPixelSize(R.dimen.video_small_view_height);
		int edgeX = mContext.getResources().getDimensionPixelSize(R.dimen.video_small_view_offsetX);
		int edgeY = mContext.getResources().getDimensionPixelSize(R.dimen.video_small_view_offsetY);
		if (mBottomOffset == 0) {
			edgeY = edgeX;
		}

		switch (mPosition) {
		case Position.LEFT_TOP:
			left = edgeX;
			right = left + w;
			// if (mBottomOffset != 0) {
			// top = height - h - edgeY - mBottomOffset;
			// bottom = top + h;
			// } else {
			top = edgeY + mTopOffset;
			bottom = top + h;
			// }
			break;
		case Position.RIGHT_TOP:
			left = width - w - edgeX;
			right = left + w;
			// if (mBottomOffset != 0) {
			// top = height - h - edgeY - mBottomOffset;
			// bottom = top + h;
			// } else {
			top = edgeY + mTopOffset;
			bottom = top + h;
			// }
			break;
		case Position.LEFT_BOTTOM:
			left = edgeX;
			right = left + w;
			top = height - h - edgeY - mBottomOffset;
			bottom = top + h;
			break;
		case Position.RIGHT_BOTTOM:
			left = width - w - edgeX;
			top = height - h - edgeY - mBottomOffset;
			right = left + w;
			bottom = top + h;
			break;
		}
		
		
		if (isRemoteHasVideo) {// 打开摄像头
			GLVideoView view = null;
			mRemoteIdentifier = remoteIdentifier;
			int index = getViewIndexById(remoteIdentifier, videoSrcType);	
			
			//请求多路画面用这个测试
//			if (remoteViewIndex != -1 && !mRemoteIdentifier.equals("") && !mRemoteIdentifier.equals(remoteIdentifier)) {
//				closeVideoView(remoteViewIndex);
//			}
			
			if(!isSupportMultiVideo) {
				if (remoteViewIndex != -1) {
					closeVideoView(remoteViewIndex);
				}
			}
			if (index < 0) {
                closePreview(remoteIdentifier);
				index = getIdleViewIndex(0);					
				if (index >= 0) {
					view = mGlVideoView[index];
					view.setRender(remoteIdentifier, videoSrcType);
					remoteViewIndex = index;
				}
			} else {
				view = mGlVideoView[index];
			}
			if (view != null) {
				view.setIsPC(false);
				view.enableLoading(false);
				view.setVisibility(GLView.VISIBLE);
			}

		} else {// 关闭摄像头
			int index = getViewIndexById(remoteIdentifier, videoSrcType);			
			if (index >= 0) {
				closeVideoView(index);
				remoteViewIndex = -1;
			}
		}
        layoutVideoView(false);
		
//		if (null != mGlVideoView[1].getOpenId()) {
//			mGlVideoView[1].clearRender();
//		}
//		
//				
//		mGlVideoView[1].layout(left, top, right, bottom);
//		mGlVideoView[1].setRender(remoteOpenid, videoSrcType);
//		mGlVideoView[1].setIsPC(false);
//		mGlVideoView[1].enableLoading(false);	
//		mGlVideoView[1].setVisibility(View.VISIBLE);
	}

	int mPosition = Position.LEFT_TOP;
	boolean mDragMoving = false;

	public int getPosition() {
		return mPosition;
	}

	void checkAndChangeMargin(int deltaX, int deltaY) {
		if (mContext == null) {
			return;
		}
		int width = mContext.getResources().getDimensionPixelSize(R.dimen.video_small_view_width);
		int height = mContext.getResources().getDimensionPixelSize(R.dimen.video_small_view_height);

		Rect outRect = getBounds();
		int minOffsetX = 0;
		int minOffsetY = 0;
		int maxOffsetX = outRect.width() - width;
		int maxOffsetY = outRect.height() - height;

		int left = mGlVideoView[1].getBounds().left + deltaX;
		int top = mGlVideoView[1].getBounds().top + deltaY;
		if (left < minOffsetX) {
			left = minOffsetX;
		} else if (left > maxOffsetX) {
			left = maxOffsetX;
		}
		if (top < minOffsetY) {
			top = minOffsetY;
		} else if (top > maxOffsetY) {
			top = maxOffsetY;
		}
		int right = left + width;
		int bottom = top + height;
		mGlVideoView[1].layout(left, top, right, bottom);
	}

	int getSmallViewPosition() {
		int position = Position.CENTER;
		Rect visableRect = getBounds();
		int screenCenterX = visableRect.centerX();
		int screenCenterY = visableRect.centerY();
		int viewCenterX = mGlVideoView[1].getBounds().centerX();
		int viewCenterY = mGlVideoView[1].getBounds().centerY();
		if (viewCenterX < screenCenterX && viewCenterY < screenCenterY) {
			position = Position.LEFT_TOP;
		} else if (viewCenterX < screenCenterX && viewCenterY > screenCenterY) {
			position = Position.LEFT_BOTTOM;
		} else if (viewCenterX > screenCenterX && viewCenterY < screenCenterY) {
			position = Position.RIGHT_TOP;
		} else if (viewCenterX > screenCenterX && viewCenterY > screenCenterY) {
			position = Position.RIGHT_BOTTOM;
		}

		return position;
	}

	class TouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(GLView view, MotionEvent event) {
            mTargetIndex = -1;
            for(int i=0;i<mGlVideoView.length;i++){
                if(view==mGlVideoView[i]){
                    mTargetIndex=i;
                    break;
                }
            }
			if (mGestureDetector != null) {
				mGestureDetector.onTouchEvent(event);
			}
//			if (mTargetIndex == 1 && mMoveDetector != null) {
//				mMoveDetector.onTouchEvent(event);
//			} else if (mTargetIndex == 0 && mGlVideoView[0].getVideoSrcType() == AVView.VIDEO_SRC_TYPE_SCREEN) {
//				if (mScaleGestureDetector != null) {
//					mScaleGestureDetector.onTouchEvent(event);
//				}
//				if (mMoveDetector != null) {
//					mMoveDetector.onTouchEvent(event);
//				}
//			}
			return true;
		}
	};

	class GestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onSingleTapConfirmed(MotionEvent event) {
			if (QLog.isColorLevel())
				QLog.d(TAG, QLog.CLR, "GestureListener-->mTargetIndex=" + mTargetIndex);
//			if (mTargetIndex <= 0) {
//				// 显示控制层
//			} else {
////				switchVideo(0, mTargetIndex);
//			}
            if(mTargetIndex==-1)
                return true;
            if(mTargetIndex==localViewIndex){
                mQavsdkControl.toggleSwitchCamera();
            }else{
                if(enlargeUserId!=null){
                    enlargeUserId=null;
                    layoutVideoView(false);
                }else
                    enlargeVideo(mGlVideoView[mTargetIndex]);
            }
			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (mTargetIndex == 0 && mGlVideoView[0].getVideoSrcType() == AVView.VIDEO_SRC_TYPE_SCREEN) {
				mClickTimes++;
				if (mClickTimes % 2 == 1) {
					mGlVideoView[0].setScale(GLVideoView.MAX_SCALE + 1, 0, 0, true);
				} else {
					mGlVideoView[0].setScale(GLVideoView.MIN_SCALE, 0, 0, true);
				}
				return true;
			}
			return super.onDoubleTap(e);
		}
	}

	class MoveListener implements OnMoveGestureListener {
		int startX = 0;
		int startY = 0;
		int endX = 0;
		int endY = 0;
		int startPosition = 0;

		@Override
		public boolean onMoveBegin(MoveGestureDetector detector) {
			if (mTargetIndex == 0) {

			} else if (mTargetIndex == 1) {
				startX = (int) detector.getFocusX();
				startY = (int) detector.getFocusY();
				startPosition = getSmallViewPosition();
			}
			return true;
		}

		@Override
		public boolean onMove(MoveGestureDetector detector) {
			PointF delta = detector.getFocusDelta();
			int deltaX = (int) delta.x;
			int deltaY = (int) delta.y;
			if (mTargetIndex == 0) {
				mGlVideoView[0].setOffset(deltaX, deltaY, false);
			} else if (mTargetIndex == 1) {
				if (Math.abs(deltaX) > AVView.MAX_VIEW_COUNT || Math.abs(deltaY) > AVView.MAX_VIEW_COUNT) {
					mDragMoving = true;
				}
				// 修改拖动窗口的位置
				checkAndChangeMargin(deltaX, deltaY);
			}
			return true;
		}

		@Override
		public void onMoveEnd(MoveGestureDetector detector) {
			PointF delta = detector.getFocusDelta();
			int deltaX = (int) delta.x;
			int deltaY = (int) delta.y;
			if (mTargetIndex == 0) {
				mGlVideoView[0].setOffset(deltaX, deltaY, true);
			} else if (mTargetIndex == 1) {
				// 修改拖动窗口的位置
				checkAndChangeMargin(deltaX, deltaY);
				endX = (int) detector.getFocusX();
				endY = (int) detector.getFocusY();
				mPosition = getSmallViewDstPosition(startPosition, startX, startY, endX, endY);
				afterDrag(mPosition);
			}
		}
	};

	class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			float x = detector.getFocusX();
			float y = detector.getFocusY();
			float scale = detector.getScaleFactor();
			float curScale = mGlVideoView[0].getScale();
			mGlVideoView[0].setScale(curScale * scale, (int) x, (int) y, false);
			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			float x = detector.getFocusX();
			float y = detector.getFocusY();
			float scale = detector.getScaleFactor();
			float curScale = mGlVideoView[0].getScale();
			mGlVideoView[0].setScale(curScale * scale, (int) x, (int) y, true);
		}

	}

	enum MoveDistanceLevel {
		e_MoveDistance_Min, e_MoveDistance_Positive, e_MoveDistance_Negative
	};

	int getSmallViewDstPosition(int startPosition, int nStartX, int nStartY, int nEndX, int nEndY) {
		int thresholdX = mContext.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.video_smallview_move_thresholdX);
		int thresholdY = mContext.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.video_smallview_move_thresholdY);
		int xMoveDistanceLevelStandard = thresholdX;
		int yMoveDistanceLevelStandard = thresholdY;

		MoveDistanceLevel eXMoveDistanceLevel = MoveDistanceLevel.e_MoveDistance_Min;
		MoveDistanceLevel eYMoveDistanceLevel = MoveDistanceLevel.e_MoveDistance_Min;

		if (nEndX - nStartX > xMoveDistanceLevelStandard) {
			eXMoveDistanceLevel = MoveDistanceLevel.e_MoveDistance_Positive;
		} else if (nEndX - nStartX < -xMoveDistanceLevelStandard) {
			eXMoveDistanceLevel = MoveDistanceLevel.e_MoveDistance_Negative;
		} else {
			eXMoveDistanceLevel = MoveDistanceLevel.e_MoveDistance_Min;
		}

		if (nEndY - nStartY > yMoveDistanceLevelStandard) {
			eYMoveDistanceLevel = MoveDistanceLevel.e_MoveDistance_Positive;
		} else if (nEndY - nStartY < -yMoveDistanceLevelStandard) {
			eYMoveDistanceLevel = MoveDistanceLevel.e_MoveDistance_Negative;
		} else {
			eYMoveDistanceLevel = MoveDistanceLevel.e_MoveDistance_Min;
		}

		int eBeginPosition = startPosition;
		int eEndPosition = Position.LEFT_TOP;
		int eDstPosition = Position.LEFT_TOP;
		eEndPosition = getSmallViewPosition();

		if (eEndPosition == Position.RIGHT_BOTTOM) {
			if (eBeginPosition == Position.LEFT_TOP) {
				eDstPosition = Position.RIGHT_BOTTOM;
			} else if (eBeginPosition == Position.RIGHT_TOP) {
				eDstPosition = Position.RIGHT_BOTTOM;
			} else if (eBeginPosition == Position.LEFT_BOTTOM) {
				eDstPosition = Position.RIGHT_BOTTOM;
			} else if (eBeginPosition == Position.RIGHT_BOTTOM) {
				if (eXMoveDistanceLevel == MoveDistanceLevel.e_MoveDistance_Negative) {
					if (eYMoveDistanceLevel == MoveDistanceLevel.e_MoveDistance_Negative) {
						eDstPosition = Position.LEFT_TOP;
					} else {
						eDstPosition = Position.LEFT_BOTTOM;
					}
				} else {
					if (eYMoveDistanceLevel == MoveDistanceLevel.e_MoveDistance_Negative) {
						eDstPosition = Position.RIGHT_TOP;
					} else {
						eDstPosition = Position.RIGHT_BOTTOM;
					}
				}
			}
		} else if (eEndPosition == Position.RIGHT_TOP) {
			if (eBeginPosition == Position.LEFT_TOP) {
				eDstPosition = Position.RIGHT_TOP;
			} else if (eBeginPosition == Position.RIGHT_BOTTOM) {
				eDstPosition = Position.RIGHT_TOP;
			} else if (eBeginPosition == Position.LEFT_BOTTOM) {
				eDstPosition = Position.RIGHT_TOP;
			} else if (eBeginPosition == Position.RIGHT_TOP) {
				if (eXMoveDistanceLevel == MoveDistanceLevel.e_MoveDistance_Negative) {
					if (eYMoveDistanceLevel == MoveDistanceLevel.e_MoveDistance_Positive) {
						eDstPosition = Position.LEFT_BOTTOM;
					} else {
						eDstPosition = Position.LEFT_TOP;
					}
				} else {
					if (eYMoveDistanceLevel == MoveDistanceLevel.e_MoveDistance_Positive) {
						eDstPosition = Position.RIGHT_BOTTOM;
					} else {
						eDstPosition = Position.RIGHT_TOP;
					}
				}
			}
		} else if (eEndPosition == Position.LEFT_TOP) {
			if (eBeginPosition == Position.RIGHT_TOP) {
				eDstPosition = Position.LEFT_TOP;
			} else if (eBeginPosition == Position.RIGHT_BOTTOM) {
				eDstPosition = Position.LEFT_TOP;
			} else if (eBeginPosition == Position.LEFT_BOTTOM) {
				eDstPosition = Position.LEFT_TOP;
			} else if (eBeginPosition == Position.LEFT_TOP) {
				if (eXMoveDistanceLevel == MoveDistanceLevel.e_MoveDistance_Positive) {
					if (eYMoveDistanceLevel == MoveDistanceLevel.e_MoveDistance_Positive) {
						eDstPosition = Position.RIGHT_BOTTOM;
					} else {
						eDstPosition = Position.RIGHT_TOP;
					}
				} else {
					if (eYMoveDistanceLevel == MoveDistanceLevel.e_MoveDistance_Positive) {
						eDstPosition = Position.LEFT_BOTTOM;
					} else {
						eDstPosition = Position.LEFT_TOP;
					}
				}
			}
		} else if (eEndPosition == Position.LEFT_BOTTOM) {
			if (eBeginPosition == Position.LEFT_TOP) {
				eDstPosition = Position.LEFT_BOTTOM;
			} else if (eBeginPosition == Position.RIGHT_TOP) {
				eDstPosition = Position.LEFT_BOTTOM;
			} else if (eBeginPosition == Position.RIGHT_BOTTOM) {
				eDstPosition = Position.LEFT_BOTTOM;
			} else if (eBeginPosition == Position.LEFT_BOTTOM) {
				if (eXMoveDistanceLevel == MoveDistanceLevel.e_MoveDistance_Positive) {
					if (eYMoveDistanceLevel == MoveDistanceLevel.e_MoveDistance_Negative) {
						eDstPosition = Position.RIGHT_TOP;
					} else {
						eDstPosition = Position.RIGHT_BOTTOM;
					}
				} else {
					if (eYMoveDistanceLevel == MoveDistanceLevel.e_MoveDistance_Negative) {
						eDstPosition = Position.LEFT_TOP;
					} else {
						eDstPosition = Position.LEFT_BOTTOM;
					}
				}
			}
		}
		return eDstPosition;
	}

	void afterDrag(int position) {
		int width = mContext.getResources().getDimensionPixelSize(R.dimen.video_small_view_width);
		int height = mContext.getResources().getDimensionPixelSize(R.dimen.video_small_view_height);
		int edgeX = mContext.getResources().getDimensionPixelSize(R.dimen.video_small_view_offsetX);
		int edgeY = mContext.getResources().getDimensionPixelSize(R.dimen.video_small_view_offsetY);
		if (mBottomOffset == 0) {
			edgeY = edgeX;
		}
		Rect visableRect = getBounds();

		int fromX = mGlVideoView[1].getBounds().left;
		int fromY = mGlVideoView[1].getBounds().top;
		int toX = 0;
		int toY = 0;

		switch (position) {
		case Position.LEFT_TOP:
			toX = edgeX;
			toY = edgeY;
			break;
		case Position.RIGHT_TOP:
			toX = visableRect.width() - edgeX - width;
			toY = edgeY;
			break;
		case Position.RIGHT_BOTTOM:
			toX = visableRect.width() - edgeX - width;
			toY = visableRect.height() - edgeY - height;
			break;
		case Position.LEFT_BOTTOM:
			toX = edgeX;
			toY = visableRect.height() - edgeY - height;
			break;
		default:
			break;
		}
	}
	public void setSelfId(String key) {
		if (mGraphicRenderMgr != null) {
			mGraphicRenderMgr.setSelfId(key + "_" + AVView.VIDEO_SRC_TYPE_CAMERA);
		}
	}
	void onMemberChange() {
		Log.d(TAG, "WL_DEBUG onMemberChange start");
		QavsdkControl qavsdk = ImVideo.getInstance().mQavsdkControl;
		
		ArrayList<MemberInfo> audioAndCameraMemberList = qavsdk.getAudioAndCameraMemberList();

		for (MemberInfo memberInfo : audioAndCameraMemberList) {
			int index = getViewIndexById(memberInfo.identifier, AVView.VIDEO_SRC_TYPE_CAMERA);
			if (index >= 0) {
				Log.d(TAG, "WL_DEBUG onMemberChange memberInfo.hasCameraVideo = " + memberInfo.hasCameraVideo);

				if (!memberInfo.hasCameraVideo && !memberInfo.hasAudio) {
					closeVideoView(index);
				}
			}
		}
		
		ArrayList<MemberInfo> screenMemberList = qavsdk.getScreenMemberList();

		for (MemberInfo memberInfo : screenMemberList) {
			int index = getViewIndexById(memberInfo.identifier, AVView.VIDEO_SRC_TYPE_SCREEN);
			if (index >= 0) {
				Log.d(TAG, "WL_DEBUG onMemberChange memberInfo.hasScreenVideo = " + memberInfo.hasScreenVideo);

				if (!memberInfo.hasScreenVideo) {
					closeVideoView(index);
				}
			}
		}
		
		ArrayList<MemberInfo> memberList = qavsdk.getMemberList();
		// 去掉已经不再memberlist中的view
		if (!memberList.isEmpty()) {
			for (int i = 0; i < mGlVideoView.length; i++) {
				GLVideoView view = mGlVideoView[i];
				if (view == null)
					continue;
				String viewIdentifier = view.getIdentifier();
				int viewVideoSrcType = view.getVideoSrcType();
				
				if (TextUtils.isEmpty(viewIdentifier) || viewVideoSrcType == AVView.VIDEO_SRC_TYPE_NONE)
					continue;
				
				
				boolean memberExist = false;
				for (int j=0; j<memberList.size(); j++) {
					if (!TextUtils.isEmpty(memberList.get(j).identifier)) {
						int videoSrcType = AVView.VIDEO_SRC_TYPE_NONE;
						if(memberList.get(j).hasCameraVideo)videoSrcType = AVView.VIDEO_SRC_TYPE_CAMERA;
						else if(memberList.get(j).hasScreenVideo)videoSrcType = AVView.VIDEO_SRC_TYPE_SCREEN;
						else videoSrcType = AVView.VIDEO_SRC_TYPE_NONE;

						if (viewIdentifier.equals(memberList.get(j).identifier) && viewVideoSrcType == videoSrcType) {
							memberExist = true;
							break;
						}			
					}
				}
				
				if (!memberExist) {

					if (null != mQavsdkControl) {
						String selfIdentifier = mQavsdkControl.getSelfIdentifier();
						Log.d(TAG, "self identifier : " + selfIdentifier);
						if (selfIdentifier != null && selfIdentifier.equals(viewIdentifier)) {
							return;
						}
					}
					
					closeVideoView(i);
				}
			}	
		} else {
			for (int i = 0; i < mGlVideoView.length; i++) {
				closeVideoView(i);
			}
		}

		Log.d(TAG, "WL_DEBUG onMemberChange end");
	}

    public GLVideoView getMyselfView(){
        if(localViewIndex>=0)
            return mGlVideoView[localViewIndex];
        return null;
    }

	private View addPreview(UserInfo u){
		View v=  getPreviewView(u.id);
		if(v!=null){
			return v;
		}
//		v=new ImageView(mContext);
        v= LayoutInflater.from(mContext).inflate(R.layout.vchat_preview,mPreviewContainer,false);
		v.setTag(u.id);
		mPreviewContainer.addView(v);
        ImageView iv= (ImageView) v.findViewById(R.id.image_view);
        String url=u.pic;
        if(ImUtils.getLoginUserId().equals(u.id)){
            url=ImSdk.getInstance().userAvatar;
        }
        ImageLoader.getInstance().displayImage(url,iv, ImUtils.getAvatarNormalImageOptions());
        ImageView anim= (ImageView) v.findViewById(R.id.anim);
        AnimationDrawable d= (AnimationDrawable) anim.getDrawable();
        d.start();
        ImageView voice= (ImageView) v.findViewById(R.id.voice);
        AnimationDrawable voiceD= (AnimationDrawable) voice.getDrawable();
        voiceD.start();
		return v;
	}

	private View getPreviewView(String userId){
		View v=null;
		for(int i=0;i<mPreviewContainer.getChildCount();i++){
			View temp=mPreviewContainer.getChildAt(i);
			if(userId.equals(temp.getTag())){
				v=temp;
				break;
			}
		}
		return v;
	}
	private void closePreview(String userId){
		View v=getPreviewView(userId);
		if(v!=null){
			mPreviewContainer.removeView(v);
		}
	}
    public void onEventMainThread(VChatUserChangeEvent e){
        layoutVideoView(false);
    }
    public void onEventMainThread(VChatUserRemoveEvent e){
		closePreview(e.userId);
        layoutVideoView(false);
    }

    private void enlargeVideo(GLVideoView v){
        if(v.getIdentifier()==null){
            return;
        }
        enlargeUserId=v.getIdentifier();
        layoutVideoView(false);
    }
}
