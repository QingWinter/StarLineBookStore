package com.github.winter.library.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by Winter on 2016/7/9.
 * Description the JavaBean of Book
 * E-mail huang.wqing@gmail.com
 */
public class Book extends BmobObject {
    private String title;//标题
    private String[] author;//作者
    private Integer out;//是否借出 0否1是
    private BmobUser owner;//借出人
    private Image images;//图片
    private String image;//图片
    private String publisher;//出版社
    private String alt;//豆瓣wap
    private String summary;//简介

    public Book() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getAuthor() {
        return author;
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }

    public Integer getOut() {
        return out;
    }

    public void setOut(Integer out) {
        this.out = out;
    }

    public BmobUser getOwner() {
        return owner;
    }

    public void setOwner(BmobUser owner) {
        this.owner = owner;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Image getImages() {
        return images;
    }

    public void setImages(Image images) {
        this.images = images;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }


}
