package com.traveloid.model;

public class Hike {

    private final int id;
    private final String name;
    private final String distance;
    private final int image;

    public Hike(int id, String name, String distance, int image) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDistance() {
        return distance;
    }

    public int getImage() {
        return image;
    }
}
