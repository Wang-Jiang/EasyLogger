package space.wangjiang.easylogger.json;


import space.wangjiang.easylogger.StringUtil;
import space.wangjiang.easylogger.json.handler.IJsonHandler;
import space.wangjiang.easylogger.json.handler.JsonHandlerMapping;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 这里的方法本身不做格式化处理，格式化由jsonFormat处理
 */
public class JsonUtil {

    private static final String QUOTATION_MARK = "\"";

    /**
     * 对象转化json字符串
     * 这个需要递归调用
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String toJson(Object object) {
        if (object == null) {
            return "null";
        }
        //优先判断是否实现IJson
        Class<?> clazz = object.getClass();
        IJsonHandler jsonHandler = JsonHandlerMapping.get(clazz);
        if (jsonHandler != null) {
            return jsonHandler.toJson(object);
        }
        //基本类型
        if (object instanceof Integer) {
            return String.valueOf(((Integer) object).intValue());
        }
        if (object instanceof Long) {
            return String.valueOf(((Long) object).longValue());
        }
        if (object instanceof Double) {
            return String.valueOf(((Double) object).doubleValue());
        }
        if (object instanceof Float) {
            return String.valueOf(((Float) object).floatValue());
        }
        if (object instanceof Boolean) {
            return String.valueOf(((Boolean) object).booleanValue());
        }
        //BigInteger等大数类型
        if (object instanceof BigInteger || object instanceof BigDecimal) {
            return object.toString();
        }
        //其他类型
        if (object instanceof String) {
            return QUOTATION_MARK + object + QUOTATION_MARK;
        }
        if (object instanceof List) {
            return list2Json((List) object);
        }
        if (object instanceof Map) {
            return map2Json((Map) object);
        }
        if (object instanceof Set) {
            return set2Json((Set) object);
        }
        //各种时间类型，加上双引号
        if (object instanceof java.sql.Date) {
            SimpleDateFormat format = new SimpleDateFormat("\"yyyy-MM-dd\"");
            return format.format((java.sql.Date) object);
        }
        if (object instanceof Time) {
            SimpleDateFormat format = new SimpleDateFormat("\"HH:mm:ss\"");
            return format.format((Time) object);
        }
        if (object instanceof Date) {
            //Timestamp就当做Date处理
            SimpleDateFormat format = new SimpleDateFormat("\"yyyy-MM-dd HH:mm:ss\"");
            return format.format((Date) object);
        }
        if (object.getClass().isArray()) {
            Object[] array = object2Array(object);
            return array2Json(array);
        }
        //都不是的话，就是javabean了
        return bean2Json(object);
    }

    /**
     * Java标准实体类的转化
     * 通过setter方法获取值
     * 必须是标准的getter方法(不包含参数)，并且返回类型不能为自己本身
     */
    public static String bean2Json(Object bean) {
        StringBuilder sb = new StringBuilder("{");
        Method[] methods = bean.getClass().getMethods();
        boolean first = true;
        for (Method method : methods) {
            if (!method.getName().startsWith("get")) {
                continue;
            }
            //只处理getter方法
            String attrName = method.getName().substring(3);
            if (attrName.equals("Class")) {
                //getClass()
                continue;
            }
            Class<?>[] types = method.getParameterTypes();
            if (types.length > 0) {
                //getXX(int X)这种不是标准getter方法，不处理
                continue;
            }
            Class<?> returnType = method.getReturnType();
            if (returnType.getName().equals(bean.getClass().getName())) {
                //不能返回自己本身的类型，否则会导致递归调用StackOverflowError
                continue;
            }
            try {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                Object value = method.invoke(bean);
                sb.append(QUOTATION_MARK)
                        .append(StringUtil.firstCharToLowerCase(attrName))
                        .append(QUOTATION_MARK)
                        .append(":")
                        .append(toJson(value));  //递归处理
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * 这个key直接转换成字符串
     */
    public static String map2Json(Map map) {
        StringBuilder sb = new StringBuilder("{");
        Iterator iterator = map.entrySet().iterator();
        boolean first = true;
        while (iterator.hasNext()) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            Map.Entry entry = (Map.Entry) iterator.next();
            sb.append(QUOTATION_MARK).append(entry.getKey()).append(QUOTATION_MARK)
                    .append(":")
                    .append(toJson(entry.getValue()));   //递归调用
        }
        sb.append("}");

        return sb.toString();
    }

    public static String list2Json(List list) {
        return array2Json(list.toArray());
    }

    /**
     * set转化成array交给array2Json处理
     */
    public static String set2Json(Set set) {
        return array2Json(set.toArray());
    }

    /**
     * list2Json和set2Json都是交给这个方法处理的
     */
    public static String array2Json(Object[] array) {
        //这个相对比较麻烦，需要判断是否是基本类型的数组
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Object object : array) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append(toJson(object)); //递归调用
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * <pre>
     * Object转化为object[]
     * 自动判断是否是基本类型数组
     * </pre>
     */
    public static Object[] object2Array(Object object) {
        //是不是基本类型的数组
        //基本类型的数组需要单独处理，否则会报类型转换异常
        Object[] array;
        if (object instanceof int[]) {
            int[] intArray = (int[]) object;
            array = new Integer[intArray.length];
            for (int i = 0; i < intArray.length; i++) {
                array[i] = intArray[i];
            }
        } else if (object instanceof long[]) {
            long[] longArray = (long[]) object;
            array = new Long[longArray.length];
            for (int i = 0; i < longArray.length; i++) {
                array[i] = longArray[i];
            }
        } else if (object instanceof float[]) {
            float[] floatArray = (float[]) object;
            array = new Float[floatArray.length];
            for (int i = 0; i < floatArray.length; i++) {
                array[i] = floatArray[i];
            }
        } else if (object instanceof double[]) {
            double[] doubleArray = (double[]) object;
            array = new Double[doubleArray.length];
            for (int i = 0; i < doubleArray.length; i++) {
                array[i] = doubleArray[i];
            }
        } else if (object instanceof boolean[]) {
            boolean[] booleanArray = (boolean[]) object;
            array = new Boolean[booleanArray.length];
            for (int i = 0; i < booleanArray.length; i++) {
                array[i] = booleanArray[i];
            }
        } else {
            //不是基本类型的数组
            array = (Object[]) object;
        }
        return array;
    }

}
