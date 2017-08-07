package com.newland.base.dao.ann;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface Column {
	String name() default "";

	boolean primaryKey() default false;
	
	
	int len() default 10;
	
	
	boolean unique() default false;
}
