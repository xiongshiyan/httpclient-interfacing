package top.jfunc.common.http.base;

import top.jfunc.common.http.HttpConstants;
import top.jfunc.common.http.Method;
import top.jfunc.common.http.cookie.CookieJar;
import top.jfunc.common.http.holder.*;
import top.jfunc.common.http.interceptor.CompositeInterceptor;
import top.jfunc.common.http.interceptor.Interceptor;
import top.jfunc.common.http.request.HttpRequest;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 全局公共配置
 * @author xiongshiyan at 2018/8/7 , contact me with email yanshixiong@126.com or phone 15208384257
 */
public class Config {
    private ConfigFrozen configFrozen = new ConfigFrozen();
    /**
     * BaseUrl,如果设置了就在正常传送的URL之前添加上
     */
    private String baseUrl                                  = null;
    /**
     * 连接超时时间
     */
    private int defaultConnectionTimeout                    = HttpConstants.DEFAULT_CONNECT_TIMEOUT;
    /**
     * 读数据超时时间
     */
    private int defaultReadTimeout                          = HttpConstants.DEFAULT_READ_TIMEOUT;
    /**
     * 请求体编码
     */
    private String defaultBodyCharset                       = HttpConstants.DEFAULT_CHARSET;
    /**
     * 返回体编码
     */
    private String defaultResultCharset                     = HttpConstants.DEFAULT_CHARSET;
    /**
     * 代理设置,如果有就设置
     * Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostName, port));
     */
    private ProxyInfo proxyInfo = null;
    /**
     * SSL相关设置处理器
     */
    private SSLHolder sslHolder = new DefaultSSLHolder2();
    /**
     * 默认的请求头,每个请求都会加上//private MultiValueMap<String,String> defaultHeaders = null;
     */
    private HeaderHolder headerHolder = new DefaultHeaderHolder();
    /**
     * 默认的请求Query参数,每个请求都会加上//private MultiValueMap<String,String> defaultQueryParams = null;
     */
    private ParamHolder queryParamHolder = new DefaultParamHolder();
    /**
     * CookieJar，处理Cookie
     */
    private CookieJar cookieJar = null;

    /**
     * 拦截器链
     */
    private CompositeInterceptor compositeInterceptor;

    public static Config defaultConfig(){
        return new Config();
    }

