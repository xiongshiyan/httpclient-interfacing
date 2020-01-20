package top.jfunc.common.http.component.jdk;

import top.jfunc.common.http.base.Config;
import top.jfunc.common.http.base.ProxyInfo;
import top.jfunc.common.http.component.AbstractRequesterFactory;
import top.jfunc.common.http.request.HttpRequest;
import top.jfunc.common.http.util.NativeUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author xiongshiyan at 2020/1/6 , contact me with email yanshixiong@126.com or phone 15208384257
 */
public class DefaultJdkConnectionFactory extends AbstractRequesterFactory<HttpURLConnection> {

    @Override
    public HttpURLConnection doCreate(HttpRequest httpRequest) throws IOException{
        URL url = new URL(httpRequest.getCompletedUrl());
        //1.1如果需要则设置代理
        Config config = httpRequest.getConfig();
        ProxyInfo proxyInfo = config.getProxyInfoWithDefault(httpRequest.getProxyInfo());
        HttpURLConnection connection = (null != proxyInfo) ?
                (HttpURLConnection)url.openConnection(proxyInfo.getProxy()) :
                (HttpURLConnection) url.openConnection();

        ////////////////////////////////////ssl处理///////////////////////////////////
        if(connection instanceof HttpsURLConnection){
            //默认设置这些
            initSSL((HttpsURLConnection)connection , httpRequest);
        }
        ////////////////////////////////////ssl处理///////////////////////////////////

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        connection.setRequestMethod(httpRequest.getMethod().name());
        connection.setConnectTimeout(config.getConnectionTimeoutWithDefault(httpRequest.getConnectionTimeout()));
        connection.setReadTimeout(config.getReadTimeoutWithDefault(httpRequest.getReadTimeout()));

        ///HttpUrlConnection的重定向貌似很多bug，自己来实现
        ///connection.setInstanceFollowRedirects(httpRequest.followRedirects());

        return connection;
    }

    protected void initSSL(HttpsURLConnection connection , HttpRequest httpRequest){
        Config config = httpRequest.getConfig();
        NativeUtil.initSSL(connection, config.getHostnameVerifierWithDefault(httpRequest.getHostnameVerifier()) ,
                config.getSSLSocketFactoryWithDefault(httpRequest.getSslSocketFactory()));
    }
}