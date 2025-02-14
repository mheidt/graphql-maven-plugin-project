/**
 * 
 */
package com.graphql_java_generator.client.graphqlrepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.graphql_java_generator.annotation.RequestType;
import com.graphql_java_generator.client.GraphQLMutationExecutor;
import com.graphql_java_generator.client.GraphQLQueryExecutor;
import com.graphql_java_generator.client.GraphQLSubscriptionExecutor;
import com.graphql_java_generator.client.request.AbstractGraphQLRequest;
import com.graphql_java_generator.client.request.ObjectResponse;
import com.graphql_java_generator.exception.GraphQLRequestExecutionException;
import com.graphql_java_generator.exception.GraphQLRequestPreparationException;
import com.graphql_java_generator.util.GraphqlUtils;

/**
 * This class is responsible to execute the method call to {@link GraphQLRepository}.
 * 
 * @author etienne-sf
 */
public class GraphQLRepositoryInvocationHandler<T> implements InvocationHandler {

	/** Logger for this class */
	private static Logger logger = LoggerFactory.getLogger(GraphQLRepositoryInvocationHandler.class);

	static GraphqlUtils graphqlUtils = GraphqlUtils.graphqlUtils;

	/** This class contains the data for each method of the T interface */
	class RegisteredMethod {
		/** The method that is registered into this object */
		Method method;

		/** True if this request is a Full Request, and False if it's a Partial Request */
		boolean fullRequest;

		/**
		 * The request string, as it has been provided in the {@link PartialRequest} or {@link FullRequest} annotation
		 */
		String request;

		/**
		 * The request name, as it has been provided in the {@link PartialRequest} or {@link FullRequest} annotation, or
		 * by the method name if it wasn't provided in the annotation.
		 */
		String requestName;

		/** The kind of request: Query, Mutation or Subscription */
		RequestType requestType;

		/** The method name, useful for logging when an error occurs */
		String executorMethodName;

		/** The name of the method that returns the GraphQLRequest. It depends if this is a partial or a full request */
		String executorGetGraphQLRequestMethodName;

		/** The query/mutation/subscription executor instance that will be called to execute this method */
		Object executor;

		/** The query/mutation/subscription executor method that must be will be called to execute this method */
		Method executorMethod;

		/**
		 * The GraphQLRequest, calculated from the request provided in the {@link PartialRequest} or {@link FullRequest}
		 * annotation
		 */
		AbstractGraphQLRequest graphQLRequest;

		/** The list of parameters of the executor method */
		public Class<?>[] executorParameterTypes;

		/**
		 * The list of registered parameters from the repository method. This list allows to manage the BindParameter
		 * and BindVariable annotated parameters
		 */
		List<RegisteredParameter> registeredParameters = new ArrayList<>();
	}

	class RegisteredParameter {
		/**
		 * The name of the parameter, as it appears in the definition of the method, in the {@link GraphQLRepository}
		 * interface
		 */
		String name;

		/** The Java type of this parameter, as defined in the {@link GraphQLRepository} interface */
		public Class<?> type;

		/**
		 * The name of the bind parameter or GraphQL variable, as it appears in the GraphQL request, either as $name,
		 * ?name or $name
		 */
		String bindParameterName;
	}

	final Class<T> repositoryInterface;
	final T proxyInstance;
	final GraphQLQueryExecutor queryExecutor;
	final GraphQLMutationExecutor mutationExecutor;
	final GraphQLSubscriptionExecutor subscriptionExecutor;

	Map<Method, RegisteredMethod> registeredMethods = new HashMap<>();

