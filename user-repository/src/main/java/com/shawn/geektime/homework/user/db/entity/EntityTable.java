package com.shawn.geektime.homework.user.db.entity;

import java.util.List;
import java.util.Map;

public class EntityTable {

  private String tableName;

  private List<String> columns;

  private List<String> pks;

  private Map<String, EntityColumn> entityColumnMap;

  private List<EntityColumn> entityColumns;

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public List<String> getColumns() {
    return columns;
  }

  public void setColumns(List<String> columns) {
    this.columns = columns;
  }

  public List<String> getPks() {
    return pks;
  }

  public void setPks(List<String> pks) {
    this.pks = pks;
  }

  public Map<String, EntityColumn> getEntityColumnMap() {
    return entityColumnMap;
  }

  public void setEntityColumnMap(Map<String, EntityColumn> entityColumnMap) {
    this.entityColumnMap = entityColumnMap;
  }

  public List<EntityColumn> getEntityColumns() {
    return entityColumns;
  }

  public void setEntityColumns(List<EntityColumn> entityColumns) {
    this.entityColumns = entityColumns;
  }
}
