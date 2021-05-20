package com.shawn.geektime.homework.user.db.annotation;

import com.shawn.geektime.homework.user.db.enums.IdGeneratePolicy;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {

  IdGeneratePolicy policy() default IdGeneratePolicy.AUTO_INCREMENT;
}
