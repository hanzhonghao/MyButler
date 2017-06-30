package com.zhonghao.mybutler.utils;

import android.util.Log;

/**
 * 项目名称：MyButler
 * 包名：com.zhonghao.mybutler.utils
 * 类描述： lOG封装类
 * 创建人：小豪
 * 创建时间：2017/6/22 17:03
 */

public class L {
    //开关
    public static final boolean DEBUG= true;

    //TAG
    public static final String TAG = "MyButler";

    //五个等级DIWE

    public static void d(String text){
        if (DEBUG){
            Log.d(TAG,text);
        }
    }

    public static void i(String text){
        if(DEBUG){
            Log.i(TAG,text);
        }
    }

    public static void w(String text){
        if(DEBUG){
            Log.w(TAG,text);
        }
    }

    public static void e(String text){
        if(DEBUG){
            Log.e(TAG,text);
        }
    }
}
