/**
 *
 */
package com.graphql_java_generator.customscalars;

import graphql.language.StringValue;
import graphql.schema.*;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * A proposed DateTime scalar, that stores dates with time in String, formatted as: 2019-07-03T20:47:55Z<BR/>
 * In contrast to GraphQLScalarTypeDateTime this class translates to java.time.Instant.
 *
 * @author etienne-sf, mheidt
 */
public class GraphQLScalarTypeInstant {

    /**
     * Custom Scalar for Instant management. It serializes datetimes in String, formatted as: yyyy-MM-dd'T'HH:mm:ss'Z'
     */
    public static GraphQLScalarType DateTime = GraphQLScalarType.newScalar().name("DateTime").description(
            "Custom Scalar for Instant management. It serializes datetimes in String, formatted as: yyyy-MM-dd'T'HH:mm:ss'Z'")
            .coercing(
                    //
                    // Note: String is the way the data is stored in GraphQL queries
                    // Instant is the type while in the java code, either in the client and in the server
                    new Coercing<Instant, String>() {

                        /**
                         * The date pattern, used when exchanging date with this {@link GraphQLScalarType} from and to
                         * the GrahQL Server
                         */
                        final static String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

                        /**
                         * Called to convert a Java object result of a DataFetcher to a valid runtime value for the
                         * scalar type. <br/>
                         * Note : Throw {@link CoercingSerializeException} if there is fundamental
                         * problem during serialisation, don't return null to indicate failure. <br/>
                         * Note : You should not allow {@link RuntimeException}s to come out of your serialize
                         * method, but rather catch them and fire them as
                         * {@link CoercingSerializeException} instead as per the method contract.
                         *
                         * @param input
                         *            java.time.Instant excpected. Is never null.
                         *
                         * @return a serialized value which may be null.
                         *
                         * @throws CoercingSerializeException
                         *             if value input can't be serialized
                         */
                        @Override
                        public String serialize(Object input) throws CoercingSerializeException {
                            if (!(input instanceof Instant)) {
                                throw new CoercingSerializeException(
                                        "Can't format '" + input.toString() + "' to string. java.time.Instant expected.");
                            } else {
                                synchronized (formatter) {
                                    return formatter.format((Instant) input);
                                }
                            }
                        }

                        /**
                         * Called to resolve an input from a query variable into a Java object acceptable for the scalar
                         * type. <br/>
                         * Note : You should not allow {@link RuntimeException}s to come out of your
                         * parseValue method, but rather catch them and fire them as
                         * {@link CoercingParseValueException} instead as per the method contract.
                         *
                         * @param o
                         *           String expected. Is never null.
                         *
                         * @return a parsed value which is never null
                         *
                         * @throws CoercingParseValueException
                         *             if value input can't be parsed
                         */
                        @Override
                        public Instant parseValue(Object o) throws CoercingParseValueException {
                            if (!(o instanceof String)) {
                                throw new CoercingParseValueException(
                                        "Can't parse the '" + o.toString() + "' string to a java.time.Instant");
                            } else {
                                return Instant.parse((String) o);
                            }
                        }

                        /**
                         * Called during query validation to convert a query input AST node into a Java object
                         * acceptable for the scalar type. The input object will be an instance of
                         * {@link graphql.language.Value}. <br/>
                         * Note : You should not allow {@link RuntimeException}s to come out of your
                         * parseLiteral method, but rather catch them and fire them as
                         * {@link CoercingParseLiteralException} instead as per the method contract.
                         *
                         * @param o
                         *        StringValue expected. Is never null.
                         *
                         * @return a parsed value which is never null
                         *
                         * @throws CoercingParseLiteralException
                         *             if input literal can't be parsed
                         */
                        @Override
                        public Instant parseLiteral(Object o) throws CoercingParseLiteralException {
                            // o is an AST, that is: an instance of a class that implements graphql.language.Value
                            if (!(o instanceof StringValue)) {
                                throw new CoercingParseValueException(
                                        "Can't parse the '" + o.toString() + "' string to a Instant");
                            } else {
                                return Instant.parse(((StringValue) o).getValue());
                            }
                        }
                    })
            .build();

}