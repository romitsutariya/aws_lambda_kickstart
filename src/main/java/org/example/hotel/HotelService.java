package org.example.hotel;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HotelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HotelService.class.getName());
    private final HotelDAO hotelDAO;
    public HotelService(HotelDAO hotelDAO) {
        this.hotelDAO = hotelDAO;
    }
    public boolean bookRoom(String roomId) {
         if(hotelDAO.checkAvailability(roomId)){
             LOGGER.info("Room booked for room id: "+roomId);
             return Boolean.TRUE;
         }
         else{
             LOGGER.info("Room not available for room id: "+roomId);
         }
         return Boolean.FALSE;
    }
    public boolean cancelRoom(String roomId) {
        if(hotelDAO.removeBookRoom(roomId)){
            LOGGER.info("Room cancelled for room id: "+roomId);
            return Boolean.TRUE;
        }
        else{
            LOGGER.info("Room not cancelled for room id: "+roomId);
        }
        return Boolean.FALSE;
    }
    public boolean addRoom(String roomId) {
        if(hotelDAO.addBookRoom(roomId)){
            LOGGER.info("Room added for room id: "+roomId);
            return Boolean.TRUE;
        }
        else{
            LOGGER.info("Room not added for room id: "+roomId);
        }
        return Boolean.FALSE;
    }

    public List<String> getRoom() {
        return hotelDAO.getRooms();
    }
}
