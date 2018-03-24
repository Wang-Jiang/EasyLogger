package space.wangjiang.easylogger;

import space.wangjiang.easylogger.json.JsonUtil;
import space.wangjiang.easylogger.ui.UI;
import space.wangjiang.easylogger.ui.DefaultUI;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EasyLogger {

    private static String TOP_BORDER;
    private static String LEFT_BORDER;
    private static String DIVIDING_LINE;
    private static String BOTTOM_BORDER;

    static {
        setUI(new DefaultUI());
    }

    private static String TAG = null;

    static final int INFO = 0;
    static final int DEBUG = 1;
    static final int WARN = 2;
    static final int ERROR = 3;

    //是否显示线程名和是否显示调用方法名和行数
    private static boolean showThreadName = true;
    private static boolean showCallMethodAndLine = true;

    private static boolean showTime = true;

    private static boolean isClosed = false;

    private EasyLogger() {
        //禁止实例化
    }

    public static void info(String msg) {
        printIDWE(msg, INFO);
    }

    public static void info(String msg, Object... args) {
        printIDWE(String.format(msg, args), INFO);
    }

    public static void info(Object object) {
        info(Util.object2String(object));
    }

    public static void debug(String msg) {
        printIDWE(msg, DEBUG);
    }

    public static void debug(String msg, Object... args) {
        printIDWE(String.format(msg, args), DEBUG);
    }

    /**
     * 自动识别List、Map、Set等类型
     */
    public static void debug(Object object) {
        debug(Util.object2String(object));
    }

    public static void error(String msg) {
        printIDWE(msg, ERROR);
    }

    public static void error(String msg, Object... args) {
        printIDWE(String.format(msg, args), ERROR);
    }

    public static void error(Object object) {
        error(Util.object2String(object));
    }

    public static void warn(String msg) {
        printIDWE(msg, WARN);
    }

    public static void warn(String msg, Object... args) {
        printIDWE(String.format(msg, args), WARN);
    }

    public static void warn(Object object) {
        warn(Util.object2String(object));
    }

    /**
     * 格式化json字符串，已经格式好的就不要调用这个方法了
     */
    public static void json(String json) {
        debug(Util.formatJson(json));
    }

    /**
     * 将对象转化为json字符串打印
     */
    public static void json(Object object) {
        json(JsonUtil.toJson(object));
    }

    /**
     * 无论是info，debug，warn还是error都是调用这个的
     * 然后它去调用实际的打印内容
     */
    private static void printIDWE(String msg, int level) {
        if (isClosed) return;
        //对应的level设置为不显示
        if (!LoggerLevel.status.get(level)) return;

        String tag = TAG;
        if (tag == null) {
            //如果不设置TAG的话，默认是调用的类名
            tag = getCallClassName();
        }
        print(tag, TOP_BORDER, level);    //顶上的线
        if (showThreadName) {
            printThreadNameArea(tag, level);
        }
        if (showCallMethodAndLine) {
            printCallMethodAndLineArea(tag, level);
        }
        printMsgArea(tag, msg, level);
    }

    /**
     * 打印如下内容
     * 时间是可选的
     * D/Main ║Thread: main (2017-08-19 10:31:13)
     * D/Main ╟────────────────────────────────────────────────────
     */
    private static void printThreadNameArea(String tag, int level) {
        String time = "";
        if (showTime) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            time = " (" + format.format(new Date()) + ")";
        }
        String threadName = Thread.currentThread().getName();
        print(tag, LEFT_BORDER + "Thread: " + threadName + time, level);
        print(tag, DIVIDING_LINE, level);
    }

    /**
     * 最底层的打印方法
     *
     * @param tag   tag
     * @param line  要打印的内容
     * @param level 打印的级别
     */
    private static void print(String tag, String line, int level) {
        StringBuilder sb;
        switch (level) {
            case INFO:
                sb = new StringBuilder("INFO/");
                break;
            case DEBUG:
                sb = new StringBuilder("DEBUG/");
                break;
            case WARN:
                sb = new StringBuilder("WARN/");
                break;
            case ERROR:
                sb = new StringBuilder("ERROR/");
                break;
            default:
                sb = new StringBuilder("UNKNOWN-LEVEL/");
                break;
        }
        sb.append(tag).append(" ").append(line);
        System.out.println(sb.toString());
    }

    /**
     * 这个是利用异常获取调用的方法和行数的
     * 打印如下内容
     * D/Main ║EasyLogger.debug(EasyLogger.java:38)
     * D/Main ║  Main.main(Main.java:20)
     * D/Main ╟────────────────────────────────────────────────────
     */
    private static void printCallMethodAndLineArea(String tag, int level) {
        Exception exception = new Exception();
        StackTraceElement[] elements = exception.getStackTrace();
//        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        int indent = 0;
        for (StackTraceElement element : elements) {
            if (element.getClassName().equals(EasyLogger.class.getName()) || element.getClassName().equals("java.lang.Thread")) {
                //这个目的是不打印EasyLogger内部的调用信息
                continue;
            }
            String line = LEFT_BORDER +
                    getSomeSpace(indent) +
                    element.getClassName() +
                    "." +
                    element.getMethodName() +
                    "(" +
                    element.getFileName() +
                    ":" +
                    element.getLineNumber() +
                    ")";
            print(tag, line, level);
            indent++;   //下一个方法的缩进+1
        }
        print(tag, DIVIDING_LINE, level);
    }

    /**
     * <pre>
     * 打印信息区域
     * 支持/n自动换行处理
     * D/Main ║msg
     * D/Main ╚════════════════════════════════════════════════════
     * </pre>
     */
    private static void printMsgArea(String callClassName, String msg, int level) {
        if (msg != null) {
            String[] array = msg.split("\n");
            for (String line : array) {
                print(callClassName, LEFT_BORDER + line, level);
            }
        } else {
            print(callClassName, LEFT_BORDER + "msg is NULL", level);
        }
        print(callClassName, BOTTOM_BORDER, level);
    }

    /**
     * 获取指定个空格
     */
    private static String getSomeSpace(int n) {
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < n; i++) {
            space.append("  ");
        }
        return space.toString();
    }

    /**
     * 获取调用的类的名称
     */
    private static String getCallClassName() {
//        //原先是通过异常获取
        Exception exception = new Exception();
        StackTraceElement[] elements = exception.getStackTrace();
//        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        String className = elements[elements.length - 1].getClassName();
        String[] array = className.split("\\.");
        return array[array.length - 1];
    }

    /**
     * 设置自定义的tag
     */
    public static void setTag(String tag) {
        TAG = tag;
    }

    /**
     * 移除自定义的TAG，使用默认的TAG，也可以调用.setTag(null)
     *
     * @see #setTag(String)
     */
    public static void removeTag() {
        TAG = null;
    }

    /**
     * 打开日志
     */
    public static void openLog() {
        isClosed = false;
    }

    /**
     * 关闭日志
     */
    public static void closeLog() {
        isClosed = true;
    }

    /**
     * 是否显示线程名称
     */
    public static void showThreadName(boolean show) {
        showThreadName = show;
    }

    /**
     * 是否显示调用方法和行数
     */
    public static void showCallMethodAndLine(boolean show) {
        showCallMethodAndLine = show;
    }

    public static void showTime(boolean show) {
        showTime = show;
    }

    /**
     * 改变外观
     */
    public static void setUI(UI ui) {
        TOP_BORDER = ui.topBorder();
        LEFT_BORDER = ui.leftBorder();
        DIVIDING_LINE = ui.dividingLine();
        BOTTOM_BORDER = ui.bottomBorder();
    }

    /**
     * 配置打印哪些类别的log
     */
    public static void logLevel(int... levels) {
        LoggerLevel.closeAll();
        if (levels == null) return;
        for (int level : levels) {
            LoggerLevel.status.put(level, Boolean.TRUE);
        }
    }

}
