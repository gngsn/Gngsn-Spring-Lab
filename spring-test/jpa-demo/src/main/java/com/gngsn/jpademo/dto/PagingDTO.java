package com.gngsn.jpademo.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class PagingDTO implements Serializable {

    private long total;
    private long totalPage;
    private int page;
    private int size;

    private long minPage;
    private long maxPage;

    public void setTotal(long total, long totalPage) {
        this.total = total;
        this.totalPage = totalPage;

        int mid = PAGING_DISPLAY_NUM / 2;
        this.minPage = page < mid-1 ? 0: page-mid;
        this.maxPage = page > totalPage-mid ? totalPage: page+mid;
    }

    private int PAGING_DISPLAY_NUM = 10;
}