package fitnesscenter.gymmembershipsystem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RoomSchedule {
    private final String roomName;
    private final Map<String, String> bookingsBySlot;

    public RoomSchedule(String roomName) {
        if (roomName == null || roomName.trim().isEmpty()) {
            throw new IllegalArgumentException("Room name cannot be empty.");
        }
        this.roomName = roomName;
        this.bookingsBySlot = new HashMap<>();
    }

    public String getRoomName() {
        return roomName;
    }

    public void bookSlot(String timeSlot, String purpose) {
        if (timeSlot == null || timeSlot.trim().isEmpty()) {
            throw new IllegalArgumentException("Time slot cannot be empty.");
        }
        if (purpose == null || purpose.trim().isEmpty()) {
            throw new IllegalArgumentException("Purpose cannot be empty.");
        }
        if (bookingsBySlot.containsKey(timeSlot.trim())) {
            throw new RoomUnavailableException("Room is already booked at slot: " + timeSlot);
        }
        bookingsBySlot.put(timeSlot.trim(), purpose.trim());
    }

    public Map<String, String> getBookingsBySlot() {
        return Collections.unmodifiableMap(bookingsBySlot);
    }

    public String getSummary() {
        if (bookingsBySlot.isEmpty()) {
            return roomName + ": No reservations";
        }

        StringBuilder builder = new StringBuilder(roomName + " reservations:");
        for (Map.Entry<String, String> entry : bookingsBySlot.entrySet()) {
            builder.append("\n- ").append(entry.getKey()).append(" -> ").append(entry.getValue());
        }
        return builder.toString();
    }
}

