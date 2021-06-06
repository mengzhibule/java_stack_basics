package com.shawn.geektime.homework.user.db;

import com.shawn.geektime.homework.user.entity.User;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import org.junit.Test;

public class BeanInfoTests {

  private static final Logger LOGGER = Logger.getLogger(BeanInfoTests.class.getName());

  @Test
  public void test_user_bean_info() {
    try {
      BeanInfo beanInfo = Introspector.getBeanInfo(User.class, Object.class);
      PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
      for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
        Method writeMethod = propertyDescriptor.getWriteMethod();
        String name = propertyDescriptor.getName();
        String writeMethodName = writeMethod.getName();
        Method readMethod = propertyDescriptor.getReadMethod();
        String readMethodName = readMethod.getName();
        LOGGER.info(
            String.format(
                "propertyName: [%s], writeMethodName: [%s], readMethodName: [%s]",
                name, writeMethodName, readMethodName));
      }
    } catch (IntrospectionException e) {
      throw new RuntimeException(e);
    }
  }
}
