package com.graphql_java_generator.client.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.graphql_java_generator.client.domain.allGraphQLCases.AnotherMutationType;
import com.graphql_java_generator.client.domain.allGraphQLCases.Episode;
import com.graphql_java_generator.client.domain.allGraphQLCases.HumanInput;
import com.graphql_java_generator.client.domain.allGraphQLCases.MyQueryType;
import com.graphql_java_generator.client.domain.allGraphQLCases._extends;
import com.graphql_java_generator.exception.GraphQLRequestExecutionException;
import com.graphql_java_generator.exception.GraphQLRequestPreparationException;

@Execution(ExecutionMode.CONCURRENT)
class AliasesTest {

	HumanInput input;
	Map<String, Object> params = new HashMap<>();

	@BeforeEach
	void setup() {
		input = new HumanInput();
		input.setName("a new name");
		List<Episode> episodes = new ArrayList<>();
		episodes.add(Episode.JEDI);
		episodes.add(Episode.EMPIRE);
		episodes.add(Episode.NEWHOPE);
		input.setAppearsIn(episodes);

		params.put("humanInput", input);
		params.put("value", "the mutation value");
		params.put("anotherValue", "the other mutation value");
	}

	@Test
	@Execution(ExecutionMode.CONCURRENT)
	void testBuild_scalarInputParameters() throws GraphQLRequestPreparationException {
		// Go, go, go
		MyQueryType queryType = new MyQueryType("http://localhost");
		@SuppressWarnings("deprecation")
		AbstractGraphQLRequest graphQLRequest = queryType.getABreakResponseBuilder()
				.withQueryResponseDef("{myAlias     :   \ncase(test: DOUBLE)}").build();

		// Verification
		assertEquals(1, graphQLRequest.query.fields.size());
		QueryField aBreak = graphQLRequest.query.fields.get(0);
		assertEquals("aBreak", aBreak.name);
		assertEquals("aBreak", aBreak.name);
		assertEquals(2, aBreak.fields.size(), " (with the added __typename field)");

		QueryField field = aBreak.fields.get(0);
		assertEquals("case", field.name);
		assertEquals("myAlias", field.alias);
		assertEquals(1, field.inputParameters.size());
		assertEquals("test", field.inputParameters.get(0).getName());
		assertEquals(_extends.DOUBLE, field.inputParameters.get(0).getValue());
	}

	@Test
	@Execution(ExecutionMode.CONCURRENT)
	void testBuild_Partial_createHuman() throws GraphQLRequestPreparationException, GraphQLRequestExecutionException {
		// Preparation
		AnotherMutationType mutationType = new AnotherMutationType("http://localhost/graphql");
		params = new HashMap<>();
		params.put("anotherMutationTypeCreateHumanHuman", input);
		params.put("value", "the mutation value");
		params.put("anotherValue", "the other mutation value");

		// Go, go, go
		@SuppressWarnings("deprecation")
		AbstractGraphQLRequest graphQLRequest = mutationType.getCreateHumanResponseBuilder().withQueryResponseDef(
				"{aliasId:id \taliasName: name aliasAppearsIn :appearsIn aliasFriends : friends {aliasId2:id aliasName2:name}}}")
				.build();

		// Verification
		assertEquals("{\"query\":\"mutation" //
				+ "{createHuman(human:{name:\\\"a new name\\\",appearsIn:[JEDI,EMPIRE,NEWHOPE]})"//
				+ "{aliasId:id aliasName:name aliasAppearsIn:appearsIn aliasFriends:friends{aliasId2:id aliasName2:name __typename} __typename}}" //
				+ "\",\"variables\":null,\"operationName\":null}", //
				graphQLRequest.buildRequest(params));
	}

	@Test
	@Execution(ExecutionMode.CONCURRENT)
	void testBuild_Full_createHuman_withBuilder()
			throws GraphQLRequestPreparationException, GraphQLRequestExecutionException {
		// Preparation
		AnotherMutationType mutationType = new AnotherMutationType("http://localhost/graphql");

		// Go, go, go
		@SuppressWarnings("deprecation")
		AbstractGraphQLRequest graphQLRequest = mutationType.getResponseBuilder().withQueryResponseDef(//
				"mutation { "//
						+ " createHuman (human: &humanInput) @testDirective(value:&value, anotherValue:?anotherValue)   {id name appearsIn friends {id name}}"//
						+ " createHuman2:createHuman (human: &humanInput) @testDirective(value:&value, anotherValue:?anotherValue)   {a1:id a2:name a3:appearsIn a4:friends {a5:id a6:name}}"//
						+ "}"//
		).build();

		// Verification
		assertEquals("{\"query\":\"mutation" //
				+ "{createHuman(human:{name:\\\"a new name\\\",appearsIn:[JEDI,EMPIRE,NEWHOPE]}) @testDirective(value:\\\"the mutation value\\\",anotherValue:\\\"the other mutation value\\\")"//
				+ "{id name appearsIn friends{id name __typename} __typename} " //
				+ "createHuman2:createHuman(human:{name:\\\"a new name\\\",appearsIn:[JEDI,EMPIRE,NEWHOPE]}) @testDirective(value:\\\"the mutation value\\\",anotherValue:\\\"the other mutation value\\\")"//
				+ "{a1:id a2:name a3:appearsIn a4:friends{a5:id a6:name __typename} __typename}}" //
				+ "\",\"variables\":null,\"operationName\":null}", //
				graphQLRequest.buildRequest(params));
	}
}
