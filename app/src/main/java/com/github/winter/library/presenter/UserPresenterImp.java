package com.github.winter.library.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.winter.library.view.MainActivity;
import com.github.winter.library.R;
import com.github.winter.library.app.LibApplication;
import com.github.winter.library.view.LoginView;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Winter on 2016/7/9.
 * Description
 * E-mail huang.wqing@gmail.com
 */
public class UserPresenterImp implements UserPresenter {
    private LoginView view;
    private BmobUser user;

    public UserPresenterImp(LoginView view) {
        this.view = view;
    }

    @Override
    public BmobUser getUser() {
        return user;
    }

    @Override
    public void registerOrLogin(final String userName, final String pwd) {
        user = new BmobUser();
        user.setUsername(userName);
        user.setPassword(pwd);
        user.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    //创建成功
                    signInOrSignUpSuccess(user, userName);
                } else {
                    //创建失败，已存在用户
                    login(userName, pwd);
                }
            }
        });
    }

    public void login(final String userName, String pwd) {
        final BmobUser user = new BmobUser();
        user.setUsername(userName);
        user.setPassword(pwd);
        user.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    signInOrSignUpSuccess(user, userName);
                } else {
                    view.showError(LibApplication.getAppContext().getString(R.string.errorUser));
                }
            }
        });
    }

    private void signInOrSignUpSuccess(BmobUser user, String userName) {
        EventBus.getDefault().postSticky(user);
        SharedPreferences sharedPreferences = LibApplication.getAppContext().getSharedPreferences(LibApplication.getAppContext().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MainActivity.ID, user.getObjectId()).putString(MainActivity.NAME, user.getUsername()).apply();
        view.showMsg(LibApplication.getAppContext().getString(R.string.welcome_back, userName));
    }
}