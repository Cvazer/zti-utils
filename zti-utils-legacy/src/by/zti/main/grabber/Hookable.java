package by.zti.main.grabber;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * This annotation is used to mark class as init point
 */
public @interface Hookable {

    /**
     * Name for init method to look up
     * @return - method name
     */
    String value() default "hook";

}
