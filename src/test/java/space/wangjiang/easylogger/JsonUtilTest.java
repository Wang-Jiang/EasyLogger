package space.wangjiang.easylogger;

import org.junit.Before;
import org.junit.Test;

public class JsonUtilTest {

    @Before
    public void init() {
        EasyLogger.showCallMethodAndLine(false);
    }

    @Test
    public void toJsonTest() {
        EasyLogger.json(JsonUtil.toJson(new Person(10, "Jack")));
        EasyLogger.json(JsonUtil.toJson(new int[]{3, 6, 5, 4,}));
    }

}
