package com.github.winter.library.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by Winter on 2016/7/10.
 * Description
 * E-mail huang.wqing@gmail.com
 */
public class Record extends BmobObject {
    private Book book;
    private BmobUser user;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BmobUser getUser() {
        return user;
    }

    public void setUser(BmobUser user) {
        this.user = user;
    }
}
