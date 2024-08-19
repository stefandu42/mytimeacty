package mytimeacty.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to specify roles allowed to access a method.
 * This annotation is used to restrict method access based on user roles.
 * 
 * @Target(ElementType.METHOD) : This annotation can only be applied to methods.
 * @Retention(RetentionPolicy.RUNTIME) : This annotation is retained at runtime, 
 * allowing it to be accessed via reflection.
 * 
 * @param value An array of strings representing the allowed roles. 
 * Example: {"admin", "chief"}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RolesAllowed {
    String[] value();
}
