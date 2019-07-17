package sunkey.common.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Sunkey
 */
public class JSON {

    public static class JSONException extends RuntimeException {
        public JSONException() {
            super();
        }

        public JSONException(String message) {
            super(message);
        }

        public JSONException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = init();
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    private static ObjectMapper init() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 发现未知属性时忽略
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        // 禁止写入null
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 允许特殊字符
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 日期序列化格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 默认时区
        objectMapper.setTimeZone(TimeZone.getDefault());
        _serialByFields(objectMapper);
        return objectMapper;
    }

    public static void serialByDefault() {
        // 使用默认序列化规则
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance());
    }

    public static void serialByFields() {
        _serialByFields(objectMapper);
    }

    private static void _serialByFields(ObjectMapper _objectMapper) {
        // 使用Fields序列化
        _objectMapper.setVisibility(
                VisibilityChecker.Std.defaultInstance()
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));
    }

    public static <T> T parseObject(String text, JavaType type) throws JSONException {
        try {
            return objectMapper.readValue(text, type);
        } catch (Throwable ex) {
            throw new JSONException(ex.getMessage(), ex);
        }
    }

    public static <T> T parseObject(String text, Class<T> type) throws JSONException {
        return parseObject(
                text,
                objectMapper.getTypeFactory().constructType(type));
    }

    public static <T> List<T> parseList(String text, Class<T> type) {
        return parseObject(
                text,
                objectMapper
                        .getTypeFactory()
                        .constructCollectionType(
                                ArrayList.class,
                                type));
    }


    public static Map<String, Object> parseMap(String text) {
        return parseMap(text, String.class, Object.class);
    }

    public static <K, V> Map<K, V> parseMap(String text, Class<K> k, Class<V> v) {
        return parseObject(text,
                objectMapper
                        .getTypeFactory()
                        .constructMapType(
                                HashMap.class,
                                k, v));
    }

    public static String toJSONString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Throwable ex) {
            throw new JSONException(ex.getMessage(), ex);
        }
    }

    public static void writeJSONString(OutputStream out, Object object) {
        try {
            objectMapper.writeValue(out, object);
        } catch (Throwable ex) {
            throw new JSONException(ex.getMessage(), ex);
        }
    }

    public static ObjectNode parseJsonObject(String json) {
        try {
            return objectMapper.readValue(json, ObjectNode.class);
        } catch (Throwable e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public static ArrayNode parseJsonArray(String json) {
        try {
            return objectMapper.readValue(json, ArrayNode.class);
        } catch (Throwable e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public static String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Throwable e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public static ObjectNode createNode() {
        return objectMapper.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return objectMapper.createArrayNode();
    }
}
