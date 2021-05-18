package com.shawn.geektime.homework.user.context;

import java.util.List;

public interface ApplicationContext {
  /** 初始化组件 */
  void init();

  /** 上下文销毁方法 */
  void destroy();

  /**
   * 通过名称查找组件对象
   *
   * @param componentName 组件名称
   * @param <C> 组件对象类型
   * @return 如果找不到返回, <code>null</code>
   */
  <C> C getComponent(String componentName);

  /**
   * 获取所有的组件名称
   *
   * @return
   */
  List<String> getComponentNames();
}
