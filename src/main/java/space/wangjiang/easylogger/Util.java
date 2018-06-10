package space.wangjiang.easylogger;

import space.wangjiang.easylogger.json.JsonUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 辅助类
 */
public class Util {

    /**
     * List转化成字符串
     */
    public static <T> String list2String(List<T> list) {
        StringBuilder sb = new StringBuilder("List.size() = " + list.size());
        sb.append("\n{\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append("  [").append(i).append("]: ")
                    .append(list.get(i))
                    .append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Array转化成字符串
     */
    public static String array2String(Object[] array) {
        StringBuilder sb = new StringBuilder("Array.length = " + array.length);
        sb.append("\n{\n");
        for (int i = 0; i < array.length; i++) {
            sb.append("  [").append(i).append("]: ")
                    .append(array[i])
                    .append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    public static <K, V> String map2String(Map<K, V> map) {
        StringBuilder sb = new StringBuilder("Map.size() = " + map.size());
        sb.append("\n{\n");
        Set<Map.Entry<K, V>> keySet = map.entrySet();
        for (Map.Entry<K, V> entry : keySet) {
            sb.append("  ")
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    public static <E> String set2String(Set<E> set) {
        StringBuilder sb = new StringBuilder("Set.size() = " + set.size());
        sb.append("\n{\n");
        for (E e : set) {
            sb.append("  ")
                    .append(e)
                    .append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * <pre>
     * 判断类型，做相应的处理
     * 比较麻烦的是数组类型的
     * 基本类型的数组不能强转成Object[]
     * </pre>
     */
    public static String object2String(Object object) {
        if (object == null) return "null";
        String result;
        if (object instanceof List) {
            result = Util.list2String((List<?>) object);
        } else if (object instanceof Map) {
            result = Util.map2String((Map<?, ?>) object);
        } else if (object instanceof Set) {
            result = Util.set2String((Set<?>) object);
        } else if (object.getClass().isArray()) {
            Object[] array = JsonUtil.object2Array(object);
            result = Util.array2String(array);
        } else {
            //其他类型直接调用toString()方法
            result = object.toString();
        }
        return result;
    }

    /**
     * 格式化json
     */
    public static String formatJson(String json) {
        if (json == null) return "null";
        int level = 0;
        StringBuilder sb = new StringBuilder();
        for (char ch : json.toCharArray()) {
            if (ch == '{' || ch == '[') {
                level++;
                sb.append(ch)
                        .append("\n")
                        .append(indent(level));
            } else if (ch == '}' || ch == ']') {
                level--;
                sb.append("\n")
                        .append(indent(level))
                        .append(ch);
            } else if (ch == ',') {
                sb.append(ch)
                        .append("\n")
                        .append(indent(level));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * 缩进n个空格
     */
    public static String indent(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }

}
