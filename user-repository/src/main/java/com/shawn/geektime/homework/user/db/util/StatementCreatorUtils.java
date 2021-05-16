package com.shawn.geektime.homework.user.db.util;

import com.sun.istack.internal.Nullable;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Utility methods for PreparedStatement
 *
 * @author Shawn
 * @since 1.0
 */
public class StatementCreatorUtils {

  private static int TYPE_UNKNOWN = Integer.MIN_VALUE;

  private static final Map<Class<?>, Integer> javaTypeToSqlTypeMap = new HashMap<>(32);

  static {
    javaTypeToSqlTypeMap.put(boolean.class, Types.BOOLEAN);
    javaTypeToSqlTypeMap.put(Boolean.class, Types.BOOLEAN);
    javaTypeToSqlTypeMap.put(byte.class, Types.TINYINT);
    javaTypeToSqlTypeMap.put(Byte.class, Types.TINYINT);
    javaTypeToSqlTypeMap.put(short.class, Types.SMALLINT);
    javaTypeToSqlTypeMap.put(Short.class, Types.SMALLINT);
    javaTypeToSqlTypeMap.put(int.class, Types.INTEGER);
    javaTypeToSqlTypeMap.put(Integer.class, Types.INTEGER);
    javaTypeToSqlTypeMap.put(long.class, Types.BIGINT);
    javaTypeToSqlTypeMap.put(Long.class, Types.BIGINT);
    javaTypeToSqlTypeMap.put(BigInteger.class, Types.BIGINT);
    javaTypeToSqlTypeMap.put(float.class, Types.FLOAT);
    javaTypeToSqlTypeMap.put(Float.class, Types.FLOAT);
    javaTypeToSqlTypeMap.put(double.class, Types.DOUBLE);
    javaTypeToSqlTypeMap.put(Double.class, Types.DOUBLE);
    javaTypeToSqlTypeMap.put(BigDecimal.class, Types.DECIMAL);
    javaTypeToSqlTypeMap.put(java.sql.Date.class, Types.DATE);
    javaTypeToSqlTypeMap.put(java.sql.Time.class, Types.TIME);
    javaTypeToSqlTypeMap.put(java.sql.Timestamp.class, Types.TIMESTAMP);
    javaTypeToSqlTypeMap.put(Blob.class, Types.BLOB);
    javaTypeToSqlTypeMap.put(Clob.class, Types.CLOB);
  }

  /**
   * Derive a default SQL type from the given Java type.
   *
   * @param javaType the Java type to translate
   * @return the corresponding SQL type
   */
  public static int javaTypeToSqlParameterType(@Nullable Class<?> javaType) {
    if (javaType == null) {
      return Types.NULL;
    }
    Integer sqlType = javaTypeToSqlTypeMap.get(javaType);
    if (sqlType != null) {
      return sqlType;
    }
    if (Number.class.isAssignableFrom(javaType)) {
      return Types.NUMERIC;
    }
    if (isStringValue(javaType)) {
      return Types.VARCHAR;
    }
    if (isDateValue(javaType) || Calendar.class.isAssignableFrom(javaType)) {
      return Types.TIMESTAMP;
    }
    return TYPE_UNKNOWN;
  }

  private static boolean isStringValue(Class<?> paramType) {
    // Consider any CharSequence (including StringBuffer and StringBuilder) as a String.
    return (CharSequence.class.isAssignableFrom(paramType)
        || StringWriter.class.isAssignableFrom(paramType));
  }

  /**
   * Check whether the given value is a {@code java.util.Date} (but not one of the JDBC-specific
   * subclasses).
   */
  private static boolean isDateValue(Class<?> paramType) {
    return (java.util.Date.class.isAssignableFrom(paramType)
        && !(java.sql.Date.class.isAssignableFrom(paramType)
            || java.sql.Time.class.isAssignableFrom(paramType)
            || java.sql.Timestamp.class.isAssignableFrom(paramType)));
  }

