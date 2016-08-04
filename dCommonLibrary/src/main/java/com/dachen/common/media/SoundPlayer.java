package com.dachen.common.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;

/**
 * 
 * @author gaozhuo
 * @since : 2015年12月2日
 *
 */
public class SoundPlayer {
	private Context mContext;
	private MediaPlayer mMediaPlayer;

	public SoundPlayer(Context context) {
		mContext = context;
		initMediaPlayer();
	}
	
	private void initMediaPlayer(){
		mMediaPlayer = new MediaPlayer();

		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
				mp.setLooping(true);
			}
		});
	}

	public void play(Uri uri) {
		if (mMediaPlayer == null) {
			return;
		}
		mMediaPlayer.reset();
		try {
			mMediaPlayer.setDataSource(mContext, uri);
			mMediaPlayer.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void stop() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
		}
	}

	public void release() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
	public boolean isPlaying(){
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			return true;
		}
		return false;
	}
}
