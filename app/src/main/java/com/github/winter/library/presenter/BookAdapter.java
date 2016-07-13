package com.github.winter.library.presenter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.winter.library.R;
import com.github.winter.library.data.Book;
import com.github.winter.library.databinding.BookItemBinding;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Winter on 2016/7/9.
 * Description
 * E-mail huang.wqing@gmail.com
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private List<Book> data;
    private Context context;
    private BookItemBinding bookItemBinding;
    private OnItemClickListener onItemClickListener;
    private int position = -1;

    public BookAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Book> data) {
        this.data = new LinkedList<>(data);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bookItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.book_item, parent, false);
        return new ViewHolder(bookItemBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = data.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(Arrays.toString(book.getAuthor()));
        int out = 0;
        if (null != book.getOut()) {
            out = book.getOut();
        }
        if (0 == out) {
            holder.out.setText(context.getResources().getString(R.string.free));
            holder.out.setTextColor(context.getResources().getColor(R.color.green));
            holder.owner.setVisibility(View.GONE);
        } else if (1 == out) {
            holder.out.setText(context.getResources().getString(R.string.borrowed));
            holder.out.setTextColor(context.getResources().getColor(R.color.red));
            holder.owner.setVisibility(View.VISIBLE);
            holder.owner.setText(context.getResources().getString(R.string.borrowed_at, book.getOwner().getUsername(), book.getUpdatedAt()));

        }
        Picasso.with(context).load(book.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void updateItem(Book book) {
        data.set(position, book);
        notifyItemChanged(position);
        position = -1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        TextView out;
        TextView owner;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = bookItemBinding.title;
            author = bookItemBinding.author;
            out = bookItemBinding.out;
            owner = bookItemBinding.owner;
            image = bookItemBinding.image;
            bookItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        position = getLayoutPosition();
                        onItemClickListener.onItemClick(v, data.get(position));
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view, Book book);
    }
}
