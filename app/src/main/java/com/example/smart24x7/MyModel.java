package com.example.smart24x7;

public class MyModel {
    private String id;
    private String name;
    private String phoneno;

    public MyModel(String id, String name, String phoneno) {
        this.id = id;
        this.name = name;
        this.phoneno = phoneno;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneno;
    }
}