	/**
	 * This constructor provides the query, mutation and subscription that have been defined in the GraphQL schema. The
	 * mutation and subscription are optional in the GraphQL schema, so these two executors may be null.
	 * 
	 * @param repositoryInterface
	 *            The {@link GraphQLRepository} interface, that this {@link InvocationHandler} has to manage. It is
	 *            mandatory.
	 * @param queryExecutor
	 *            The GraphQL query executor. It is mandatory.
	 * @param mutationExecutor
	 *            The GraphQL mutation executor. It may be null (only if no mutation type is provided in the GraphQL
	 *            schema).
	 * @param subscriptionExecutor
	 *            The GraphQL subscription executor. It may be null (only if no subscription type is provided in the
	 *            GraphQL schema).
	 * @throws GraphQLRequestPreparationException
	 */
	@Autowired
	public GraphQLRepositoryInvocationHandler(Class<T> repositoryInterface, GraphQLQueryExecutor queryExecutor,
			GraphQLMutationExecutor mutationExecutor, GraphQLSubscriptionExecutor subscriptionExecutor)
			throws GraphQLRequestPreparationException {
		if (repositoryInterface == null) {
			throw new NullPointerException("'repositoryInterface' may not be null");
		}
		if (!repositoryInterface.isInterface()) {
			throw new NullPointerException("The 'repositoryInterface' (" + repositoryInterface.getName()
					+ ") must be an interface, but it is not");
		}
		if (repositoryInterface.getAnnotation(GraphQLRepository.class) == null) {
			throw new GraphQLRequestPreparationException(
					"This InvocationHandler may only be called for GraphQL repositories. "
							+ "GraphQL repositories must be annotated with the 'com.graphql_java_generator.annotation.GraphQLRepository' annotation. "
							+ "But the '" + repositoryInterface.getName() + "' is not.");
		}
		if (queryExecutor == null) {
			throw new NullPointerException("'queryExecutor' may not be null");
		}

		// All basic tests are Ok. Let's go
		this.repositoryInterface = repositoryInterface;
		this.queryExecutor = queryExecutor;
		this.mutationExecutor = mutationExecutor;
		this.subscriptionExecutor = subscriptionExecutor;

		////////////////////////////////////////////////////////////////////////////////////////////////////////
		// CREATION IF THE PROXY INSTANCE
		@SuppressWarnings("unchecked")
		Class<T>[] classes = (Class<T>[]) new Class<?>[] { repositoryInterface };
		@SuppressWarnings("unchecked")
		T t = (T) Proxy.newProxyInstance(repositoryInterface.getClassLoader(), classes, this);
		// Assignment is done in two step, so that the @SuppressWarnings is not on the whole method.
		this.proxyInstance = t;

		for (Method method : repositoryInterface.getDeclaredMethods()) {
			registeredMethods.put(method, registerMethod(method));
		}
	}

	/**
	 * Getter for the proxy instance that has been created from this invocation handler.
	 * 
	 * @return
	 */
	T getProxyInstance() {
		return proxyInstance;
	}

	/**
	 * Register the given method of the repository interface, and returns its characteristics
	 * 
	 * @throws GraphQLRequestPreparationException
	 */
	private RegisteredMethod registerMethod(Method method) throws GraphQLRequestPreparationException {
		RegisteredMethod registeredMethod = new RegisteredMethod();

		// Some checks, to begin with:
		if (!Arrays.asList(method.getExceptionTypes()).contains(GraphQLRequestExecutionException.class)) {
			throw new GraphQLRequestPreparationException("Error while preparing the GraphQL Repository, on the method '"
					+ method.getDeclaringClass().getName() + "." + method.getName()
					+ "(..). Every method of GraphQL repositories must throw the 'com.graphql_java_generator.exception.GraphQLRequestExecutionException' exception, but the '"
					+ method.getName() + "' doesn't");
		}

		PartialRequest partialRequest = method.getAnnotation(PartialRequest.class);
		FullRequest fullRequest = method.getAnnotation(FullRequest.class);

		if (partialRequest != null) {
			registerMethod(registeredMethod, method, false, partialRequest.requestName(), partialRequest.requestType(),
					partialRequest.request());
		} else if (fullRequest != null) {
			registerMethod(registeredMethod, method, true, null, fullRequest.requestType(), fullRequest.request());
		} else {
			throw new GraphQLRequestPreparationException("Error while preparing the GraphQL Repository, on the method '"
					+ method.getDeclaringClass().getName() + "." + method.getName()
					+ "(..). Every method of GraphQL repositories must be annotated by either @PartialRequest or @FullRequest. But the '"
					+ method.getName() + "()' isn't.");
		}
		return registeredMethod;
	}

