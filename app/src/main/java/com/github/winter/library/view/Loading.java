package com.github.winter.library.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Winter on 2016/7/9.
 * Description
 * E-mail huang.wqing@gmail.com
 */
public class Loading extends Dialog {
    public Loading(Context context) {
        super(context);
    }

    public Loading(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected Loading(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
