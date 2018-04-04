package com.android.karthi.androidtask.Const;

import android.os.Environment;

/**
 * Created by Karthi on 27/3/2018.
 */

public class Const {

    public static final int PER_PAGE = 15;

    public static final String END_POINT = "https://api.unsplash.com/search/";
    public static final String CLIENT_ID = "3186c8c450cd966d2b028c3a7b14f1c0d44c32feb1edaa35193aecd0d9e980b3";
    public static final String My_INTENT = "com.android.karthi.androidtask.Activity_myintent";
    public static final String My_INTENT_POSITION = "com.android.karthi.androidtask.Activity_myintent_position";
    public static final String My_INTENT_ARG_POSITION = "com.android.karthi.androidtask.Activity_arg_position";
    public static final String My_INTENT_DOWNLOAD = "com.android.karthi.androidtask.download_url";
    public static final String My_INTENT_RESPONSE_DOWNLOAD = "com.android.karthi.androidtask.download_url_response";
    public static final String My_BROADCAST_ACTION = "com.android.karthi.androidtask.intent_servicew.ALL_DONE";
    public static final String DOWNLOAD_PATH  = Environment.getExternalStorageDirectory().toString()+"/Unsplash/";
}