package com.hotel;

import java.io.Serializable;
import java.time.LocalDate;

public class Booking implements Serializable {
    private static int counter = 1;

    private int bookingId;
    private String customerName;
    private String contactNumber;
    private int roomNumber;
    private String roomType;
    private double pricePerDay;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalBill;
    private String status; // Active / Checked Out

    public Booking(String customerName, String contactNumber, Room room, LocalDate checkIn, LocalDate checkOut) {
        this.bookingId      = counter++;
        this.customerName   = customerName;
        this.contactNumber  = contactNumber;
        this.roomNumber     = room.getRoomNumber();
        this.roomType       = room.getRoomType();
        this.pricePerDay    = room.getPricePerDay();
        this.checkInDate    = checkIn;
        this.checkOutDate   = checkOut;
        long nights         = checkOut.toEpochDay() - checkIn.toEpochDay();
        this.totalBill      = nights * pricePerDay;
        this.status         = "Active";
    }

    // CSV constructor (load from file)
    private Booking() {}

    public static void setCounter(int c) { counter = c; }

    public int    getBookingId()     { return bookingId; }
    public String getCustomerName()  { return customerName; }
    public String getContactNumber() { return contactNumber; }
    public int    getRoomNumber()    { return roomNumber; }
    public String getRoomType()      { return roomType; }
    public double getPricePerDay()   { return pricePerDay; }
    public LocalDate getCheckInDate()  { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public double getTotalBill()     { return totalBill; }
    public String getStatus()        { return status; }

    public void setStatus(String status)       { this.status = status; }
    public void setCheckOutDate(LocalDate d)   { this.checkOutDate = d; }
    public void setTotalBill(double b)         { this.totalBill = b; }

    public void recalcBill() {
        long nights = checkOutDate.toEpochDay() - checkInDate.toEpochDay();
        totalBill   = Math.max(nights, 1) * pricePerDay;
    }

    @Override
    public String toString() {
        return bookingId + "," + customerName + "," + contactNumber + ","
             + roomNumber + "," + roomType + "," + pricePerDay + ","
             + checkInDate + "," + checkOutDate + "," + totalBill + "," + status;
    }

    public static Booking fromString(String line) {
        String[] p = line.split(",");
        Booking b = new Booking();
        b.bookingId     = Integer.parseInt(p[0].trim());
        b.customerName  = p[1].trim();
        b.contactNumber = p[2].trim();
        b.roomNumber    = Integer.parseInt(p[3].trim());
        b.roomType      = p[4].trim();
        b.pricePerDay   = Double.parseDouble(p[5].trim());
        b.checkInDate   = LocalDate.parse(p[6].trim());
        b.checkOutDate  = LocalDate.parse(p[7].trim());
        b.totalBill     = Double.parseDouble(p[8].trim());
        b.status        = p[9].trim();
        if (b.bookingId >= counter) counter = b.bookingId + 1;
        return b;
    }
}
