/** Generated by the default template from graphql-java-generator */
package com.graphql_java_generator.client.domain.allGraphQLCases;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.graphql_java_generator.annotation.GraphQLNonScalar;
import com.graphql_java_generator.client.request.ObjectResponse;
import com.graphql_java_generator.client.response.Error;


public class MyQueryTypeRootResponse {

	@JsonProperty("data")
	@GraphQLNonScalar(fieldName = "MyQueryType", graphQLTypeSimpleName = "MyQueryType", javaClass = MyQueryType.class)
	MyQueryType query;

	@JsonProperty("errors")
	@JsonDeserialize(contentAs = Error.class)
	public List<Error> errors;

	@JsonProperty("extensions")
	public JsonNode extensions;

	// This getter is needed for the Json serialization
	public MyQueryType getData() {
		return this.query;
	}

	// This setter is needed for the Json deserialization
	public void setData(MyQueryType query) {
		this.query = query;
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public JsonNode getExtensions() {
		return extensions;
	}

	public void setExtensions(JsonNode extensions) {
		this.extensions = extensions;
	}

}
