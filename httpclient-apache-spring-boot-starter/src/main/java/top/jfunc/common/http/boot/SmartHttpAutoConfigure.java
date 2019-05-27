package top.jfunc.common.http.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jfunc.common.http.base.Config;
import top.jfunc.common.http.base.ProxyInfo;
import top.jfunc.common.http.smart.ApacheSmartHttpClient;
import top.jfunc.common.http.smart.SmartHttpClient;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author xiongshiyan at 2019/5/10 , contact me with email yanshixiong@126.com or phone 15208384257
 */
@Configuration
@EnableConfigurationProperties(SmartHttpProperties.class)
public class SmartHttpAutoConfigure {

   /* @Bean
    @ConfigurationProperties("spring.http.smart")
    public SmartHttpProperties smartHttpProperties(){
        return new SmartHttpProperties();
    }*/
   @Autowired
   SmartHttpProperties smartHttpProperties;

    @Bean(name = "smartHttpClient")
    @ConditionalOnMissingBean
    public SmartHttpClient smartHttpClient(){
        SmartHttpClient smartHttpClient = new ApacheSmartHttpClient();

        Config config = Config.defaultConfig();
        if(null != smartHttpProperties.getBaseUrl()){
            config.setBaseUrl(smartHttpProperties.getBaseUrl());
        }
        config.setDefaultConnectionTimeout(smartHttpProperties.getDefaultConnectionTimeout());
        config.setDefaultReadTimeout(smartHttpProperties.getDefaultReadTimeout());
        config.setDefaultBodyCharset(smartHttpProperties.getDefaultBodyCharset());
        config.setDefaultResultCharset(smartHttpProperties.getDefaultResultCharset());

        if(null != smartHttpProperties.getDefaultHeaders()){
            config.setDefaultHeaders(smartHttpProperties.getDefaultHeaders());
        }
        if(null != smartHttpProperties.getDefaultQueryParams()){
            config.setDefaultQueryParams(smartHttpProperties.getDefaultQueryParams());
        }
        SmartHttpProperties.Proxy propertiesProxy = smartHttpProperties.getProxy();
        if(null != propertiesProxy){

            InetSocketAddress inetSocketAddress = new InetSocketAddress(
                    propertiesProxy.getHostName(), propertiesProxy.getPort());
            Proxy.Type type = Proxy.Type.valueOf(propertiesProxy.getType());
            
            config.setProxyInfo(ProxyInfo.of(new Proxy(type,inetSocketAddress),
                    propertiesProxy.getUsername() , propertiesProxy.getPassword()));
        }

        smartHttpClient.setConfig(config);

        return smartHttpClient;
    }
}
