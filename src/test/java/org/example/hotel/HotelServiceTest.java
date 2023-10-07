package org.example.hotel;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HotelServiceTest{
        private HotelDAO hotelDAO;
        private HotelService hotelService;
        @BeforeEach
        void setUp() {
            hotelDAO= Mockito.mock(HotelDAO.class);
            hotelService=new HotelService(hotelDAO);
        }

        @Test
        void bookRoom1() {
            Mockito.when(hotelDAO.checkAvailability("101")).thenReturn(Boolean.FALSE);
            Assertions.assertFalse(hotelService.bookRoom("101"));
        }

        @Test
        void bookRoom2() {
            Mockito.when(hotelDAO.checkAvailability("101")).thenReturn(Boolean.TRUE);
            Assertions.assertTrue(hotelService.bookRoom("101"));
        }

    @Test
    void bookRoom_null() {
        Mockito.when(hotelDAO.checkAvailability(null)).thenReturn(Boolean.TRUE);
        Assertions.assertFalse(hotelService.bookRoom("101"));
    }
}