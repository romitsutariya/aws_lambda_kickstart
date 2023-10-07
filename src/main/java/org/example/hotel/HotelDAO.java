package org.example.hotel;

import java.util.Arrays;
import java.util.List;

public class HotelDAO {
    List<String> rooms;
    public HotelDAO() {
        rooms= Arrays.asList("101","102","103","104","105","106","107","108","109","110");
    }
    public boolean checkAvailability(String roomNumber) {
        return rooms.contains(roomNumber);
    }
}

