package com.sanzhi.work.network.model;

import java.util.List;

/**
 * Created by parade岁月 on 2019/9/26 18:11
 * 分页基础信息
 */
public class PageData<T> {
    //总的数据量
    private int totalElements;
    //总页数
    private int totalPages;
    //当前页码，0表示第1页
    private int number;
    //每页的数据条数
    private int size;
    //是否第一页
    private boolean first;
    //当前页有几条数据
    private int numberOfElements;
    private boolean last;
    private List<T> content;

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
