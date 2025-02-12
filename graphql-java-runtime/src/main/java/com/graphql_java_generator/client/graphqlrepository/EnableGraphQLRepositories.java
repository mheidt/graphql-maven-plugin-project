/**
 * 
 */
package com.graphql_java_generator.client.graphqlrepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({ GraphQLRepositoryProxyConfig.class, GraphQLRepositoryProxyBeansRegistrar.class })
/**
 * This annotation can be applied to some Configuration annotated class with packages to scan interfaces, as below.
 * <code>
&#64;EnableWebServices({
    "a.b.c.webservices",
    "x.y.z.webservices"
}) * </code>
 * 
 * @see https://stackoverflow.com/questions/39507736/dynamic-proxy-bean-with-autowiring-capability
 */
public @interface EnableGraphQLRepositories {

	@AliasFor("basePackages")
	String[] value() default {};

	@AliasFor("value")
	String[] basePackages() default {};
}
