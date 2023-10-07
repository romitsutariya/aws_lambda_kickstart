package org.example.hotel;

import java.util.Arrays;
import java.util.List;

public class HotelDAO {
    private final List<String> rooms;
    public HotelDAO() {
        rooms= Arrays.asList("101","102","103","104","105","106","107","108","109","110");
    }
    public boolean checkAvailability(String roomNumber) {
        return rooms.contains(roomNumber);
    }

    public boolean removeBookRoom(String roomNumber) {
        return rooms.remove(roomNumber);
    }

    public boolean addBookRoom(String roomNumber) {
        return rooms.add(roomNumber);
    }

    public List<String> getRooms() {
        return rooms;
    }

}

