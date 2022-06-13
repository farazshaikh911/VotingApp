package com.example.voting;

public class Members {
    private String name;
    private int count;
    private String id;
    public Members() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Members(String id, String name, int count) {
        this.name = name;
        this.count = count;
        this.id = id;
    }
    public Members(String id, int count) {
        this.count = count;
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


}
