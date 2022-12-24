package com.example.exe1;


public class UserDetails {
    private String name;
    private int score;
    private Double lag;
    private Double lat;

    public UserDetails( ) {
    }

    public Double getLag() {
        return lag;
    }

    public UserDetails setLag(Double lag) {
        this.lag = lag;
        return this;
    }

    public Double getLat() {
        return lat;
    }

    public UserDetails setLat(Double lat) {
        this.lat = lat;
        return this;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public UserDetails setName(String name) {
        this.name = name;
        return this;
    }

    public UserDetails setScore(int score) {
        this.score = score;
        return this;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
