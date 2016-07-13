package com.github.winter.library.presenter;

import cn.bmob.v3.BmobUser;

/**
 * Created by Winter on 2016/7/9.
 * Description
 * E-mail huang.wqing@gmail.com
 */
public interface UserPresenter {

    void registerOrLogin(String userName, String pwd);

    BmobUser getUser();
}
