package space.wangjiang.easylogger;

import space.wangjiang.easylogger.json.handler.IJsonHandler;

public class CatJsonHandler implements IJsonHandler<Cat> {
    @Override
    public String toJson(Cat value) {
         return "{" +
                "\"name\"=\"" + value.getName() + '\"' +
                ", \"age\"=\"" + value.getAge() + "\"}";
    }
}
