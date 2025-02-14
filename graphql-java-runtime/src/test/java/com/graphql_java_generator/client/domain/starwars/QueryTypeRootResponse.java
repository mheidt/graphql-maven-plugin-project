/** Generated by the default template from graphql-java-generator */
package com.graphql_java_generator.client.domain.starwars;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.graphql_java_generator.annotation.GraphQLNonScalar;
import com.graphql_java_generator.client.request.ObjectResponse;
import com.graphql_java_generator.client.response.Error;


public class QueryTypeRootResponse {

	@JsonProperty("data")
	@GraphQLNonScalar(fieldName = "QueryType", graphQLTypeSimpleName = "QueryType", javaClass = QueryType.class)
	QueryType query;

	@JsonProperty("errors")
	@JsonDeserialize(contentAs = Error.class)
	public List<Error> errors;

	@JsonProperty("extensions")
	public JsonNode extensions;

	// This getter is needed for the Json serialization
	public QueryType getData() {
		return this.query;
	}

	// This setter is needed for the Json deserialization
	public void setData(QueryType query) {
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
