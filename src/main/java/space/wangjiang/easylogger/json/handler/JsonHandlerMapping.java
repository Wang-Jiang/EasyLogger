package space.wangjiang.easylogger.json.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义类型处理
 */
public class JsonHandlerMapping {

    private static final Map<Class<?>, IJsonHandler<?>> MAPPING = new HashMap<>();

    /**
     * 注册
     */
    public static <T> void register(Class<T> clazz, IJsonHandler<T> json) {
        MAPPING.put(clazz, json);
    }

    @SuppressWarnings("unchecked")
    public static <T> IJsonHandler<T> get(Class<T> clazz) {
        return (IJsonHandler<T>) MAPPING.get(clazz);
    }

}