  public static void setSqlParamValue(PreparedStatement ps, int parameterIndex, Object param)
      throws SQLException {
    int sqlType;
    if (Objects.isNull(param)) {
      sqlType = javaTypeToSqlParameterType(null);
    } else {
      sqlType = javaTypeToSqlParameterType(param.getClass());
    }
    if (sqlType == Types.VARCHAR || sqlType == Types.LONGVARCHAR) {
      ps.setString(parameterIndex, param.toString());
    } else if (sqlType == Types.NVARCHAR || sqlType == Types.LONGNVARCHAR) {
      ps.setNString(parameterIndex, param.toString());
    } else if ((sqlType == Types.CLOB || sqlType == Types.NCLOB)
        && isStringValue(param.getClass())) {
      String strVal = param.toString();
      if (strVal.length() > 4000) {
        // Necessary for older Oracle drivers, in particular when running against an Oracle 10
        // database.
        // Should also work fine against other drivers/databases since it uses standard JDBC 4.0
        // API.
        if (sqlType == Types.NCLOB) {
          ps.setNClob(parameterIndex, new StringReader(strVal), strVal.length());
        } else {
          ps.setClob(parameterIndex, new StringReader(strVal), strVal.length());
        }
      } else {
        // Fallback: setString or setNString binding
        if (sqlType == Types.NCLOB) {
          ps.setNString(parameterIndex, strVal);
        } else {
          ps.setString(parameterIndex, strVal);
        }
      }
    } else if (sqlType == Types.DECIMAL || sqlType == Types.NUMERIC) {
      if (param instanceof BigDecimal) {
        ps.setBigDecimal(parameterIndex, (BigDecimal) param);
      } else {
        ps.setObject(parameterIndex, param, sqlType);
      }
    } else if (sqlType == Types.BOOLEAN) {
      if (param instanceof Boolean) {
        ps.setBoolean(parameterIndex, (Boolean) param);
      } else {
        ps.setObject(parameterIndex, param, Types.BOOLEAN);
      }
    } else if (sqlType == Types.DATE) {
      if (param instanceof java.util.Date) {
        if (param instanceof java.sql.Date) {
          ps.setDate(parameterIndex, (java.sql.Date) param);
        } else {
          ps.setDate(parameterIndex, new java.sql.Date(((java.util.Date) param).getTime()));
        }
      } else if (param instanceof Calendar) {
        Calendar cal = (Calendar) param;
        ps.setDate(parameterIndex, new java.sql.Date(cal.getTime().getTime()), cal);
      } else {
        ps.setObject(parameterIndex, param, Types.DATE);
      }
    } else if (sqlType == Types.TIME) {
      if (param instanceof java.util.Date) {
        if (param instanceof java.sql.Time) {
          ps.setTime(parameterIndex, (java.sql.Time) param);
        } else {
          ps.setTime(parameterIndex, new java.sql.Time(((java.util.Date) param).getTime()));
        }
      } else if (param instanceof Calendar) {
        Calendar cal = (Calendar) param;
        ps.setTime(parameterIndex, new java.sql.Time(cal.getTime().getTime()), cal);
      } else {
        ps.setObject(parameterIndex, param, Types.TIME);
      }
    } else if (sqlType == Types.TIMESTAMP) {
      if (param instanceof java.util.Date) {
        if (param instanceof java.sql.Timestamp) {
          ps.setTimestamp(parameterIndex, (java.sql.Timestamp) param);
        } else {
          ps.setTimestamp(
              parameterIndex, new java.sql.Timestamp(((java.util.Date) param).getTime()));
        }
      } else if (param instanceof Calendar) {
        Calendar cal = (Calendar) param;
        ps.setTimestamp(parameterIndex, new java.sql.Timestamp(cal.getTime().getTime()), cal);
      } else {
        ps.setObject(parameterIndex, param, Types.TIMESTAMP);
      }
    } else if (sqlType == TYPE_UNKNOWN
        || (sqlType == Types.OTHER
            && "Oracle".equals(ps.getConnection().getMetaData().getDatabaseProductName()))) {
      if (isStringValue(param.getClass())) {
        ps.setString(parameterIndex, param.toString());
      } else if (isDateValue(param.getClass())) {
        ps.setTimestamp(parameterIndex, new java.sql.Timestamp(((java.util.Date) param).getTime()));
      } else if (param instanceof Calendar) {
        Calendar cal = (Calendar) param;
        ps.setTimestamp(parameterIndex, new java.sql.Timestamp(cal.getTime().getTime()), cal);
      } else {
        // Fall back to generic setObject call without SQL type specified.
        ps.setObject(parameterIndex, param);
      }
    } else if (sqlType == Types.NULL) {
      ps.setNull(parameterIndex, sqlType);
    } else {
      // Fall back to generic setObject call with SQL type specified.
      ps.setObject(parameterIndex, param, sqlType);
    }
  }
}
