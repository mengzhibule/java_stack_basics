package com.shawn.geektime.homework.user.db.enums;

public enum SQLKeywords {
  ALTER_TABLE(SQLScripts.ALTER_TABLE),
  ;

  private String script;

  SQLKeywords(String script) {
    this.script = script;
  }

  public String getScript() {
    return script;
  }
}
