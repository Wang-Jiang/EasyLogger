package space.wangjiang.easylogger;

import org.junit.Test;

import java.util.*;

public class EasyLoggerTest {

    private static List<Date> list = new ArrayList<>();
    private static HashMap<String, String> map = new HashMap<>();
    private static HashSet<String> set = new HashSet<>();

    static {
        EasyLogger.showCallMethodAndLine(false);

        list.add(new Date(System.currentTimeMillis() - 16000000));
        list.add(new Date(System.currentTimeMillis() - 26000000));

        map.put("key1", "AAAAA");
        map.put("key2", "BBBBB");
        map.put("key3", "CCCCC");

        set.add("test1");
        set.add("test2");
    }

    public static void main(String[] args) {
        EasyLogger.showCallMethodAndLine(true);
        EasyLogger.debug("输出一条debug信息");
        EasyLogger.info("输出一条info信息");
        EasyLogger.showCallMethodAndLine(false);
        EasyLogger.setTag("EasyLogger");
        EasyLogger.showTime(false);
        EasyLogger.showThreadName(false);
        EasyLogger.warn("输出一条warn信息");
        EasyLogger.showCallMethodAndLine(true);
        EasyLogger.error("java.lang.ClassNotFoundException");
        EasyLogger.showThreadName(true);
        EasyLogger.json(new Person(11, "jack"));
        EasyLogger.json("{\"age\":23,\"name\":{\"firstName\":\"zhang\",\"lastName\":\"san\"}} ");
        EasyLogger.debug(list);
        EasyLogger.debug(map);
        EasyLogger.debug(new int[]{0, 1, 2, 3});
    }

    @Test
    public void debugTest() {
        EasyLogger.debug("debug");
        EasyLogger.debug("debug:%s", "this is a debug message");
        EasyLogger.debug(list);
        EasyLogger.debug(map);
        EasyLogger.debug(set);
    }

    @Test
    public void infoTest() {
        EasyLogger.info("info");
        EasyLogger.info("info:%s", "this is a info message");
        EasyLogger.info(list);
        EasyLogger.info(map);
        EasyLogger.info(set);
    }

    @Test
    public void warnTest() {
        EasyLogger.warn("warn");
        EasyLogger.warn("warn:%s", "this is a warn message");
        EasyLogger.warn(list);
        EasyLogger.warn(map);
        EasyLogger.warn(set);
    }

    @Test
    public void errorTest() {
        EasyLogger.error("error");
        EasyLogger.error("error:%s", "this is a error message");
        EasyLogger.error(list);
        EasyLogger.error(map);
        EasyLogger.error(set);
    }

    @Test
    public void jsonTest() {
        EasyLogger.json("{\"age\":23,\"name\":{\"firstName\":\"zhang\",\"lastName\":\"san\"}} ");
    }

    @Test
    public void iJsonTest() {
        Cat cat = new Cat("Tom", 3);
        EasyLogger.json(cat);
    }

    /**
     * 关闭日志后进行性能测试
     */
    @Test
    public void performanceTest() {
        EasyLogger.closeLog();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String pow = i + "的平方根是" + Math.pow(i, 0.5);
            String square = i + "的平方是" + Math.pow(i, 2);
            EasyLogger.debug(pow + "\n" + square);
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - start));
    }

    /**
     * 完全不带有EasyLogger的性能测试
     */
    @Test
    public void performanceTestWithoutEasyLogger() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String pow = i + "的平方根是" + Math.pow(i, 0.5);
            String square = i + "的平方是" + Math.pow(i, 2);
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - start));
    }

}
