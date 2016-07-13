package com.github.winter.library.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.winter.library.R;
import com.github.winter.library.app.BaseActivity;
import com.github.winter.library.data.Book;
import com.github.winter.library.data.Type;
import com.github.winter.library.databinding.ActivityMainBinding;
import com.github.winter.library.presenter.BookAdapter;
import com.github.winter.library.presenter.BookPresenter;
import com.github.winter.library.presenter.BookPresenterImp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity implements MainView, SwipeRefreshLayout.OnRefreshListener, BookAdapter.OnItemClickListener {
    public static final String ID = "id";
    public static final String NAME = "name";
    ActivityMainBinding binding;
    BookPresenter bookPresenter;
    //    UserPresenter userPresenter;
    Type listType = Type.ALL;
    BookAdapter adapter;
    private String name;
    private BmobUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.refreshLayout.setOnRefreshListener(this);
        adapter = new BookAdapter(this);
        adapter.setOnItemClickListener(this);
        binding.list.setAdapter(adapter);
        binding.list.setHasFixedSize(true);
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        bookPresenter = new BookPresenterImp(this);
        setSupportActionBar(binding.toolBar);
        binding.toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        binding.toolBar.setSubtitleTextColor(getResources().getColor(R.color.white));
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        String id = sharedPreferences.getString(ID, "");
        name = sharedPreferences.getString(NAME, "");
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(name)) {
//            showMsgForWhile(getString(R.string.welcome_back, name));
            user = new BmobUser();
            user.setObjectId(id);
            user.setUsername(name);
            showUser(user);
            EventBus.getDefault().postSticky(user);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void showMsgForWhile(final String msg) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setMessage(msg).create();
        alertDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                binding.toolBar.setTitle(getString(R.string.hello, name));
            }
        }, LoginActivity.DELAY_TIME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switchAccount:
                switchAccount();
                break;
            case R.id.all:
                listType = Type.ALL;
                binding.refreshLayout.setRefreshing(true);
                bookPresenter.getAllBookList();
                break;
            case R.id.mine:
                listType = Type.MINE;
                binding.refreshLayout.setRefreshing(true);
                bookPresenter.getMyBookList(EventBus.getDefault().getStickyEvent(BmobUser.class));
                break;
            case R.id.borrowable:
                listType = Type.BORROWABLE;
                binding.refreshLayout.setRefreshing(true);
                bookPresenter.getBorrowableBookList();
                break;
            case R.id.borrowed:
                listType = Type.BORROWED;
                binding.refreshLayout.setRefreshing(true);
                bookPresenter.getBorrowedBookList();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void switchAccount() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.switchAccount))
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().removeStickyEvent(BmobUser.class);
                        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit();
                        editor.remove(NAME).remove(ID).apply();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                }).create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == user && null == (user = EventBus.getDefault().getStickyEvent(BmobUser.class))) {
//            startActivity(new Intent(this, LoginActivity.class));
        } else {
            showUser(user);
//            showMsgForWhile(getString(R.string.welcome, user.getUsername()));
        }
        binding.refreshLayout.setRefreshing(true);
        bookPresenter.getAllBookList();
    }

    @Subscribe
    public void onEvent(BmobUser user) {
        this.user = user;
        showUser(user);
    }

    @Override
    public void showMsg(String msg) {
        binding.refreshLayout.setRefreshing(false);
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .show();
    }

    @Override
    public void showUser(BmobUser user) {
        binding.toolBar.setSubtitle(getString(R.string.hello, user.getUsername()));
    }

    @Override
    public void showBooks(List<Book> bookList) {
        binding.refreshLayout.setRefreshing(false);
        adapter.setData(bookList);
    }

    @Override
    public void showRefresh() {
        binding.refreshLayout.setRefreshing(true);

    }

    @Override
    public void showLoading(boolean show) {

    }

    @Override
    public void borrowSuccess(Book book) {
        adapter.updateItem(book, listType);
    }

    @Override
    public void onRefresh() {
        switch (listType) {
            case ALL:
                bookPresenter.getAllBookList();
                break;
            case MINE:
                bookPresenter.getMyBookList(EventBus.getDefault().getStickyEvent(BmobUser.class));
                break;
            case BORROWABLE:
                bookPresenter.getBorrowableBookList();
                break;
            case BORROWED:
                bookPresenter.getBorrowedBookList();
                break;
        }
    }

    @Override
    public void onItemClick(View view, final Book book) {
        int out = book.getOut();
        if (0 == out) {
            new AlertDialog.Builder(this)
                    .setTitle(book.getTitle())
                    .setItems(new String[]{"借入", "查看详情"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switchWhich(which, book, "borrow");
                        }
                    }).setNegativeButton("取消", null)
                    .create()
                    .show();
        } else if (1 == out) {
            if (book.getOwner().getObjectId().equals(user.getObjectId()) || "管理员".equals(user.getUsername())) {
                new AlertDialog.Builder(this)
                        .setTitle(book.getTitle())
                        .setItems(new String[]{"还书", "查看详情"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switchWhich(which, book, "back");
                            }
                        }).setNegativeButton("取消", null)
                        .create()
                        .show();
            } else {
                final AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.sorry, book.getOwner().getUsername()))
                        .setPositiveButton("确定", null)
                        .create();
                alertDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    }
                }, 2000);
            }
        }
    }

    private void switchWhich(int which, Book book, String operation) {
        switch (which) {
            case 0:
                if ("borrow".equals(operation)) {
                    bookPresenter.borrowBook(book, EventBus.getDefault().getStickyEvent(BmobUser.class));
                } else if ("back".equals(operation)) {
                    bookPresenter.back(book, EventBus.getDefault().getStickyEvent(BmobUser.class));
                }
                break;
            case 1:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra(WebActivity.URL, book.getAlt());
                startActivity(intent);
                break;
        }
    }
}
