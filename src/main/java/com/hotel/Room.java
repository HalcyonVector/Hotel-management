package com.hotel;

import java.io.Serializable;

public class Room implements Serializable {
    private int roomNumber;
    private String roomType;   // Single, Double, Deluxe
    private double pricePerDay;
    private boolean available;

    public Room(int roomNumber, String roomType, double pricePerDay) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerDay = pricePerDay;
        this.available = true;
    }

    public int getRoomNumber()          { return roomNumber; }
    public String getRoomType()         { return roomType; }
    public double getPricePerDay()      { return pricePerDay; }
    public boolean isAvailable()        { return available; }
    public String getAvailabilityStatus(){ return available ? "Available" : "Occupied"; }

    public void setAvailable(boolean available) { this.available = available; }
    public void setPricePerDay(double p)        { this.pricePerDay = p; }

    @Override
    public String toString() {
        return roomNumber + "," + roomType + "," + pricePerDay + "," + available;
    }

    public static Room fromString(String line) {
        String[] p = line.split(",");
        Room r = new Room(Integer.parseInt(p[0].trim()), p[1].trim(), Double.parseDouble(p[2].trim()));
        r.setAvailable(Boolean.parseBoolean(p[3].trim()));
        return r;
    }
}
