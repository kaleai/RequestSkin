package kale.net.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kale.net.http.processor.HttpProcessor;

/**
 * @author Jack Tony
 * @date 2015/8/16
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface HttpGet {

    String url();

    Class<?> model() default HttpProcessor.class;
}
