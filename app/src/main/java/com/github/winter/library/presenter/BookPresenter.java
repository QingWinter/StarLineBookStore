package com.github.winter.library.presenter;

import com.github.winter.library.data.Book;

import cn.bmob.v3.BmobUser;

/**
 * Created by Winter on 2016/7/9.
 * Description
 * E-mail huang.wqing@gmail.com
 */
public interface BookPresenter {

    void getAllBookList();

    void getMyBookList(BmobUser user);

    void getBorrowableBookList();

    void getBorrowedBookList();

    void borrowBook(Book book, BmobUser bmobUser);

    void back(Book book, BmobUser bmobUser);
}
