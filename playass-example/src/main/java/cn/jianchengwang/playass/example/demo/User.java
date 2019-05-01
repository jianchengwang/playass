package cn.jianchengwang.playass.example.demo;

import lombok.Data;

@Data
public class User {

    private String name;
    private Integer age;

    public User() {
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
