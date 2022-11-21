package com.gngsn.webClient.common;

/**
 * Constants.
 */
public interface Constants {

	/**
	 * The constant SUCCESS.
	 */
	String SUCCESS = "success";

	/**
	 * The constant APP_PARAM.
	 */
	String APP_PARAM = "appParam";

	/**
	 * The constant context.
	 */
	String CONTEXT = "context";

	/**
	 * The constant context path.
	 */
	String CONTEXT_PATH = "contextPath";

	/**
	 * The constant META_DATA.
	 */
	String META_DATA = "metaData";

	/**
	 * The constant CLIENT_RESPONSE_ATTR.
	 */
	String CLIENT_RESPONSE_ATTR = "webHandlerClientResponse";

	/**
	 * The constant DUBBO_RPC_RESULT_EMPTY.
	 */
	String DUBBO_RPC_RESULT_EMPTY = "dubbo has not return value!";

	/**
	 * The constant SOFA_RPC_RESULT_EMPTY.
	 */
	String SOFA_RPC_RESULT_EMPTY = "sofa has not return value!";

	/**
	 * The constant RPC_RESULT.
	 */
	String RPC_RESULT = "rpc_result";

	/**
	 * The constant MOTAN_RPC_RESULT.
	 */
	String MOTAN_RPC_RESULT = "motan_rpc_result";

	/**
	 * The constant TARS_RPC_RESULT_EMPTY.
	 */
	String TARS_RPC_RESULT_EMPTY = "tars has not return value!";

	/**
	 * The constant MOTAN_RPC_RESULT_EMPTY.
	 */
	String MOTAN_RPC_RESULT_EMPTY = "motan has not return value!";

	/**
	 * The constant CLIENT_RESPONSE_RESULT_TYPE.
	 */
	String CLIENT_RESPONSE_RESULT_TYPE = "webHandlerClientResponseResultType";

	/**
	 * The constant CLIENT_RESPONSE_CONN_ATTR.
	 */
	String CLIENT_RESPONSE_CONN_ATTR = "nettyClientResponseConnection";

	/**
	 * The constant HTTP_TIME_OUT.
	 */
	String HTTP_TIME_OUT = "httpTimeOut";

	/**
	 * The constant HTTP_RETRY.
	 */
	String HTTP_RETRY = "httpRetry";

	/**
	 * The constant RETRY_STRATEGY.
	 */
	String RETRY_STRATEGY = "retryStrategy";

	/**
	 * The constant LOAD_BALANCE.
	 */
	String LOAD_BALANCE = "loadBalance";

	/**
	 * divide online selector id.
	 */
	String DIVIDE_SELECTOR_ID = "divideSelectorId";

	/**
	 * Original response Content-Type attribute name.
	 */
	String ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR = "original_response_content_type";

	/**
	 * The constant HTTP_URI.
	 */
	String HTTP_URI = "httpUri";

	/**
	 * The constant HTTP_DOMAIN.
	 */
	String HTTP_DOMAIN = "httpDomain";

	/**
	 * The constant RPC_PARAM_TRANSFORM.
	 */
	String PARAM_TRANSFORM = "param_transform";

	/**
	 * The constant DECODE.
	 */
	String DECODE = "UTF-8";

	/**
	 * The constant MODULE.
	 */
	String MODULE = "module";

	/**
	 * RESPONSE_MONO.
	 */
	String RESPONSE_MONO = "RESPONSE_MONO";

	/**
	 * WATCHER_HTTP_STATUS.
	 */
	String WATCHER_HTTP_STATUS = "WATCHER_HTTP_STATUS";

	/**
	 * The constant METHOD.
	 */
	String METHOD = "method";

	/**
	 * The constant APP_KEY.
	 */
	String APP_KEY = "appKey";

	/**
	 * The constant RPC_TYPE.
	 */
	String RPC_TYPE = "rpcType";

	/**
	 * The constant SIGN.
	 */
	String SIGN = "sign";

	/**
	 * The constant PATH.
	 */
	String PATH = "path";

	/**
	 * The constant VERSION.
	 */
	String VERSION = "version";

	/**
	 * The constant SIGN_PARAMS_ERROR.
	 */
	String SIGN_PARAMS_ERROR = "sign parameters are incomplete!";

