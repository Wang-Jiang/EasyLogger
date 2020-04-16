package space.wangjiang.easylogger.json.handler;

/**
 * 自定义转json处理方式
 */
public interface IJsonHandler<T> {

    String toJson(T value);

}
