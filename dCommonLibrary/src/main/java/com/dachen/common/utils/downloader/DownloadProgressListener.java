package com.dachen.common.utils.downloader;

import android.view.View;

public interface DownloadProgressListener {
	void onProgressUpdate(String imageUri, View view, int current, int total);
}
