package com.github.winter.library.view;

import com.github.winter.library.data.Book;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Winter on 2016/7/9.
 * Description
 * E-mail huang.wqing@gmail.com
 */
public interface MainView {

    void showMsg(String msg);

    void showUser(BmobUser user);

    void showBooks(List<Book> bookList);

    void showRefresh();

    void showLoading(boolean show);

    void borrowSuccess(Book book);

}
