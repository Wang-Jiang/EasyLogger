package space.wangjiang.easylogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangJiang on 2017/8/19.
 * 输出的日志等级
 * 把这个单独放在一起，要不然EasyLogger的代码有点多
 */
class LoggerLevel {

    /**
     * 显示状态
     */
    static final Map<Integer, Boolean> status = new HashMap<Integer, Boolean>();

    static {
        status.put(EasyLogger.INFO, Boolean.TRUE);
        status.put(EasyLogger.DEBUG, Boolean.TRUE);
        status.put(EasyLogger.WARN, Boolean.TRUE);
        status.put(EasyLogger.ERROR, Boolean.TRUE);
    }

    static void closeAll() {
        status.put(EasyLogger.INFO, Boolean.FALSE);
        status.put(EasyLogger.DEBUG, Boolean.FALSE);
        status.put(EasyLogger.WARN, Boolean.FALSE);
        status.put(EasyLogger.ERROR, Boolean.FALSE);
    }

}
