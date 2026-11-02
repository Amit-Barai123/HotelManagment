package com.amit.hotelManagment.service;

import java.util.List;

import com.amit.hotelManagment.entity.BookedRoom;

public interface IBookingService {

	void cancelBooking(Long bookingId);
	
	List<BookedRoom> getAllBookingByRoomId(Long roomId);
	
	String saveBooking(Long roomId, BookedRoom bookingRequest);
	
	List<BookedRoom> getAllBookings();
	
	List<BookedRoom> getBookingByUserEmail(String email);
	
	BookedRoom findByBookingConformationCode(String conformationCode);
	
}
