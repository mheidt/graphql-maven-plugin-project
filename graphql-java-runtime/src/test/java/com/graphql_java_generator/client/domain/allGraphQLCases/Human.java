/** Generated by the default template from graphql-java-generator */
package com.graphql_java_generator.client.domain.allGraphQLCases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.graphql_java_generator.GraphQLField;
import com.graphql_java_generator.annotation.GraphQLInputParameters;
import com.graphql_java_generator.annotation.GraphQLNonScalar;
import com.graphql_java_generator.annotation.GraphQLObjectType;
import com.graphql_java_generator.annotation.GraphQLScalar;
import com.graphql_java_generator.exception.GraphQLRequestExecutionException;

/**
 *
 * @author generated by graphql-java-generator
 * @see <a href=
 *      "https://github.com/graphql-java-generator/graphql-java-generator">https://github.com/graphql-java-generator/graphql-java-generator</a>
 */
@GraphQLObjectType("Human")
public class Human implements Character, Commented, WithID, AnyCharacter {

	public Map<String, Object> aliasValues = new HashMap<>();

	public void setAliasValue(String key, Object value) {
		aliasValues.put(key, value);
	}

	/**
	 * Retrieves the value for the given alias, as it has been received for this object in the GraphQL response. <BR/>
	 * This method <B>should not be used for Custom Scalars</B>, as the parser doesn't know if this alias is a custom
	 * scalar, and which custom scalar to use at deserialization time. In most case, a value will then be provided by
	 * this method with a basis json deserialization, but this value won't be the proper custom scalar value.
	 * 
	 * @param alias
	 * @return
	 * @throws GraphQLRequestExecutionException
	 *             If the value can not be parsed
	 */
	public Object getAliasValue(String alias) throws GraphQLRequestExecutionException {
		Object value = aliasValues.get(alias);
		if (value instanceof GraphQLRequestExecutionException)
			throw (GraphQLRequestExecutionException) value;
		else
			return value;
	}

	public Human() {
		// No action
	}

	@JsonProperty("id")
	@GraphQLScalar(fieldName = "id", graphQLTypeSimpleName = "ID", javaClass = String.class)
	String id;

	@JsonProperty("name")
	@GraphQLInputParameters(names = { "uppercase" }, types = { "Boolean" }, mandatories = { false }, listDepths = {
			0 }, itemsMandatory = { false })
	@GraphQLScalar(fieldName = "name", graphQLTypeSimpleName = "String", javaClass = String.class)
	String name;

	@JsonProperty("bestFriend")
	@GraphQLNonScalar(fieldName = "bestFriend", graphQLTypeSimpleName = "Character", javaClass = Character.class)
	Character bestFriend;

	@JsonProperty("friends")
	@JsonDeserialize(using = CustomJacksonDeserializers.ListCharacter.class)
	@GraphQLNonScalar(fieldName = "friends", graphQLTypeSimpleName = "Character", javaClass = Character.class)
	List<Character> friends;

	@JsonProperty("nbComments")
	@GraphQLScalar(fieldName = "nbComments", graphQLTypeSimpleName = "Int", javaClass = Integer.class)
	Integer nbComments;

	@JsonProperty("comments")
	@JsonDeserialize(using = CustomJacksonDeserializers.ListString.class)
	@GraphQLScalar(fieldName = "comments", graphQLTypeSimpleName = "String", javaClass = String.class)
	List<String> comments;

	@JsonProperty("appearsIn")
	@JsonDeserialize(using = CustomJacksonDeserializers.ListEpisode.class)
	@GraphQLScalar(fieldName = "appearsIn", graphQLTypeSimpleName = "Episode", javaClass = Episode.class)
	List<Episode> appearsIn;

	@JsonProperty("homePlanet")
	@GraphQLScalar(fieldName = "homePlanet", graphQLTypeSimpleName = "String", javaClass = String.class)
	String homePlanet;

	@JsonProperty("__typename")
	@GraphQLScalar(fieldName = "__typename", graphQLTypeSimpleName = "String", javaClass = String.class)
	String __typename;

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setBestFriend(Character bestFriend) {
		this.bestFriend = bestFriend;
	}

	public Character getBestFriend() {
		return bestFriend;
	}

	@Override
	public void setFriends(List<Character> friends) {
		this.friends = friends;
	}

	@Override
	public List<Character> getFriends() {
		return friends;
	}

	@Override
	public void setNbComments(Integer nbComments) {
		this.nbComments = nbComments;
	}

	@Override
	public Integer getNbComments() {
		return nbComments;
	}

	@Override
	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	@Override
	public List<String> getComments() {
		return comments;
	}

	@Override
	public void setAppearsIn(List<Episode> appearsIn) {
		this.appearsIn = appearsIn;
	}

	@Override
	public List<Episode> getAppearsIn() {
		return appearsIn;
	}

	public void setHomePlanet(String homePlanet) {
		this.homePlanet = homePlanet;
	}

	public String getHomePlanet() {
		return homePlanet;
	}

	@Override
	public void set__typename(String __typename) {
		this.__typename = __typename;
	}

	@Override
	public String get__typename() {
		return __typename;
	}

	@Override
	public String toString() {
		return "Human {" + "id: " + id + ", " + "name: " + name + ", " + "bestFriend: " + bestFriend + ", "
				+ "friends: " + friends + ", " + "nbComments: " + nbComments + ", " + "comments: " + comments + ", "
				+ "appearsIn: " + appearsIn + ", " + "homePlanet: " + homePlanet + ", " + "__typename: " + __typename
				+ "}";
	}

	/**
	 * Enum of field names
	 */
	public static enum Field implements GraphQLField {
		Id("id"), Name("name"), BestFriend("bestFriend"), Friends("friends"), NbComments("nbComments"), Comments(
				"comments"), AppearsIn("appearsIn"), HomePlanet("homePlanet"), __typename("__typename");

		private String fieldName;

		Field(String fieldName) {
			this.fieldName = fieldName;
		}

		@Override
		public String getFieldName() {
			return fieldName;
		}

		@Override
		public Class<?> getGraphQLType() {
			return this.getClass().getDeclaringClass();
		}

	}

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder
	 */
	public static class Builder {
		private String id;
		private String name;
		private Character bestFriend;
		private List<Character> friends;
		private Integer nbComments;
		private List<String> comments;
		private List<Episode> appearsIn;
		private String homePlanet;

		public Builder withId(String id) {
			this.id = id;
			return this;
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withBestFriend(Character bestFriend) {
			this.bestFriend = bestFriend;
			return this;
		}

		public Builder withFriends(List<Character> friends) {
			this.friends = friends;
			return this;
		}

		public Builder withNbComments(Integer nbComments) {
			this.nbComments = nbComments;
			return this;
		}

		public Builder withComments(List<String> comments) {
			this.comments = comments;
			return this;
		}

		public Builder withAppearsIn(List<Episode> appearsIn) {
			this.appearsIn = appearsIn;
			return this;
		}

		public Builder withHomePlanet(String homePlanet) {
			this.homePlanet = homePlanet;
			return this;
		}

		public Human build() {
			Human _object = new Human();
			_object.setId(id);
			_object.setName(name);
			_object.setBestFriend(bestFriend);
			_object.setFriends(friends);
			_object.setNbComments(nbComments);
			_object.setComments(comments);
			_object.setAppearsIn(appearsIn);
			_object.setHomePlanet(homePlanet);
			_object.set__typename("Human");
			return _object;
		}
	}
}
