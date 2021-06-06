package com.shawn.geektime.homework.user.db.enums;

import com.shawn.geektime.homework.user.db.exception.NoSuchSqlOperationsException;
import java.util.Arrays;

public enum SQLOps {
  EQ("="),
  NE("<>"),
  GT(">"),
  LT("<"),
  GTE(">="),
  LTE("<="),
  LIKE("LIKE"),
  NOT_LIKE("NOT LIKE"),
  IN("IN"),
  NOT_IN("NOT IN"),
  IS_NOT_NULL("IS NOT NULL"),
  IS_NULL("IS NULL"),
  X(""),
  LIMIT("LIMIT"),
  OFFSET("OFFSET"),
  SUB("SUB"),

  NONE(""),
  AND(" AND "),
  OR(" OR "),
  ORDER_BY(" ORDER BY "),
  GROUP_BY(" GROUP BY "),
  HAVING(" HAVING "),
  WHERE(" WHERE ");

  private String op;

  SQLOps(String str) {
    op = str;
  }

  public String getOp() {
    return op;
  }

  public static SQLOps toSQLOps(String ops) {
    return Arrays.stream(values())
        .filter(sqlOps -> sqlOps.getOp().equals(ops))
        .findFirst()
        .orElseThrow(
            () ->
                new NoSuchSqlOperationsException(
                    String.format(
                        "the SQL operations are not supported, this operation is: [%s]", ops)));
  }
}
