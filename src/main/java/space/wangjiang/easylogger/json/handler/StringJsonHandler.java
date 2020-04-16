package space.wangjiang.easylogger.json.handler;

/**
 * JSON 不支持换行符，同时把\转化为\\
 * 但是控制台显示\n不如直接换行看得直观
 */
public class StringJsonHandler implements IJsonHandler<String> {

    @Override
    public String toJson(String value) {
        value = '"' + value + '"';
        return value.replaceAll("\\\\", "\\\\\\\\").replaceAll("\n", "\\\\n");
    }

}
