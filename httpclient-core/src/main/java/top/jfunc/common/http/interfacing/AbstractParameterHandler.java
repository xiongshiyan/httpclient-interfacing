package top.jfunc.common.http.interfacing;

import top.jfunc.common.http.base.FormFile;
import top.jfunc.common.http.request.ChangeableStringBodyRequest;
import top.jfunc.common.http.request.FormRequest;
import top.jfunc.common.http.request.HttpRequest;
import top.jfunc.common.http.request.UploadRequest;
import top.jfunc.common.utils.MultiValueMap;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.Objects;

/**
 * 参数处理器定义及对应注解的实现
 * @author xiongshiyan at 2019/5/24 , contact me with email yanshixiong@126.com or phone 15208384257
 */
abstract class AbstractParameterHandler<P>{
    abstract void apply(HttpRequest r, P p);

    /**
     * 处理header的
     * @see top.jfunc.common.http.annotation.parameter.Header
     */
    static final class Header  extends AbstractParameterHandler<String> {
        private final String name;
        Header(String name) {
            this.name = Objects.requireNonNull(name);
        }

        @Override
        public void apply(HttpRequest httpRequest, String value) {
            if (value == null) {
                return; // Skip null values.
            }
            httpRequest.addHeader(name, value);
        }
    }
    /**
     * 处理headerMap的
     * @see top.jfunc.common.http.annotation.parameter.HeaderMap
     */
    static final class HeaderMap  extends AbstractParameterHandler<Map<String , String>> {

        @Override
        public void apply(HttpRequest httpRequest, Map<String , String> headers) {
            if (headers == null || headers.isEmpty()) {
                return; // Skip null values.
            }
            if(headers instanceof MultiValueMap){
                MultiValueMap<String, String> multiValueMap = (MultiValueMap) headers;
                multiValueMap.forEachKeyValue(httpRequest::addHeader);
            }
            headers.forEach(httpRequest::addHeader);
        }
    }

    /**
     * 处理查询参数的
     * @see top.jfunc.common.http.annotation.parameter.Query
     */
    static final class Query  extends AbstractParameterHandler<Object> {
        private final String name;
        Query(String name) {
            this.name = Objects.requireNonNull(name);
        }

        @Override
        public void apply(HttpRequest httpRequest, Object value) {
            if (value == null) {
                return; // Skip null values.
            }
            httpRequest.addQueryParam(name, value.toString());
        }
    }
    /**
     * 处理headerMap的
     * @see top.jfunc.common.http.annotation.parameter.QueryMap
     */
    static final class QueryMap  extends AbstractParameterHandler<Map<String , String>> {

        @Override
        public void apply(HttpRequest httpRequest, Map<String , String> querys) {
            if (querys == null || querys.isEmpty()) {
                return; // Skip null values.
            }
            if(querys instanceof MultiValueMap){
                MultiValueMap<String, String> multiValueMap = (MultiValueMap) querys;
                multiValueMap.forEachKeyValue(httpRequest::addQueryParam);
            }
            querys.forEach(httpRequest::addQueryParam);
        }
    }
    /**
     * 处理路径参数的
     * @see top.jfunc.common.http.annotation.parameter.Path
     */
    static final class Route extends AbstractParameterHandler<Object> {
        private final String name;
        Route(String name) {
            this.name = Objects.requireNonNull(name);
        }

        @Override
        public void apply(HttpRequest httpRequest, Object value) {
            if (value == null) {
                return; // Skip null values.
            }
            httpRequest.addRouteParam(name, value.toString());
        }
    }
    /**
     * 处理headerMap的
     * @see top.jfunc.common.http.annotation.parameter.PathMap
     */
    static final class RouteMap  extends AbstractParameterHandler<Map<String , String>> {

        @Override
        public void apply(HttpRequest httpRequest, Map<String , String> routes) {
            if (routes == null || routes.isEmpty()) {
                return; // Skip null values.
            }
            routes.forEach(httpRequest::addRouteParam);
        }
    }
    /**
     * 处理Part
     * @see top.jfunc.common.http.annotation.parameter.Part
     */
    static final class Part extends AbstractParameterHandler<Object> {
        private final String name;
        Part(String name) {
            this.name = Objects.requireNonNull(name);
        }

        @Override
        public void apply(HttpRequest httpRequest, Object value)  {
            if (value == null) {
                return; // Skip null values.
            }

            UploadRequest uploadRequest = (UploadRequest)httpRequest;
            if(value instanceof FormFile){
                uploadRequest.addFormFile((FormFile)value);
            }else if(value.getClass().isArray() && Array.get(value , 0) instanceof FormFile){
                int length = Array.getLength(value);
                FormFile[] formFiles = new FormFile[length];
                System.arraycopy(value , 0 , formFiles , 0 , length);
                uploadRequest.addFormFile(formFiles);
            }else if(value instanceof Iterable){
                Iterable<FormFile> formFiles = (Iterable<FormFile>) value;
                formFiles.forEach(uploadRequest::addFormFile);
            }else {
                uploadRequest.addFormParam(name, value.toString());
            }
        }
    }
    /**
     * 处理field的
     * @see top.jfunc.common.http.annotation.parameter.Field
     */
    static final class Field  extends AbstractParameterHandler<Object> {
        private final String name;
        Field(String name) {
            this.name = Objects.requireNonNull(name);
        }

        @Override
        public void apply(HttpRequest httpRequest, Object value) {
            if (value == null) {
                return; // Skip null values.
            }
            ((FormRequest)httpRequest).addFormParam(name, value.toString());
        }
    }
    /**
     * 处理fieldMap的
     * @see top.jfunc.common.http.annotation.parameter.FieldMap
     */
    static final class FieldMap  extends AbstractParameterHandler<Map<String , String>> {
        @Override
        public void apply(HttpRequest httpRequest, Map<String , String> fields) {
            if (fields == null || fields.isEmpty()) {
                return; // Skip null values.
            }
            FormRequest formRequest = (FormRequest) httpRequest;
            if(fields instanceof MultiValueMap){
                MultiValueMap<String, String> multiValueMap = (MultiValueMap) fields;
                multiValueMap.forEachKeyValue(formRequest::addFormParam);
            }
            fields.forEach(formRequest::addFormParam);
        }
    }

    /**
     * 处理Body的
     * @see top.jfunc.common.http.annotation.parameter.Body
     */
    static final class Body  extends AbstractParameterHandler<String> {

        @Override
        public void apply(HttpRequest httpRequest, String value) {
            if (value == null) {
                return; // Skip null values.
            }
            ((ChangeableStringBodyRequest)httpRequest).setBody(value);
        }
    }
    /**
     * 处理请求URL的
     * @see top.jfunc.common.http.annotation.parameter.Url
     */
    static final class Url  extends AbstractParameterHandler<Object> {

        @Override
        public void apply(HttpRequest httpRequest, Object value) {
            if (value == null) {
                return; // Skip null values.
            }
            //支持URL和String
            httpRequest.setUrl(value.toString());
        }
    }
}
