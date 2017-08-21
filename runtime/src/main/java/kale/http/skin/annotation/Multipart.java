package kale.http.skin.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by YLM on 2017/8/15.
 */
@Target(METHOD)
@Retention(SOURCE)
public @interface Multipart {
}
