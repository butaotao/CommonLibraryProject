package com.dachen.common.toolbox;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

import java.io.File;

/**
 * Created by Mcp on 2016/8/31.
 */
public class SingleMediaScanner {
    private MediaScannerConnection mMs;
    private File mFile;
    private Context context;

    public SingleMediaScanner(Context context, File f) {
        this.context=context;
        mFile = f;
        mMs = new MediaScannerConnection(context, client);
        mMs.connect();
    }

    private MediaScannerConnectionClient client=new MediaScannerConnectionClient(){
        @Override
        public void onMediaScannerConnected() {
            mMs.scanFile(mFile.getAbsolutePath(), null);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mMs.disconnect();
        }
    };


}
