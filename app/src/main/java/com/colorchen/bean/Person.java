package com.colorchen.bean;

/**
 * test 数据结构
 * Author ChenQ on 2017/6/23
 * email：wxchenq@yutong.com
 */
public class Person {
    private String name, age;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Person(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public Person(String name, String age, int type) {
        this.name = name;
        this.age = age;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

}
