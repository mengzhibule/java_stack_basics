package com.shawn.geektime.homework.user.db.enums;

public interface SQLScripts {

  String SPACE = " ";
  String DOT = ".";
  String LEFT_PARENTHESES = "(";
  String RIGHT_PARENTHESES = ")";

  String ALTER_TABLE = "ALTER TABLE";
  String SELECT = "SELECT";
  String DISTINCT = "DISTINCT";
  String WHERE = "WHERE";
  String FROM = "FROM";
  String LIMIT = "LIMIT";
  String OFFSET = "OFFSET";
  String SET = "SET";
  String UPDATE = "UPDATE";
  String DELETE = "DELETE";
  String DELETE_FROM = "DELETE" + SPACE + FROM;
  String IN = "IN";
  String ON = "ON";
  String AND = "AND";
  String INSERT = "INSERT";
  String INTO = "INTO";
  String INSERT_INTO = INSERT + SPACE + INTO;
  String VALUES = "VALUES";

  String AS = "AS";

  String PLACE_HOLDER = "?";
  String EQ_PLACE_HOLDER = " = " + PLACE_HOLDER;
  String LIKE_HOLDER = "%";
  String COMMA = ",";
  String STAR = "*";
  String SINGLE_QUOTES = "'";
  String KEYWORD_MARK = "`";
}