    public Config freezeConfig(){
        configFrozen.freezeConfig();
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Config setBaseUrl(String baseUrl) {
        configFrozen.ensureConfigNotFreeze();
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * 统一的获取实际的值：逻辑是输入的不等于空就返回输入的，否则返回默认的
     * @param input 输入值，可能为null
     * @param defaultValue 默认值
     * @param <T> 泛型参数
     * @return 输入的值或者默认值
     */
    public <T> T getValueWithDefault(T input , T defaultValue){
        return null == input ? defaultValue : input;
    }


    public int getDefaultConnectionTimeout() {
        return defaultConnectionTimeout;
    }
    public int getConnectionTimeoutWithDefault(int connectionTimeout){
        return HttpConstants.TIMEOUT_UNSIGNED == connectionTimeout ? getDefaultConnectionTimeout() : connectionTimeout;
    }
    public Config setDefaultConnectionTimeout(int defaultConnectionTimeout) {
        configFrozen.ensureConfigNotFreeze();
        this.defaultConnectionTimeout = defaultConnectionTimeout;
        return this;
    }

    public int getDefaultReadTimeout() {
        return defaultReadTimeout;
    }
    public int getReadTimeoutWithDefault(int readTimeout){
        return HttpConstants.TIMEOUT_UNSIGNED == readTimeout ? getDefaultReadTimeout() : readTimeout;
    }
    public Config setDefaultReadTimeout(int defaultReadTimeout) {
        configFrozen.ensureConfigNotFreeze();
        this.defaultReadTimeout = defaultReadTimeout;
        return this;
    }

    public String getDefaultQueryCharset() {
        return queryParamHolder.getParamCharset();
    }

    public String getQueryCharsetWithDefault(String queryCharset){
        return getValueWithDefault(queryCharset , getDefaultQueryCharset());
    }

    public Config setDefaultQueryCharset(String defaultQueryCharset) {
        configFrozen.ensureConfigNotFreeze();
        this.queryParamHolder.setParamCharset(defaultQueryCharset);
        return this;
    }

    public String getDefaultBodyCharset() {
        return defaultBodyCharset;
    }

    public String getBodyCharsetWithDefault(String bodyCharset){
        return getValueWithDefault(bodyCharset , getDefaultBodyCharset());
    }
    public Config setDefaultBodyCharset(String defaultBodyCharset) {
        configFrozen.ensureConfigNotFreeze();
        this.defaultBodyCharset = defaultBodyCharset;
        return this;
    }

    public String getDefaultResultCharset() {
        return defaultResultCharset;
    }
    public String getResultCharsetWithDefault(String resultCharset){
        return getValueWithDefault(resultCharset , getDefaultResultCharset());
    }

    public Config setDefaultResultCharset(String defaultResultCharset) {
        configFrozen.ensureConfigNotFreeze();
        this.defaultResultCharset = defaultResultCharset;
        return this;
    }
    public ProxyInfo getProxyInfoWithDefault(ProxyInfo proxyInfo){
        return getValueWithDefault(proxyInfo , getProxyInfo());
    }

    public ProxyInfo getProxyInfo() {
        return proxyInfo;
    }

    public Config setProxyInfo(ProxyInfo proxyInfo) {
        configFrozen.ensureConfigNotFreeze();
        this.proxyInfo = proxyInfo;
        return this;
    }
    public HostnameVerifier getHostnameVerifierWithDefault(HostnameVerifier hostnameVerifier){
        return getValueWithDefault(hostnameVerifier , sslHolder.getHostnameVerifier());
    }

    public SSLContext getSSLContextWithDefault(SSLContext sslContext) {
        return getValueWithDefault(sslContext , sslHolder.getSslContext());
    }

    public SSLSocketFactory getSSLSocketFactoryWithDefault(SSLSocketFactory sslSocketFactory) {
        return getValueWithDefault(sslSocketFactory , sslHolder.getSslSocketFactory());
    }

    public X509TrustManager getX509TrustManagerWithDefault(X509TrustManager x509TrustManager){
        return getValueWithDefault(x509TrustManager , sslHolder.getX509TrustManager());
    }
    public SSLHolder sslHolder(){
        return sslHolder;
    }

    public HeaderHolder headerHolder() {
        return headerHolder;
    }

    public ParamHolder queryParamHolder(){
        return queryParamHolder;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    public Config setCookieJar(CookieJar cookieJar) {
        this.cookieJar = cookieJar;
        return this;
    }

    public CompositeInterceptor getCompositeInterceptor() {
        return compositeInterceptor;
    }

    public Config setCompositeInterceptor(CompositeInterceptor compositeInterceptor) {
        configFrozen.ensureConfigNotFreeze();
        this.compositeInterceptor = compositeInterceptor;
        return this;
    }
    public Config addInterceptor(Interceptor interceptor , Interceptor... interceptors){
        configFrozen.ensureConfigNotFreeze();
        if(null == this.compositeInterceptor){
            this.compositeInterceptor = new CompositeInterceptor();
        }
        this.compositeInterceptor.add(interceptor , interceptors);
        return this;
    }

    HttpRequest onBeforeIfNecessary(HttpRequest httpRequest , Method method){
        if(hasInterceptors()){
            return compositeInterceptor.onBefore(httpRequest, method);
        }
        return httpRequest;
    }

    void onBeforeReturnIfNecessary(HttpRequest httpRequest , Object returnValue){
        if(hasInterceptors()){
            compositeInterceptor.onBeforeReturn(httpRequest, returnValue);
        }
    }
    void onErrorIfNecessary(HttpRequest httpRequest , Exception exception){
        if(hasInterceptors()){
            compositeInterceptor.onError(httpRequest, exception);
        }
    }
    void onFinallyIfNecessary(HttpRequest httpRequest){
        if(hasInterceptors()){
            compositeInterceptor.onFinally(httpRequest);
        }
    }
    private boolean hasInterceptors(){
        return null != this.compositeInterceptor
                && this.compositeInterceptor.hasInterceptors();
    }
}
