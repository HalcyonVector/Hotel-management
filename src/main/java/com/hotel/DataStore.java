package com.hotel;

import java.io.*;
import java.util.*;

/**
 * Handles all file I/O for persistent storage.
 * Rooms  → rooms.csv
 * Bookings → bookings.csv
 */
public class DataStore {

    private static final String ROOMS_FILE    = "rooms.csv";
    private static final String BOOKINGS_FILE = "bookings.csv";

    // ── Rooms ──────────────────────────────────────────────────

    public static void saveRooms(List<Room> rooms) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ROOMS_FILE))) {
            // Write header
            pw.println("roomNumber,roomType,pricePerDay,available");
            // Write data
            for (Room r : rooms) pw.println(r.toString());
        } catch (IOException e) {
            System.err.println("Error saving rooms: " + e.getMessage());
        }
    }

    public static List<Room> loadRooms() {
        List<Room> list = new ArrayList<>();
        File f = new File(ROOMS_FILE);
        if (!f.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header row
                }
                if (!line.isBlank()) list.add(Room.fromString(line));
            }
        } catch (IOException e) {
            System.err.println("Error loading rooms: " + e.getMessage());
        }
        return list;
    }

    // ── Bookings ───────────────────────────────────────────────

    public static void saveBookings(List<Booking> bookings) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
            // Write header
            pw.println("bookingId,customerName,contactNumber,roomNumber,roomType,pricePerDay,checkInDate,checkOutDate,totalBill,status");
            // Write data
            for (Booking b : bookings) pw.println(b.toString());
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }

    public static List<Booking> loadBookings() {
        List<Booking> list = new ArrayList<>();
        File f = new File(BOOKINGS_FILE);
        if (!f.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header row
                }
                if (!line.isBlank()) list.add(Booking.fromString(line));
            }
        } catch (IOException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
        }
        return list;
    }
}