	/**
	 * Register the given method with the given parameters
	 * 
	 * @param registeredMethod
	 *            The class where all the registering info should be stored
	 * @param method
	 *            The method that is being registered
	 * @param fullRequest
	 *            True if this method is marked with the {@link FullRequest} annotation, false otherwise, that is: the
	 *            method is marked with the {@link PartialRequest} annotation
	 * @param requestName
	 *            The name of the request, as read in the {@link PartialRequest} annotation, null for full requests
	 * @param requestType
	 *            The type of request. Query is the default.
	 * @param request
	 *            The string of the request. For {@link FullRequest}, it must be a valid GraphQL request. For more
	 *            information, have a look at the <A HREF=
	 *            "https://github.com/graphql-java-generator/graphql-maven-plugin-project/wiki/client_exec_graphql_requests">Client
	 *            wiki</A>
	 * @throws GraphQLRequestPreparationException
	 */
	private void registerMethod(RegisteredMethod registeredMethod, Method method, boolean fullRequest,
			String requestName, RequestType requestType, String request) throws GraphQLRequestPreparationException {
		registeredMethod.method = method;
		registeredMethod.fullRequest = fullRequest;
		registeredMethod.requestName = requestName;
		registeredMethod.requestType = requestType;
		registeredMethod.executor = getExecutor(method, requestType);
		registerParameters(registeredMethod, method);

		if (fullRequest) {
			// It's a full request
			registeredMethod.executorMethodName = "execWithBindValues";
			registeredMethod.executorGetGraphQLRequestMethodName = "getGraphQLRequest";
		} else if (registeredMethod.requestName == null || registeredMethod.requestName.equals("")) {
			// It's a partial request, with no given requestName
			registeredMethod.executorMethodName = method.getName() + "WithBindValues";
			registeredMethod.executorGetGraphQLRequestMethodName = "get" + graphqlUtils.getPascalCase(method.getName())
					+ "GraphQLRequest";
		} else {
			// It's a partial request, with a given requestName
			registeredMethod.executorMethodName = registeredMethod.requestName + "WithBindValues";
			registeredMethod.executorGetGraphQLRequestMethodName = "get"
					+ graphqlUtils.getPascalCase(registeredMethod.requestName) + "GraphQLRequest";
		}

		try {
			registeredMethod.executorMethod = registeredMethod.executor.getClass()
					.getMethod(registeredMethod.executorMethodName, registeredMethod.executorParameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			StringBuffer parameters = new StringBuffer();
			String separator = "";
			for (Class<?> clazz : registeredMethod.executorParameterTypes) {
				parameters.append(separator);
				parameters.append(clazz.getName());
				separator = ",";
			}
			throw new GraphQLRequestPreparationException("Error while preparing the GraphQL Repository, on the method '"
					+ method.getDeclaringClass().getName() + "." + method.getName()
					+ "(..). Couldn't find the matching executor method '" + registeredMethod.executorMethodName
					+ "' for executor class '" + registeredMethod.executor.getClass().getName()
					+ "' with these parameters: [" + parameters + "]", e);
		}

		// The returned value by the executor method must be assignable to the returned value for the GraphQL Repository
		// method
		if (!method.getReturnType().isAssignableFrom(registeredMethod.executorMethod.getReturnType())) {
			throw new GraphQLRequestPreparationException("Error while preparing the GraphQL Repository, on the method '"
					+ method.getDeclaringClass().getName() + "." + method.getName() + "(..). This method should return "
					+ registeredMethod.executorMethod.getReturnType().getName() + " but returns "
					+ method.getReturnType().getName() + " (and the later can not be assigned to the former)");
		}

		registeredMethod.request = request;
		registeredMethod.graphQLRequest = getGraphQLRequest(registeredMethod);
	}

	/**
	 * This method scans the method parameters, and register its parameters in the
	 * {@link RegisteredMethod#registeredParameters} attribute. It also fills the
	 * {@link RegisteredMethod#executorParameterTypes} attribute.
	 *
	 * @param registeredMethod
	 *            The record for the method that is being registered
	 * @param method
	 *            The method of the {@link GraphQLRepository}
	 * @return
	 * @throws GraphQLRequestPreparationException
	 */
	private void registerParameters(RegisteredMethod registeredMethod, Method method)
			throws GraphQLRequestPreparationException {
		List<Class<?>> executorParameterTypes = new ArrayList<>();
		// The first parameter of the executor is the GraphQL Request, given as an ObjectResponse
		executorParameterTypes.add(ObjectResponse.class);

		// Let's go through all the parameters of the method that we're registering
		boolean foundBindParameterAnnotation = false;
		for (Parameter param : method.getParameters()) {
			RegisteredParameter regParam = new RegisteredParameter();
			regParam.name = param.getName();
			regParam.type = param.getType();

			// No Map parameter, nor vararg (object[])
			if (Map.class.isAssignableFrom(regParam.type) || Object[].class.isAssignableFrom(regParam.type)) {
				throw new GraphQLRequestPreparationException(
						"Error while preparing the GraphQL Repository, on the method '"
								+ registeredMethod.method.getDeclaringClass().getName() + "."
								+ registeredMethod.method.getName()
								+ "(..). Map and vararg (Object[]) are not allowed. But the '" + param.getName()
								+ "' is an instance of '" + param.getType().getName() + "'");
			}

			BindParameter bindParameter = param.getAnnotation(BindParameter.class);
			if (bindParameter == null) {
				if (registeredMethod.fullRequest = false) {
					throw new GraphQLRequestPreparationException(
							"Error while preparing the GraphQL Repository, on the method '"
									+ registeredMethod.method.getDeclaringClass().getName() + "."
									+ registeredMethod.method.getName()
									+ "(..). This request is a full request: all its parameters must be marked with the '"
									+ BindParameter.class.getSimpleName() + "' annotation. But the '" + param.getName()
									+ "' isn't marked with this annotation.");
				} else if (foundBindParameterAnnotation) {
					throw new GraphQLRequestPreparationException(
							"Error while preparing the GraphQL Repository, on the method '"
									+ registeredMethod.method.getDeclaringClass().getName() + "."
									+ registeredMethod.method.getName()
									+ "(..). It is not allowed to have parameters without the '"
									+ BindParameter.class.getSimpleName()
									+ "' annotation, after parameters that have this annotation. The '"
									+ param.getName() + "' parameter lacks the '" + BindParameter.class.getSimpleName()
									+ "' annotation.");
				} else {
					// It's a partial request, and we didn't find any parameter with the BindParameter yet.
					// So we've found a parameter that should be a parameter of the executor method.
					executorParameterTypes.add(param.getType());
				}
			} else {
				// This parameter is annotated with the BindParameter. Let's store the bind parameter name that is is
				// associated to.
				foundBindParameterAnnotation = true;
				regParam.bindParameterName = bindParameter.name();
			}

			registeredMethod.registeredParameters.add(regParam);
		} // for

		// The last parameter of executorParameterTypes is the Map for the Bind Parameter, which may not be a parameter
		// of the GraphQL repository method
		executorParameterTypes.add(Map.class);

		// The next lien generates a runtime error ! :(
		// registeredMethod.executorParameterTypes = (Class<?>[]) (executorParameterTypes.toArray());
		// So, let's loop manually
		registeredMethod.executorParameterTypes = new Class<?>[executorParameterTypes.size()];
		for (int i = 0; i < registeredMethod.executorParameterTypes.length; i += 1) {
			registeredMethod.executorParameterTypes[i] = executorParameterTypes.get(i);
		}

		logger.debug("The expected parameter types for the '{}.{}' method are: {}",
				method.getDeclaringClass().getName(), method.getName(), registeredMethod.executorParameterTypes);
	}

	/**
	 * Retrieves the GraphQlRequest object from the data of the given {@link RegisteredMethod}.
	 * 
	 * @param registeredMethod
	 * @return
	 * @throws GraphQLRequestPreparationException
	 */
	private AbstractGraphQLRequest getGraphQLRequest(RegisteredMethod registeredMethod)
			throws GraphQLRequestPreparationException {
		Method getGraphQlMethod;
		try {
			getGraphQlMethod = registeredMethod.executor.getClass()
					.getDeclaredMethod(registeredMethod.executorGetGraphQLRequestMethodName, String.class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new GraphQLRequestPreparationException("Error while preparing the GraphQL Repository, on the method '"
					+ registeredMethod.method.getDeclaringClass().getName() + "." + registeredMethod.method.getName()
					+ "(..). Could not find the '" + registeredMethod.executorGetGraphQLRequestMethodName
					+ " method of the '" + registeredMethod.executor.getClass().getName() + "' class.");
		}

		try {
			return (AbstractGraphQLRequest) getGraphQlMethod.invoke(registeredMethod.executor,
					registeredMethod.request);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new GraphQLRequestPreparationException("Error while preparing the GraphQL Repository, on the method '"
					+ registeredMethod.method.getDeclaringClass().getName() + "." + registeredMethod.method.getName()
					+ "(..): " + e.getClass().getSimpleName() + " when trying to invoke the '"
					+ registeredMethod.executorGetGraphQLRequestMethodName + "' method of the '"
					+ registeredMethod.executor.getClass().getName() + "' class", e);
		}
	}

	/**
	 * Retrieves the executor that matches the given {@link RequestType}
	 * 
	 * @param method
	 * @param requestType
	 * @return
	 * @throws GraphQLRequestPreparationException
	 *             If no executor of this {@link RequestType} has been provided
	 */
	private Object getExecutor(Method method, RequestType requestType) throws GraphQLRequestPreparationException {
		switch (requestType) {
		case query:
			return queryExecutor;
		case mutation:
			return mutationExecutor;
		case subscription:
			return subscriptionExecutor;
		}

		throw new GraphQLRequestPreparationException("The '" + method.getDeclaringClass().getName() + "."
				+ method.getName() + "()' method refers to a '" + requestType.toString()
				+ "', but there is no such executor found. Check if the GraphQL has such a request type defined.");
	}

	/**
	 * Invocation of the {@link InvocationHandler}. This method is called when a method of the T interface is called.
	 * This call is delegated to the relevant Query/Mutation/Subscription executor. <BR/>
	 * {@inheritDoc}
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		RegisteredMethod registeredMethod = registeredMethods.get(method);

		if (registeredMethod == null) {
			throw new GraphQLRequestExecutionException("The method '" + method.getDeclaringClass().getName() + "."
					+ method.getName()
					+ "' has not been stored in initialization phase of this InvocationHandler. Is this method coming from the right interface? (that is, the same as the one this InvocationHandler has been created with?)");
		}

		List<Object> params = new ArrayList<>();
		Map<String, Object> bindParameters = new HashMap<>();
		// The first parameter is the GraphQL request
		params.add(registeredMethod.graphQLRequest);
		// The we loop the given args match the registeredParameters.

		// A first check, to begin with
		if ((args == null ? 0 : args.length) != registeredMethod.registeredParameters.size()) {
			throw new GraphQLRequestExecutionException("Error while invoking the '" + method.getDeclaringClass() + "."
					+ method.getName() + "': the proxy invocation handler receives " + (args == null ? 0 : args.length)
					+ ", but it has registered " + registeredMethod.registeredParameters.size() + " parameters");
		}
		if (args != null) {
			for (int i = 0; i < args.length; i += 1) {
				RegisteredParameter regParam = registeredMethod.registeredParameters.get(i);

				if (regParam.bindParameterName == null) {
					// This is regular parameter, that we must just add to the argument list
					params.add(args[i]);
				} else {
					bindParameters.put(regParam.bindParameterName, args[i]);
				}
			} // for
		}
		params.add(bindParameters);

		return registeredMethod.executorMethod.invoke(registeredMethod.executor, params.toArray());
	}
}
