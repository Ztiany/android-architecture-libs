package com.android.sdk.net.rxjava2;

import com.android.sdk.net.core.result.ExceptionFactory;
import com.android.sdk.net.core.result.Result;
import com.github.dmstocking.optional.java.util.Optional;


/**
 * 用于处理 Retrofit + RxJava2 网络请求返回的结果
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-11-22 17:22
 */
@SuppressWarnings("unused")
public class ResultHandlers {

    private static class ResultTransformer<Upstream, T extends Result<Upstream>> extends HttpResultTransformer<Upstream, Upstream, T> {
        ResultTransformer() {
            super(true, Result::getData, null);
        }

        ResultTransformer(ExceptionFactory exceptionFactory) {
            super(true, Result::getData, exceptionFactory);
        }
    }

    private static class OptionalResultTransformer<Upstream, T extends Result<Upstream>> extends HttpResultTransformer<Upstream, Optional<Upstream>, T> {
        OptionalResultTransformer() {
            super(false, rResult -> Optional.ofNullable(rResult.getData()), null);
        }

        OptionalResultTransformer(ExceptionFactory exceptionFactory) {
            super(false, rResult -> Optional.ofNullable(rResult.getData()), exceptionFactory);
        }
    }

    private static class ResultChecker<Upstream, T extends Result<Upstream>> extends HttpResultTransformer<Upstream, T, T> {
        @SuppressWarnings("unchecked")
        ResultChecker() {
            super(false, rResult -> (T) rResult, null);
        }

        @SuppressWarnings("unchecked")
        ResultChecker(ExceptionFactory exceptionFactory) {
            super(false, rResult -> (T) rResult, exceptionFactory);
        }
    }

    private static final ResultTransformer DATA_TRANSFORMER = new ResultTransformer();

    private static final OptionalResultTransformer OPTIONAL_TRANSFORMER = new OptionalResultTransformer();

    private static final ResultChecker RESULT_CHECKER = new ResultChecker();

    /**
     * 返回一个Transformer，用于统一处理网络请求返回的 Observer 数据。对网络异常和请求结果做了通用处理：
     * <pre>
     * 1. 网络无连接抛出 {@link com.android.sdk.net.core.exception.NetworkErrorException} 由下游处理
     * 2. HttpResult==null 抛出 {@link com.android.sdk.net.core.exception.NetworkErrorException} 由下游处理
     * 3. HttpResult.getCode() != SUCCESS 抛出 {@link com.android.sdk.net.core.exception.ApiErrorException} 由下游处理
     * 4. 返回的结果不符合约定的数据模型处理或为 null 抛出 {@link com.android.sdk.net.core.exception.ServerErrorException} 由下游处理
     * 5. 最后把 HttpResult&lt;T&gt; 中的数据 T 提取到下游
     * </pre>
     */
    @SuppressWarnings("unchecked")
    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, Upstream, T> _resultExtractor() {
        return (HttpResultTransformer<Upstream, Upstream, T>) DATA_TRANSFORMER;
    }

    public static <Upstream> HttpResultTransformer<Upstream, Upstream, Result<Upstream>> resultExtractor() {
        return _resultExtractor();
    }

    /**
     * 与{@link #resultExtractor()}的行为类似，但是最后把 HttpResult&lt;T&gt; 中的数据 T 用 {@link Optional} 包装后再转发到下游。
     * 适用于 HttpResult.getData() 可能为 null 的情况
     */
    @SuppressWarnings("unchecked")
    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, Optional<Upstream>, T> _optionalExtractor() {
        return (HttpResultTransformer<Upstream, Optional<Upstream>, T>) OPTIONAL_TRANSFORMER;
    }

    public static <Upstream> HttpResultTransformer<Upstream, Optional<Upstream>, Result<Upstream>> optionalExtractor() {
        return _optionalExtractor();
    }

    /**
     * 不提取 HttpResult&lt;T&gt; 中的数据 T，只进行网络异常、空数据异常、错误JSON格式异常处理。
     */
    @SuppressWarnings("unchecked")
    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, T, T> _resultChecker() {
        return (HttpResultTransformer<Upstream, T, T>) RESULT_CHECKER;
    }

    public static <Upstream> HttpResultTransformer<Upstream, Result<Upstream>, Result<Upstream>> resultChecker() {
        return _resultChecker();
    }

    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, Upstream, T> _newExtractor(ExceptionFactory exceptionFactory) {
        return new ResultTransformer<>(exceptionFactory);
    }

    public static <Upstream> HttpResultTransformer<Upstream, Upstream, Result<Upstream>> newExtractor(ExceptionFactory exceptionFactory) {
        return _newExtractor(exceptionFactory);
    }

    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, Optional<Upstream>, T> _newOptionalExtractor(ExceptionFactory exceptionFactory) {
        return new OptionalResultTransformer<>(exceptionFactory);
    }

    public static <Upstream> HttpResultTransformer<Upstream, Optional<Upstream>, Result<Upstream>> newOptionalExtractor(ExceptionFactory exceptionFactory) {
        return _newOptionalExtractor(exceptionFactory);
    }

    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, T, T> newResultChecker(ExceptionFactory exceptionFactory) {
        return new ResultChecker<>(exceptionFactory);
    }

}