package org.example.hotel;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
