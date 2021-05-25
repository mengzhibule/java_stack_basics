package com.shawn.geektime.homework.user.db.repository;

public class PageRequest {

  private int pageSize;

  private int pageNo;

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getPageNo() {
    return pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }
}
