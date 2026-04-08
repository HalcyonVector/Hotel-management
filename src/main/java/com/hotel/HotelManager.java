package com.hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Business logic layer – keeps in-memory lists in sync with file storage.
 */
public class HotelManager {

    private final List<Room>    rooms    = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

    public HotelManager() {
        rooms.addAll(DataStore.loadRooms());
        bookings.addAll(DataStore.loadBookings());
        // No default room seeding — starts empty
    }

    // ── Room operations ────────────────────────────────────────

    public void addRoom(Room room) {
        rooms.add(room);
        DataStore.saveRooms(rooms);
    }

    public List<Room> getAllRooms()       { return Collections.unmodifiableList(rooms); }

    public List<Room> getAvailableRooms() {
        return rooms.stream().filter(Room::isAvailable).collect(Collectors.toList());
    }

    public Optional<Room> findRoom(int number) {
        return rooms.stream().filter(r -> r.getRoomNumber() == number).findFirst();
    }

    public boolean roomNumberExists(int number) {
        return rooms.stream().anyMatch(r -> r.getRoomNumber() == number);
    }

    // ── Booking operations ─────────────────────────────────────

    public Booking bookRoom(String customerName, String contact, int roomNumber,
                            LocalDate checkIn, LocalDate checkOut) throws Exception {
        Room room = findRoom(roomNumber)
            .orElseThrow(() -> new Exception("Room " + roomNumber + " not found."));
        if (!room.isAvailable())
            throw new Exception("Room " + roomNumber + " is already occupied.");
        if (!checkOut.isAfter(checkIn))
            throw new Exception("Check-out date must be after check-in date.");

        room.setAvailable(false);
        Booking b = new Booking(customerName, contact, room, checkIn, checkOut);
        bookings.add(b);
        DataStore.saveRooms(rooms);
        DataStore.saveBookings(bookings);
        return b;
    }

    public Booking checkout(int bookingId) throws Exception {
        Booking b = bookings.stream()
            .filter(x -> x.getBookingId() == bookingId && x.getStatus().equals("Active"))
            .findFirst()
            .orElseThrow(() -> new Exception("No active booking with ID " + bookingId));

        Room room = findRoom(b.getRoomNumber())
            .orElseThrow(() -> new Exception("Room not found."));

        // Do NOT call recalcBill() — preserve the total set by the Billing tab
        // (which includes any service charges added before checkout).
        b.setStatus("Checked Out");
        room.setAvailable(true);

        DataStore.saveRooms(rooms);
        DataStore.saveBookings(bookings);
        return b;
    }

    public List<Booking> getAllBookings()    { return Collections.unmodifiableList(bookings); }

    public List<Booking> getActiveBookings() {
        return bookings.stream().filter(b -> b.getStatus().equals("Active")).collect(Collectors.toList());
    }

    public Optional<Booking> findActiveBookingByRoom(int roomNumber) {
        return bookings.stream()
            .filter(b -> b.getRoomNumber() == roomNumber && b.getStatus().equals("Active"))
            .findFirst();
    }
}
