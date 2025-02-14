package org.allGraphQLCases.demo;

import java.util.Arrays;
import java.util.List;

import org.allGraphQLCases.client.CharacterInput;
import org.allGraphQLCases.client.Episode;
import org.allGraphQLCases.client.util.MyQueryTypeExecutor;
import org.allGraphQLCases.demo.impl.PartialDirectQueries;
import org.allGraphQLCases.demo.impl.PartialPreparedQueries;
import org.allGraphQLCases.demo.subscription.ExecSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.UnAuthenticatedServerOAuth2AuthorizedClientRepository;

import com.graphql_java_generator.client.GraphQLConfiguration;
import com.graphql_java_generator.exception.GraphQLRequestExecutionException;
import com.graphql_java_generator.exception.GraphQLRequestPreparationException;

/**
 * The main class, which executes the same partialQueries, built by three different methods. See
 * {@link PartialDirectQueries}, {@link PartialPreparedQueries}, {@link PartialWithBuilder}<BR/>
 * 
 * A sample query, to get an OAuth token:
 * 
 * <pre>
curl -u "clientId:secret" -X POST "http://localhost:8181/oauth/token?grant_type=client_credentials" --noproxy "*" -i
 * </pre>
 * 
 * Then, reuse the previous token in the next query:
 * 
 * <pre>
curl -i -X POST "http://localhost:8180/graphql" --noproxy "*" -H "Authorization: Bearer 8c8e4a5b-d903-4ed6-9738-6f7f364b87ec"
 * </pre>
 * 
 * And, to check the token:
 * 
 * <pre>
curl -i -X GET "http://localhost:8181/profile/me" --noproxy "*" -H "Authorization: Bearer 8c8e4a5b-d903-4ed6-9738-6f7f364b87ec"
 * </pre>
 * 
 * @author etienne-sf
 * @see https://michalgebauer.github.io/spring-graphql-security/
 */
@SuppressWarnings("deprecation")
@SpringBootApplication(scanBasePackageClasses = { Main.class, GraphQLConfiguration.class, MyQueryTypeExecutor.class })
public class Main implements CommandLineRunner {

	@Autowired
	PartialDirectQueries partialDirectQueries;
	@Autowired
	PartialPreparedQueries partialPreparedQueries;
	@Autowired
	ExecSubscription execSubscription;

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	/**
	 * This method is started by Spring, once the Spring context has been loaded. This is run, as this class implements
	 * {@link CommandLineRunner}
	 */
	@Override
	public void run(String... args) throws Exception {

		// Execution of two ways of calling the GraphQL server

		System.out.println("============================================================================");
		System.out.println("======= SIMPLEST WAY: DIRECT QUERIES =======================================");
		System.out.println("============================================================================");
		execOne(partialDirectQueries);

		System.out.println("============================================================================");
		System.out.println("======= MOST SECURE WAY: PREPARED QUERIES ==================================");
		System.out.println("============================================================================");
		execOne(partialPreparedQueries);

		System.out.println("============================================================================");
		System.out.println("======= EXECUTING A SUBSCRIPTION ===========================================");
		System.out.println("============================================================================");
		execSubscription.exec();

		System.out.println("");
		System.out.println("");
		System.out.println("Sample application finished ... enjoy !    :)");
		System.out.println("");
		System.out.println("Please take a look at the other samples, for other use cases");
		System.out.println(
				"You'll find more information on the plugin's web site: https://graphql-maven-plugin-project.graphql-java-generator.com/");
	}

	public void execOne(PartialQueries client)
			throws GraphQLRequestPreparationException, GraphQLRequestExecutionException {

		try {

			System.out.println("----------------  withoutParameters  ----------------------------------------------");
			System.out.println(client.withoutParameters());

			System.out.println("---------------- withOneOptionalParam -------------------------------------------");
			CharacterInput ci1 = CharacterInput.builder().withName("my name")
					.withAppearsIn(Arrays.asList(Episode.JEDI, Episode.NEWHOPE)).withType("Droid").build();
			System.out.println(client.withOneOptionalParam(ci1));

			System.out.println("---------------- withOneMandatoryParam ------------------------------------------");
			CharacterInput ci2 = CharacterInput.builder().withName("my other name").withAppearsIn(Arrays.asList())
					.withType("Human").build();
			System.out.println(client.withOneMandatoryParam(ci2));

			System.out.println("---------------- withEnum -------------------------------------------------------");
			System.out.println(client.withEnum(Episode.NEWHOPE));

			System.out.println("---------------- withList -------------------------------------------------------");
			List<CharacterInput> chars = Arrays.asList(ci1, ci2);
			System.out.println(client.withList("The name", chars));

		} catch (javax.ws.rs.ProcessingException e) {
			throw new RuntimeException(
					"Please start the server from the project graphql-maven-plugin-samples-StarWars-server, before executing the client part",
					e);
		}
	}

	@Bean
	ServerOAuth2AuthorizedClientExchangeFilterFunction serverOAuth2AuthorizedClientExchangeFilterFunction(
			ReactiveClientRegistrationRepository clientRegistrations) {
		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
				clientRegistrations, new UnAuthenticatedServerOAuth2AuthorizedClientRepository());
		oauth.setDefaultClientRegistrationId("provider_test");
		return oauth;
	}

}
