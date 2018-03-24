package space.wangjiang.easylogger;

import space.wangjiang.easylogger.json.IJson;

public class Cat implements IJson {

    private String name;
    private int age;

    public Cat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toJson() {
        return "{}";
    }
}
