package com.android.karthi.androidtask.Service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.android.karthi.androidtask.Const.Const.DOWNLOAD_PATH;
import static com.android.karthi.androidtask.Const.Const.My_BROADCAST_ACTION;
import static com.android.karthi.androidtask.Const.Const.My_INTENT_DOWNLOAD;
import static com.android.karthi.androidtask.Const.Const.My_INTENT_RESPONSE_DOWNLOAD;

public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";

    public DownloadService() {
        super("My download service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String strUrl = intent.getStringExtra(My_INTENT_DOWNLOAD);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(My_BROADCAST_ACTION);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        //download file
        int count;
        URL url;
        try {
            Log.d(TAG, "path: " + strUrl);

            url = new URL(strUrl);
            String pathl = "";
            try {
                File directory = new File(DOWNLOAD_PATH);
                if (! directory.exists()){
                    directory.mkdir();
                }
                File f = new File(DOWNLOAD_PATH);
                if (f.exists()) {
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    InputStream is = con.getInputStream();
                    String pathr = url.getPath();
                    String filename = pathr.substring(pathr.lastIndexOf('/') + 1);
                    pathl = DOWNLOAD_PATH + filename + ".jpg";
                    Log.d(TAG, "path: " + pathl);
                    FileOutputStream fos = new FileOutputStream(pathl);
                    int lenghtOfFile = con.getContentLength();
                    byte data[] = new byte[1024];
                    long total = 0, result, old_result = 0;
                    while ((count = is.read(data)) != -1) {
                        total += count;
                        // send broadcast of progress update
                        result = (int) ((total * 100) / lenghtOfFile);
                        if (result != 0 && old_result != result) {
                            Log.d(TAG, "onHandleIntent_count: " + result);
                            old_result = result;
                            //send broadcast only at modulo(%) of 5
                            if (old_result % 5 == 0) {
                                broadcastIntent.putExtra(My_INTENT_RESPONSE_DOWNLOAD, (int) result);
                                localBroadcastManager.sendBroadcast(broadcastIntent);
                            }
                        }
                        // writing data to output file
                        fos.write(data, 0, count);
                    }
                    is.close();
                    fos.flush();
                    fos.close();
                } else {
                    Log.e("Error", "Not found: " + DOWNLOAD_PATH);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        broadcastIntent.putExtra(My_INTENT_RESPONSE_DOWNLOAD, 0);
        localBroadcastManager.sendBroadcast(broadcastIntent);
        //send broadcast to activity
        Log.d(TAG, "onHandleIntent: service_stopped");
    }
}
