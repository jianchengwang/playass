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

    public User(Integer id,String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