	/**
	 * The constant SIGN_APP_KEY_IS_NOT_EXIST.
	 */
	String SIGN_APP_KEY_IS_NOT_EXIST = "sign appKey does not exist.";

	/**
	 * The constant SIGN_PATH_NOT_EXIST.
	 */
	String SIGN_PATH_NOT_EXIST = "you have not configured the sign path.";

	/**
	 * The constant SIGN_VALUE_IS_ERROR.
	 */
	String SIGN_VALUE_IS_ERROR = "signature value is error!";

	/**
	 * The constant TIMESTAMP.
	 */
	String TIMESTAMP = "timestamp";

	/**
	 * The constant REJECT_MSG.
	 */
	String REJECT_MSG = " You are forbidden to visit";

	/**
	 * The constant REWRITE_URI.
	 */
	String REWRITE_URI = "rewrite_uri";

	String HTTP_ACCEPT_ENCODING_GZIP = "gzip";

	/**
	 * The constant URI.
	 */
	String URI = "uri";

	/**
	 * The constant LINE_SEPARATOR.
	 */
	String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * The constant URL_SEPARATOR.
	 */
	String PATH_SEPARATOR = "/";

	/**
	 * hystrix withExecutionIsolationSemaphoreMaxConcurrentRequests.
	 */
	int MAX_CONCURRENT_REQUESTS = 100;

	/**
	 * hystrix  withCircuitBreakerErrorThresholdPercentage.
	 */
	int ERROR_THRESHOLD_PERCENTAGE = 50;

	/**
	 * hystrix withCircuitBreakerRequestVolumeThreshold.
	 */
	int REQUEST_VOLUME_THRESHOLD = 20;

	/**
	 * hystrix withCircuitBreakerSleepWindowInMilliseconds.
	 */
	int SLEEP_WINDOW_INMILLISECONDS = 5000;

	/**
	 * The constant TIME_OUT.
	 */
	long TIME_OUT = 3000;

	/**
	 * The constant COLONS.
	 */
	String COLONS = ":";

	/**
	 * hystrix thead pool core size.
	 */
	int HYSTRIX_THREAD_POOL_CORE_SIZE = 10;

	/**
	 * hystrix thread pool max size.
	 */
	int HYSTRIX_THREAD_POOL_MAX_SIZE = 10;

	/**
	 * hystrix thread pool keep alive time minutes.
	 */
	int HYSTRIX_THREAD_KEEP_ALIVE_TIME_MINUTE = 1;

	/**
	 * hystrix thread pool queue size.
	 */
	int HYSTRIX_THREAD_POOL_QUEUE_SIZE = 12;

	/**
	 * ratelimiter timeoutDurationRate.
	 */
	int TIMEOUT_DURATION_RATE = 5000;

	/**
	 * ratelimiter limitRefreshPeriod.
	 */
	int LIMIT_REFRESH_PERIOD = 500;

	/**
	 * ratelimiter limitForPeriod.
	 */
	int LIMIT_FOR_PERIOD = 50;

	/**
	 * circuitBreaker circuitEnable.
	 */
	int CIRCUIT_DISABLE = 0;

	/**
	 * circuitBreaker enable.
	 */
	int CIRCUIT_ENABLE = 1;

	/**
	 * circuitBreaker timeoutDuration.
	 */
	long TIMEOUT_DURATION = 30000;

	/**
	 * circuitBreaker slidingWindowSize.
	 */
	int SLIDING_WINDOW_SIZE = 100;

	/**
	 * circuitBreaker slidingWindowType.
	 */
	int SLIDING_WINDOW_TYPE = 0;

	/**
	 * circuitBreaker minimumNumberOfCalls.
	 */
	int MINIMUM_NUMBER_OF_CALLS = 100;

	/**
	 * circuitBreaker waitIntervalFunctionInOpenState.
	 */
	int WAIT_INTERVAL_FUNCTION_IN_OPEN_STATE = 60000;

	/**
	 * circuitBreaker waitIntervalFunctionInOpenState.
	 */
	int PERMITTED_NUMBER_OF_CALLS_IN_HALF_OPEN_STATE = 10;

	/**
	 * circuitBreaker failureRateThreshold.
	 */
	float FAILURE_RATE_THRESHOLD = 50;

