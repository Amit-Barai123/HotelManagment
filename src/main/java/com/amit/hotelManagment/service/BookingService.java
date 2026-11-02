package com.amit.hotelManagment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amit.hotelManagment.entity.BookedRoom;
import com.amit.hotelManagment.entity.Room;
import com.amit.hotelManagment.exception.InvalidBookingDateException;
import com.amit.hotelManagment.repository.BookingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookingService implements IBookingService {

	private final BookingRepository bookingRepository;
	private final IRoomService roomService;
	@Override
	public void cancelBooking(Long bookingId) {

		 bookingRepository.deleteById(bookingId);
	}
	@Override
	public List<BookedRoom> getAllBookingByRoomId(Long roomId) {
		return bookingRepository.findByRoomId(roomId);
	}
	@Override
	public String saveBooking(Long roomId, BookedRoom bookingRequest) {
		if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
			throw new InvalidBookingDateException("check-in date should be before check-out date");
		}
		Room room = roomService.getRoomById(roomId).get();
		List<BookedRoom> existingBooking= room.getBookings();
		boolean roomIsAvalible = roomIsAvailable(bookingRequest,existingBooking);
		
		if(roomIsAvalible) {
			room.addBooking(bookingRequest);
			bookingRepository.save(bookingRequest);
		}else {
			throw new RuntimeException("sorry this romm is not avalible for the booking of that date");
		}
		
		return bookingRequest.getBookingConformationCode();
		
	}
	
	
	@Override
	public List<BookedRoom> getAllBookings() {
		return bookingRepository.findAll();
	}
	@Override
	public List<BookedRoom> getBookingByUserEmail(String email) {
		return bookingRepository.findByGuestEmail(email);
	}
	@Override
	public BookedRoom findByBookingConformationCode(String conformationCode) {
		return  bookingRepository.findByBookingConformationCode(conformationCode).
				orElseThrow(()-> new RuntimeException("no booking found with the booking code :" + conformationCode));
				
	}
	
    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
	
}
