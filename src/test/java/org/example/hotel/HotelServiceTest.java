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

        @Test
        void bookRoom() {
            Mockito.when(hotelDAO.addBookRoom("101")).thenReturn(Boolean.TRUE);
            Assertions.assertTrue(hotelService.addRoom("101"));
        }

        @Test
        void cancelRoom() {
            Mockito.when(hotelDAO.removeBookRoom("101")).thenReturn(Boolean.TRUE);
            Assertions.assertTrue(hotelService.cancelRoom("101"));
        }

        @Test
        void addRoom() {
            Mockito.when(hotelDAO.addBookRoom("101")).thenReturn(Boolean.TRUE);
            Assertions.assertTrue(hotelService.addRoom("101"));
        }

        @Test
        void getRoom() {
            Mockito.when(hotelDAO.getRooms()).thenReturn(null);
            Assertions.assertNull(hotelService.getRoom());
        }
}