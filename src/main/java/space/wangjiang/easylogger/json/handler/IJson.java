package space.wangjiang.easylogger.json.handler;

/**
 * 自定义转json处理方式
 * 如果类实现该接口，JsonUtil会直接调用toJson()
 */
public interface IJson {

    String toJson();

}