	/**
	 * circuitBreaker automaticTransitionFromOpenToHalfOpenEnabled.
	 */
	boolean AUTOMATIC_TRANSITION_FROM_OPEN_TO_HALF_OPEN_ENABLED = false;

	/**
	 * jwt handle key for secretKey.
	 */
	String SECRET_KEY = "secretKey";

	/**
	 * local key.
	 */
	String LOCAL_KEY = "localKey";

	/**
	 * jwt handle key for filterPath.
	 */
	String FILTER_PATH = "filterPath";

	/**
	 * Enable the flow rule.
	 */
	int SENTINEL_ENABLE_FLOW_RULE = 1;

	/**
	 * Sentinel qps flow grade.
	 */
	int SENTINEL_QPS_FLOW_GRADE = 1;

	/**
	 * Sentinel flow reject behavior.
	 */
	int SENTINEL_FLOW_REJECT = 0;

	/**
	 * Enable the degrade rule.
	 */
	int SENTINEL_ENABLE_DEGRADE_RULE = 1;

	/**
	 * Sentinel response RT degrade rule.
	 */
	int SENTINEL_RESPONSE_RULE_GRADE = 0;

	/**
	 * Sentinel degrade rule default min request.
	 */
	int SENTINEL_MIN_REQUEST_AMOUNT = 5;

	/**
	 * Sentinel degrade rule default slow ratio threshold.
	 */
	double SENTINEL_SLOW_RATIO_THRESHOLD = 1.0d;

	/**
	 * Sentinel degrade rule default stat intervals.
	 */
	int SENTINEL_STAT_INTERVALS = 1;

	/**
	 * default warmup.
	 */
	int DEFAULT_WARMUP = 10 * 60 * 1000;

	/**
	 * default register type.
	 */
	String DEFAULT_REGISTER_TYPE = "http";

	/**
	 * is checked.
	 */
	String IS_CHECKED = "checked";

	/**
	 * default checked value.
	 */
	String DEFAULT_CHECK_VALUE = "false";

	/**
	 * zombie check times.
	 */
	String ZOMBIE_CHECK_TIMES = "zombieCheckTimes";

	/**
	 * default zombie check times value.
	 */
	String ZOMBIE_CHECK_TIMES_VALUE = "5";

	/**
	 * scheduled time.
	 */
	String SCHEDULED_TIME = "scheduledTime";

	/**
	 * default scheduled time value.
	 */
	String SCHEDULED_TIME_VALUE = "10";

	/**
	 * default headerMaxSize value.
	 */
	int HEADER_MAX_SIZE = 10240;

	/**
	 * default requestMaxSize value.
	 */
	int REQUEST_MAX_SIZE = 102400;

	/**
	 * String default.
	 */
	String DEFAULT = "DEFAULT";

	/**
	 * dubbo group.
	 */
	String GROUP = "group";

	/**
	 * redis script path.
	 */
	String SCRIPT_PATH = "/META-INF/scripts/";

	/**
	 * cache max count.
	 */
	int CACHE_MAX_COUNT = 1000;

	/**
	 * the empty json.
	 */
	String EMPTY_JSON = "{}";

	/**
	 * zombie removal times.
	 */
	String ZOMBIE_REMOVAL_TIMES = "zombieRemovalTimes";

	/**
	 * The default zombie removal time value, unit is second.
	 */
	String ZOMBIE_REMOVAL_TIMES_VALUE = "60";

	/**
	 * shared thread pool type.
	 */
	String SHARED = "shared";

	/**
	 * fixed thread pool type.
	 */
	String FIXED = "fixed";

	/**
	 * eager thread pool type.
	 */
	String EAGER = "eager";

	/**
	 * limited thread pool type.
	 */
	String LIMITED = "limited";

	/**
	 * cached thread pool type.
	 */
	String CACHED = "cached";

	/**
	 * The maximum free memory reserved by the blocking queue for the JVM.
	 */
	int THE_256_MB = 256 * 1024 * 1024;

	/**
	 * The default cluster of dubbo client.
	 */
	String DEFAULT_CLUSTER = "failover";

	int PREFIX_FORWARD_ENABLE = 1;

	/**
	 * String q.
	 */
	default void findConstants() {
	}
}