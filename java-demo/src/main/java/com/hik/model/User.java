package com.hik.model;

/**
 * 用户模型
 *
 * @author wangjinchang5
 * @date 2020/8/4 11:14
 */
public class User {
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     * <li>0：女</li>
     * <li>1：男</li>
     */
    private Integer gender;
    /**
     * 年龄
     */
    private Integer age;

    public User() {
    }

    public User(String name, Integer gender, Integer age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + '\"' +
                ", \"gender\":\"" + gender + '\"' +
                ", \"age\":\"" + age + '\"' +
                '}';
    }
}
