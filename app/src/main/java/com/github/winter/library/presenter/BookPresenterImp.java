package com.github.winter.library.presenter;

import android.content.Context;
import android.widget.Toast;

import com.github.winter.library.app.LibApplication;
import com.github.winter.library.data.Book;
import com.github.winter.library.data.Record;
import com.github.winter.library.view.MainView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Winter on 2016/7/9.
 * Description
 * E-mail huang.wqing@gmail.com
 */
public class BookPresenterImp implements BookPresenter {
    private MainView view;

    public BookPresenterImp(MainView view) {
        this.view = view;
    }

    @Override
    public void getAllBookList() {
        view.showRefresh();
        String bql ="select include owner, * from Book";
        BmobQuery<Book> query=new BmobQuery<>();
        query.setSQL(bql);
        query.doSQLQuery(new SQLQueryListener<Book>(){

            @Override
            public void done(BmobQueryResult<Book> result, BmobException e) {
                if(e ==null){
                    view.showBooks(result.getResults());
                }else{
                    Toast.makeText(LibApplication.getAppContext(), result.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void getMyBookList(cn.bmob.v3.BmobUser user) {
        view.showRefresh();
//        String bql ="select include owner, * from Book where owner = pointer('_User', '" +user.getObjectId() +"')";
        String bql ="select include owner, * from Book where owner = pointer('_User', ?)";
        BmobQuery<Book> query=new BmobQuery<>();
        query.doSQLQuery(bql, new SQLQueryListener<Book>(){

            @Override
            public void done(BmobQueryResult<Book> result, BmobException e) {
                if(e == null){
                    view.showBooks(result.getResults());
                }else{
                    Toast.makeText(LibApplication.getAppContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, user.getObjectId());

//        BmobQuery<Book> query=new BmobQuery<>();
//        query.setSQL(bql);
//        query.doSQLQuery(new SQLQueryListener<Book>(){
//
//            @Override
//            public void done(BmobQueryResult<Book> result, BmobException e) {
//                if(e == null){
//                    view.showBooks(result.getResults());
//                }else{
//                    Toast.makeText(LibApplication.getAppContext(), e.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    @Override
    public void getBorrowableBookList() {
        view.showRefresh();
        String bql ="select * from Book where out != 1";
        BmobQuery<Book> query=new BmobQuery<>();
        query.setSQL(bql);
        query.doSQLQuery(new SQLQueryListener<Book>(){

            @Override
            public void done(BmobQueryResult<Book> result, BmobException e) {
                if(e ==null){
                    view.showBooks(result.getResults());
                }else{
                    Toast.makeText(LibApplication.getAppContext(), result.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void getBorrowedBookList() {
        view.showRefresh();
        String bql ="select include owner, * from Book where out != 0";
        BmobQuery<Book> query=new BmobQuery<>();
        query.setSQL(bql);
        query.doSQLQuery(new SQLQueryListener<Book>(){

            @Override
            public void done(BmobQueryResult<Book> result, BmobException e) {
                if(e ==null){
                    view.showBooks(result.getResults());
                }else{
                    Toast.makeText(LibApplication.getAppContext(), result.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void borrowBook(final Book book, final BmobUser bmobUser) {
        book.setOwner(bmobUser);
        book.setOut(1);
        book.update(book.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    view.borrowSuccess(book);
                } else {
                    Toast.makeText((Context) view, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Record record = new Record();
        record.setBook(book);
        record.setUser(bmobUser);
        record.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                } else {
                    Toast.makeText((Context) view, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void back(final Book book, BmobUser user) {
        book.remove("owner");
        book.setOut(0);
        book.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    view.borrowSuccess(book);
                } else {
                    Toast.makeText((Context) view, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
