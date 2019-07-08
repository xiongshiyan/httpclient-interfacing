package top.jfunc.common.http;

import org.junit.Assert;
import org.junit.Test;
import top.jfunc.common.http.holder.DefaultPhpUrlHolder;
import top.jfunc.common.http.holder.DefaultUrlHolder;
import top.jfunc.common.http.holder.PhpUrlHolder;
import top.jfunc.common.http.holder.UrlHolder;
import top.jfunc.common.http.request.HttpRequest;
import top.jfunc.common.http.request.holder.HolderHttpRequest;
import top.jfunc.common.http.request.holder.impl.HolderGetRequest;

import static org.hamcrest.Matchers.is;

/**
 * @author xiongshiyan at 2019/6/28 , contact me with email yanshixiong@126.com or phone 15208384257
 */
public class UrlHolderTest {
    @Test
    public void testUrl1(){
        String url = "http://127.0.0.1:8090/dddd/tttt?hill=hell&haven=heavy";
        UrlHolder holder = new DefaultUrlHolder().setUrl(url).addQueryParam("kkk","kkk");
        Assert.assertEquals("http://127.0.0.1:8090/dddd/tttt?hill=hell&haven=heavy&kkk=kkk", holder.getUrl() );
        Assert.assertEquals("http://127.0.0.1:8090/dddd/tttt?hill=hell&haven=heavy&kkk=kkk", holder.getUrl() );

        HolderHttpRequest httpRequest = HolderGetRequest.of("");
        Assert.assertTrue(httpRequest.urlHolder() instanceof UrlHolder);
        httpRequest.urlHolder(new DefaultPhpUrlHolder());
        Assert.assertTrue(httpRequest.urlHolder() instanceof PhpUrlHolder);
    }
    @Test
    public void testUrl2(){
        String url = "http://127.0.0.1:8090/{ddd}/{ttt}?hill=hell&haven=heavy";
        UrlHolder holder = new DefaultUrlHolder().setUrl(url).addRouteParam("ddd","111")
                .addRouteParam("ttt","222").addQueryParam("kkk","kkk");
        Assert.assertEquals("http://127.0.0.1:8090/111/222?hill=hell&haven=heavy&kkk=kkk", holder.getUrl() );
    }

    @Test
    public void testSetCompleteUrl1(){
        String url = "http://127.0.0.1:8090/dddd/tttt?hill=hell&haven=heavy";
        PhpUrlHolder holder = new DefaultPhpUrlHolder().setUrl(url);

        assertMultiValue (holder ,
                "http://127.0.0.1:8090/dddd/tttt?hill=hell&haven=heavy" ,
                Protocol.HTTP ,
                "127.0.0.1",8090,"/dddd/tttt" , 2);
    }
    @Test
    public void testSetCompleteUrl2(){
        String url = "http://127.0.0.1/dddd/tttt?hill=hell&haven=heavy";
        PhpUrlHolder holder = new DefaultPhpUrlHolder().setUrl(url);
        assertMultiValue (holder ,
                "http://127.0.0.1/dddd/tttt?hill=hell&haven=heavy" ,
                Protocol.HTTP ,
                "127.0.0.1",80,"/dddd/tttt" , 2);
    }
    @Test
    public void testSetCompleteUrl3(){
        String url = "https://127.0.0.1/dddd/tttt?hill=hell&haven=heavy";
        PhpUrlHolder holder = new DefaultPhpUrlHolder().setUrl(url);
        assertMultiValue (holder ,
                "https://127.0.0.1/dddd/tttt?hill=hell&haven=heavy" ,
                Protocol.HTTPS ,
                "127.0.0.1",443,"/dddd/tttt" , 2);
    }

    @Test
    public void testPartSet1(){
        PhpUrlHolder holder = new DefaultPhpUrlHolder()
                .protocol(Protocol.HTTPS)
                .host("localhost")
                .port(1234)
                .addQueryParam("k1" , "v1");
        assertMultiValue (holder ,
                "https://localhost:1234/?k1=v1" ,
                Protocol.HTTPS ,
                "localhost",1234,"/" , 1);
    }
    @Test
    public void testPartSet2(){
        PhpUrlHolder holder = new DefaultPhpUrlHolder()
                .protocol(Protocol.HTTPS)
                .host("localhost")
                .port(1234)
                .path("/sss", "/ddd" , "tt")
                .addQueryParam("k1" , "v1");
        assertMultiValue (holder ,
                "https://localhost:1234/sss/ddd/tt?k1=v1" ,
                Protocol.HTTPS ,
                "localhost",1234,"/sss/ddd/tt" , 1);
    }
    @Test
    public void testPartSet3(){
        PhpUrlHolder holder = new DefaultPhpUrlHolder()
//                .protocol(Protocol.HTTPS)
//                .host("localhost")
//                .port(1234)
                .path("/sss", "/ddd" , "tt")
                .addQueryParam("k1" , "v1");
        print(holder);
        assertMultiValue (holder ,
                "/sss/ddd/tt?k1=v1" ,
                null ,
                null,-1,"/sss/ddd/tt" , 1);
    }

    @Test
    public void testPartSet4(){
        PhpUrlHolder holder = new DefaultPhpUrlHolder()
//                .protocol(Protocol.HTTPS)
//                .host("localhost")
//                .port(1234)
                .path("/{sss}", "/{ddd}" , "tt")
                .addQueryParam("k1" , "v1").addRouteParam("sss" , "1111").addRouteParam("ddd" , "222");
        assertMultiValue (holder ,
                "/1111/222/tt?k1=v1" ,
                null ,
                null,-1,"/1111/222/tt" , 1);
    }

    @Test
    public void testRecalculate(){
        PhpUrlHolder holder = new DefaultPhpUrlHolder()
                .protocol(Protocol.HTTPS)
                .host("localhost")
                .port(1234)
                .path("/{sss}", "/{ddd}" , "tt")
                .addQueryParam("k1" , "v1").addRouteParam("sss" , "1111").addRouteParam("ddd" , "222");
        Assert.assertThat(holder.getUrl() , is("https://localhost:1234/1111/222/tt?k1=v1"));
        holder.protocol(Protocol.HTTP).addQueryParam("hh" , "hh");

        Assert.assertThat(holder.getUrl() , is("https://localhost:1234/1111/222/tt?k1=v1"));

        Assert.assertThat(holder.recalculateUrl() , is("http://localhost:1234/1111/222/tt?hh=hh&k1=v1"));
    }



    private void print(PhpUrlHolder holder){
        System.out.println(holder.getUrl());
        System.out.println(holder.protocol());
        System.out.println(holder.host());
        System.out.println(holder.port());
        System.out.println(holder.path());
        System.out.println(holder.queryParamHolder().getParams());
    }

    private void assertMultiValue(PhpUrlHolder holder ,
                                  String url , Protocol protocol , String host , int port , String path , int querySize){
        Assert.assertThat(holder.getUrl() , is(url));
        Assert.assertThat(holder.protocol() , is(protocol));
        Assert.assertThat(holder.host() , is(host));
        Assert.assertThat(holder.port() , is(port));
        Assert.assertThat(holder.path() , is(path));
        if(null != holder.queryParamHolder().getParams()){
            Assert.assertThat(holder.queryParamHolder().getParams().size() , is(querySize));
        }
    }
}
