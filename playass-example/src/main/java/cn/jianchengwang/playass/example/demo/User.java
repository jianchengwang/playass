package cn.jianchengwang.playass.example.demo;

import lombok.Data;

@Data
public class User {

    private Integer id;
    private String name;
    private Integer age;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
