package com.shawn.geektime.homework.user.db.repository;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

  private static final int DEFAULT_PAGE_SIZE = 15;

  private int pageSize;

  private int currentPageNum;

  private List<T> data;

  private long total;

  public Page() {
    this(DEFAULT_PAGE_SIZE, 0, new ArrayList<>(), 0);
  }

  public Page(int pageSize, int currentPageNum, List<T> data, long total) {
    this.pageSize = pageSize;
    this.currentPageNum = currentPageNum;
    this.data = data;
    this.total = total;
  }

  public int getPageSize() {
    return pageSize;
  }

  public int getCurrentPageNum() {
    return currentPageNum;
  }

  public List<T> getData() {
    return data;
  }

  public long getTotal() {
    return total;
  }

}
