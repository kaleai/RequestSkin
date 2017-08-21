package kale.http.skin.annotation.parameter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;
/**
 * Created by YLM on 2017/8/15.
 */
@Target(PARAMETER)
@Retention(SOURCE)
public @interface Part {
    String value() default "";

    String filename() default "";

    String mediaType() default "image/png";
}
