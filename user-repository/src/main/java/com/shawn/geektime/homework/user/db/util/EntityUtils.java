package com.shawn.geektime.homework.user.db.util;

import com.shawn.geektime.homework.user.db.entity.EntityColumn;
import com.shawn.geektime.homework.user.db.entity.EntityTable;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;

public class EntityUtils {

  public static EntityTable getEntityTable(Class<?> entityClass) {
    if (!checkIsEntity(entityClass)) {
      return null;
    }
    EntityTable table = new EntityTable();
    Table tableAnnotation = entityClass.getAnnotation(Table.class);
    String tableName;
    if (tableAnnotation == null || StringUtils.isBlank(tableAnnotation.name())) {
      tableName = entityClass.getSimpleName().toLowerCase();
    } else {
      tableName = tableAnnotation.name();
    }
    table.setTableName(tableName);
    List<EntityColumn> entityColumns = getEntityColumns(entityClass);
    table.setColumns(
        entityColumns.stream().map(EntityColumn::getColumn).collect(Collectors.toList()));
    table.setPks(
        entityColumns
            .stream()
            .filter(EntityColumn::isPk)
            .map(EntityColumn::getColumn)
            .collect(Collectors.toList()));
    table.setEntityColumnMap(
        entityColumns
            .stream()
            .collect(Collectors.toMap(EntityColumn::getProperty, entityColumn -> entityColumn)));
    table.setEntityColumns(entityColumns);
    return table;
  }

  public static boolean checkIsEntity(Class<?> entityClass) {
    if (null == entityClass || !entityClass.isAnnotationPresent(Entity.class)) {
      return false;
    }
    return true;
  }

  public static List<EntityColumn> getEntityColumns(Class<?> entityClass) {
    if (null == entityClass || !entityClass.isAnnotationPresent(Entity.class)) {
      return null;
    }
    Field[] fields = entityClass.getDeclaredFields();
    List<EntityColumn> columns = new ArrayList<>();

    try {
      BeanInfo beanInfo = Introspector.getBeanInfo(entityClass, Object.class);
      PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
      List<PropertyMethodMapping> propertyMethodMappings = new ArrayList<>();
      for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();
        String name = propertyDescriptor.getName();
        PropertyMethodMapping propertyMethodMapping = new PropertyMethodMapping();
        propertyMethodMapping.setProperty(name);
        propertyMethodMapping.setReadMethod(readMethod);
        propertyMethodMapping.setWriteMethod(writeMethod);
        propertyMethodMappings.add(propertyMethodMapping);
      }
      for (Field field : fields) {
        // exclude static and transient fields
        if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
          continue;
        }
        EntityColumn entityColumn = new EntityColumn();
        Column column = field.getAnnotation(Column.class);
        String columnName;
        if (Objects.isNull(column) || StringUtils.isBlank(column.name())) {
          columnName = field.getName();
        } else {
          columnName = column.name();
        }
        PropertyMethodMapping mapping =
            propertyMethodMappings
                .stream()
                .filter(
                    propertyMethodMapping ->
                        propertyMethodMapping.getProperty().equals(field.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(""));
        entityColumn.setColumn(columnName);
        entityColumn.setProperty(field.getName());
        entityColumn.setPk(field.isAnnotationPresent(Id.class));
        entityColumn.setJavaType(field.getType());
        entityColumn.setReadMethod(mapping.getReadMethod());
        entityColumn.setWriteMethod(mapping.getWriteMethod());
        entityColumn.setJdbcType(
            JDBCType.valueOf(StatementCreatorUtils.javaTypeToSqlParameterType(field.getType())));
        if (field.isAnnotationPresent(Id.class)) {
          GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
          if (Objects.isNull(generatedValue)) {
            entityColumn.setGenerationType(GenerationType.IDENTITY);
          } else {
            entityColumn.setGenerationType(generatedValue.strategy());
          }
        }
        columns.add(entityColumn);
      }
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }

    return columns;
  }

  private static class PropertyMethodMapping {
    private String property;
    private Method readMethod;
    private Method writeMethod;

    public String getProperty() {
      return property;
    }

    public void setProperty(String property) {
      this.property = property;
    }

    public Method getReadMethod() {
      return readMethod;
    }

    public void setReadMethod(Method readMethod) {
      this.readMethod = readMethod;
    }

    public Method getWriteMethod() {
      return writeMethod;
    }

    public void setWriteMethod(Method writeMethod) {
      this.writeMethod = writeMethod;
    }
  }
}
