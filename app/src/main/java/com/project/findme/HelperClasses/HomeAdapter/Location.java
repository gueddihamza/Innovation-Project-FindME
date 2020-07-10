package com.project.findme.HelperClasses.HomeAdapter;

import com.google.android.gms.maps.model.Marker;

public class Location {
    private String key;
    private Marker marker;
    private String username;

    public Location(String key, Marker marker, String username) {
        this.key = key;
        this.marker = marker;
        this.username = username;
    }

    public Location() {
    }

    public String getKey() {
        return key;
    }

    public Marker getMarker() {
        return marker;
    }

    public String getUsername() {
        return username;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Location{" +
                "key='" + key + '\'' +
                ", marker=" + marker.toString() +
                ", username='" + username + '\'' +
                '}';
    }
}
