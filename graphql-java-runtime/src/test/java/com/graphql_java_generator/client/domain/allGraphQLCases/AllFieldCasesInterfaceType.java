/** Generated by the default template from graphql-java-generator */
package com.graphql_java_generator.client.domain.allGraphQLCases;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.graphql_java_generator.GraphQLField;
import com.graphql_java_generator.annotation.GraphQLInputParameters;
import com.graphql_java_generator.annotation.GraphQLNonScalar;
import com.graphql_java_generator.annotation.GraphQLObjectType;
import com.graphql_java_generator.annotation.GraphQLScalar;
import java.util.List;

/**
 *  A concrete class that just implements the above interface
 *
 * @author generated by graphql-java-generator
 * @see <a href="https://github.com/graphql-java-generator/graphql-java-generator">https://github.com/graphql-java-generator/graphql-java-generator</a>
 */
@GraphQLObjectType("AllFieldCasesInterfaceType")
public class AllFieldCasesInterfaceType 
	implements AllFieldCasesInterface, WithID{

	public AllFieldCasesInterfaceType(){
		// No action
	}

	@JsonProperty("id")
	@GraphQLScalar(fieldName = "id", graphQLTypeSimpleName = "ID", javaClass = String.class)
	String id;


	@JsonProperty("name")
	@GraphQLScalar(fieldName = "name", graphQLTypeSimpleName = "String", javaClass = String.class)
	String name;


	@JsonProperty("forname")
	@GraphQLInputParameters(names = {"uppercase", "textToAppendToTheForname"}, types = {"Boolean", "String"}, mandatories = {false, false}, listDepths = {0, 0}, itemsMandatory = {false, false})
	@GraphQLScalar(fieldName = "forname", graphQLTypeSimpleName = "String", javaClass = String.class)
	String forname;


	@JsonProperty("age")
	@JsonDeserialize(using = CustomJacksonDeserializers.Long.class)
	@GraphQLScalar(fieldName = "age", graphQLTypeSimpleName = "Long", javaClass = Long.class)
	Long age;


	@JsonProperty("nbComments")
	@GraphQLScalar(fieldName = "nbComments", graphQLTypeSimpleName = "Int", javaClass = Integer.class)
	Integer nbComments;


	@JsonProperty("comments")
	@JsonDeserialize(using = CustomJacksonDeserializers.ListString.class)
	@GraphQLScalar(fieldName = "comments", graphQLTypeSimpleName = "String", javaClass = String.class)
	List<String> comments;


	@JsonProperty("booleans")
	@JsonDeserialize(using = CustomJacksonDeserializers.ListBoolean.class)
	@GraphQLScalar(fieldName = "booleans", graphQLTypeSimpleName = "Boolean", javaClass = Boolean.class)
	List<Boolean> booleans;


	@JsonProperty("aliases")
	@JsonDeserialize(using = CustomJacksonDeserializers.ListString.class)
	@GraphQLScalar(fieldName = "aliases", graphQLTypeSimpleName = "String", javaClass = String.class)
	List<String> aliases;


	@JsonProperty("planets")
	@JsonDeserialize(using = CustomJacksonDeserializers.ListString.class)
	@GraphQLScalar(fieldName = "planets", graphQLTypeSimpleName = "String", javaClass = String.class)
	List<String> planets;


	/**
	 *  @RelayConnection should be but is not applied on the friends field (this allows the unit test that checks that a warning is correctly generated)
	 */
	@JsonProperty("friends")
	@JsonDeserialize(using = CustomJacksonDeserializers.ListHuman.class)
	@GraphQLNonScalar(fieldName = "friends", graphQLTypeSimpleName = "Human", javaClass = Human.class)
	List<Human> friends;


	@JsonProperty("oneWithIdSubType")
	@GraphQLNonScalar(fieldName = "oneWithIdSubType", graphQLTypeSimpleName = "AllFieldCasesWithIdSubtype", javaClass = AllFieldCasesWithIdSubtype.class)
	AllFieldCasesWithIdSubtype oneWithIdSubType;


	@JsonProperty("listWithIdSubTypes")
	@JsonDeserialize(using = CustomJacksonDeserializers.ListAllFieldCasesWithIdSubtype.class)
	@GraphQLInputParameters(names = {"nbItems", "uppercaseName", "textToAppendToTheForname"}, types = {"Int", "Boolean", "String"}, mandatories = {true, false, false}, listDepths = {0, 0, 0}, itemsMandatory = {false, false, false})
	@GraphQLNonScalar(fieldName = "listWithIdSubTypes", graphQLTypeSimpleName = "AllFieldCasesWithIdSubtype", javaClass = AllFieldCasesWithIdSubtype.class)
	List<AllFieldCasesWithIdSubtype> listWithIdSubTypes;


	@JsonProperty("oneWithoutIdSubType")
	@GraphQLInputParameters(names = {"input"}, types = {"FieldParameterInput"}, mandatories = {false}, listDepths = {0}, itemsMandatory = {false})
	@GraphQLNonScalar(fieldName = "oneWithoutIdSubType", graphQLTypeSimpleName = "AllFieldCasesWithoutIdSubtype", javaClass = AllFieldCasesWithoutIdSubtype.class)
	AllFieldCasesWithoutIdSubtype oneWithoutIdSubType;


	@JsonProperty("listWithoutIdSubTypes")
	@JsonDeserialize(using = CustomJacksonDeserializers.ListAllFieldCasesWithoutIdSubtype.class)
	@GraphQLInputParameters(names = {"nbItems", "input", "textToAppendToTheForname"}, types = {"Int", "FieldParameterInput", "String"}, mandatories = {true, false, false}, listDepths = {0, 0, 0}, itemsMandatory = {false, false, false})
	@GraphQLNonScalar(fieldName = "listWithoutIdSubTypes", graphQLTypeSimpleName = "AllFieldCasesWithoutIdSubtype", javaClass = AllFieldCasesWithoutIdSubtype.class)
	List<AllFieldCasesWithoutIdSubtype> listWithoutIdSubTypes;


	@JsonProperty("__typename")
	@GraphQLScalar(fieldName = "__typename", graphQLTypeSimpleName = "String", javaClass = String.class)
	String __typename;



	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setForname(String forname) {
		this.forname = forname;
	}

	public String getForname() {
		return forname;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public Long getAge() {
		return age;
	}

	public void setNbComments(Integer nbComments) {
		this.nbComments = nbComments;
	}

	public Integer getNbComments() {
		return nbComments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	public List<String> getComments() {
		return comments;
	}

	public void setBooleans(List<Boolean> booleans) {
		this.booleans = booleans;
	}

	public List<Boolean> getBooleans() {
		return booleans;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void setPlanets(List<String> planets) {
		this.planets = planets;
	}

	public List<String> getPlanets() {
		return planets;
	}

	/**
	 *  @RelayConnection should be but is not applied on the friends field (this allows the unit test that checks that a warning is correctly generated)
	 */
	public void setFriends(List<Human> friends) {
		this.friends = friends;
	}

	/**
	 *  @RelayConnection should be but is not applied on the friends field (this allows the unit test that checks that a warning is correctly generated)
	 */
	public List<Human> getFriends() {
		return friends;
	}

	public void setOneWithIdSubType(AllFieldCasesWithIdSubtype oneWithIdSubType) {
		this.oneWithIdSubType = oneWithIdSubType;
	}

	public AllFieldCasesWithIdSubtype getOneWithIdSubType() {
		return oneWithIdSubType;
	}

	public void setListWithIdSubTypes(List<AllFieldCasesWithIdSubtype> listWithIdSubTypes) {
		this.listWithIdSubTypes = listWithIdSubTypes;
	}

	public List<AllFieldCasesWithIdSubtype> getListWithIdSubTypes() {
		return listWithIdSubTypes;
	}

	public void setOneWithoutIdSubType(AllFieldCasesWithoutIdSubtype oneWithoutIdSubType) {
		this.oneWithoutIdSubType = oneWithoutIdSubType;
	}

	public AllFieldCasesWithoutIdSubtype getOneWithoutIdSubType() {
		return oneWithoutIdSubType;
	}

	public void setListWithoutIdSubTypes(List<AllFieldCasesWithoutIdSubtype> listWithoutIdSubTypes) {
		this.listWithoutIdSubTypes = listWithoutIdSubTypes;
	}

	public List<AllFieldCasesWithoutIdSubtype> getListWithoutIdSubTypes() {
		return listWithoutIdSubTypes;
	}

	public void set__typename(String __typename) {
		this.__typename = __typename;
	}

	public String get__typename() {
		return __typename;
	}

    public String toString() {
        return "AllFieldCasesInterfaceType {"
				+ "id: " + id
				+ ", "
				+ "name: " + name
				+ ", "
				+ "forname: " + forname
				+ ", "
				+ "age: " + age
				+ ", "
				+ "nbComments: " + nbComments
				+ ", "
				+ "comments: " + comments
				+ ", "
				+ "booleans: " + booleans
				+ ", "
				+ "aliases: " + aliases
				+ ", "
				+ "planets: " + planets
				+ ", "
				+ "friends: " + friends
				+ ", "
				+ "oneWithIdSubType: " + oneWithIdSubType
				+ ", "
				+ "listWithIdSubTypes: " + listWithIdSubTypes
				+ ", "
				+ "oneWithoutIdSubType: " + oneWithoutIdSubType
				+ ", "
				+ "listWithoutIdSubTypes: " + listWithoutIdSubTypes
				+ ", "
				+ "__typename: " + __typename
        		+ "}";
    }

    /**
	 * Enum of field names
	 */
	 public static enum Field implements GraphQLField {
		Id("id"),
		Name("name"),
		Forname("forname"),
		Age("age"),
		NbComments("nbComments"),
		Comments("comments"),
		Booleans("booleans"),
		Aliases("aliases"),
		Planets("planets"),
		Friends("friends"),
		OneWithIdSubType("oneWithIdSubType"),
		ListWithIdSubTypes("listWithIdSubTypes"),
		OneWithoutIdSubType("oneWithoutIdSubType"),
		ListWithoutIdSubTypes("listWithoutIdSubTypes"),
		__typename("__typename");

		private String fieldName;

		Field(String fieldName) {
			this.fieldName = fieldName;
		}

		public String getFieldName() {
			return fieldName;
		}

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
		private String forname;
		private Long age;
		private Integer nbComments;
		private List<String> comments;
		private List<Boolean> booleans;
		private List<String> aliases;
		private List<String> planets;
		private List<Human> friends;
		private AllFieldCasesWithIdSubtype oneWithIdSubType;
		private List<AllFieldCasesWithIdSubtype> listWithIdSubTypes;
		private AllFieldCasesWithoutIdSubtype oneWithoutIdSubType;
		private List<AllFieldCasesWithoutIdSubtype> listWithoutIdSubTypes;


		public Builder withId(String id) {
			this.id = id;
			return this;
		}
		public Builder withName(String name) {
			this.name = name;
			return this;
		}
		public Builder withForname(String forname) {
			this.forname = forname;
			return this;
		}
		public Builder withAge(Long age) {
			this.age = age;
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
		public Builder withBooleans(List<Boolean> booleans) {
			this.booleans = booleans;
			return this;
		}
		public Builder withAliases(List<String> aliases) {
			this.aliases = aliases;
			return this;
		}
		public Builder withPlanets(List<String> planets) {
			this.planets = planets;
			return this;
		}
		public Builder withFriends(List<Human> friends) {
			this.friends = friends;
			return this;
		}
		public Builder withOneWithIdSubType(AllFieldCasesWithIdSubtype oneWithIdSubType) {
			this.oneWithIdSubType = oneWithIdSubType;
			return this;
		}
		public Builder withListWithIdSubTypes(List<AllFieldCasesWithIdSubtype> listWithIdSubTypes) {
			this.listWithIdSubTypes = listWithIdSubTypes;
			return this;
		}
		public Builder withOneWithoutIdSubType(AllFieldCasesWithoutIdSubtype oneWithoutIdSubType) {
			this.oneWithoutIdSubType = oneWithoutIdSubType;
			return this;
		}
		public Builder withListWithoutIdSubTypes(List<AllFieldCasesWithoutIdSubtype> listWithoutIdSubTypes) {
			this.listWithoutIdSubTypes = listWithoutIdSubTypes;
			return this;
		}

		public AllFieldCasesInterfaceType build() {
			AllFieldCasesInterfaceType _object = new AllFieldCasesInterfaceType();
			_object.setId(id);
			_object.setName(name);
			_object.setForname(forname);
			_object.setAge(age);
			_object.setNbComments(nbComments);
			_object.setComments(comments);
			_object.setBooleans(booleans);
			_object.setAliases(aliases);
			_object.setPlanets(planets);
			_object.setFriends(friends);
			_object.setOneWithIdSubType(oneWithIdSubType);
			_object.setListWithIdSubTypes(listWithIdSubTypes);
			_object.setOneWithoutIdSubType(oneWithoutIdSubType);
			_object.setListWithoutIdSubTypes(listWithoutIdSubTypes);
			_object.set__typename("AllFieldCasesInterfaceType");
			return _object;
		}
	}
}
