package com.amit.hotelManagment.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amit.hotelManagment.entity.BookedRoom;
import com.amit.hotelManagment.entity.Room;
import com.amit.hotelManagment.response.BookingResponse;
import com.amit.hotelManagment.response.RoomResponse;
import com.amit.hotelManagment.service.IBookingService;
import com.amit.hotelManagment.service.IRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

	private final IBookingService bookingService;
	private final IRoomService roomService;
	
	@GetMapping("/all-bookings")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<BookingResponse>> getAllBookings(){
		List<BookedRoom> bookings = bookingService.getAllBookings();
		
		List<BookingResponse> bookingResponses = new ArrayList<BookingResponse>();
		
		for(BookedRoom booking :bookings) {
			BookingResponse bookingResponse= getBookingResponse(booking);
			bookingResponses.add(bookingResponse);
		}
		return ResponseEntity.ok(bookingResponses);
		
	}
	
	@PostMapping("/room/{roomId}/bookings")
	public ResponseEntity<?> saveBooking(
			@PathVariable Long roomId,
			@RequestBody BookedRoom bookingRequest
			){
		try {
			String conformationCode = bookingService.saveBooking(roomId, bookingRequest);
			return ResponseEntity.ok(
					"Room booked sucessfully, your conformation code is " + conformationCode
					);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	@GetMapping("/conformation/{conformationCode}")
	public ResponseEntity<?> getBookingByConformationCode(
			@PathVariable String conformationCode
			){
		try {
			BookedRoom booking = bookingService.findByBookingConformationCode(conformationCode);
			BookingResponse bookingResponse = getBookingResponse(booking);
			return ResponseEntity.ok(bookingResponse);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	
	
	@GetMapping("/user/{email}/bookings")
	public ResponseEntity<?> getBookingByUserEmail(
			@PathVariable String email
			){
		try {
			List<BookedRoom> bookings = bookingService.getBookingByUserEmail(email);
			List<BookingResponse> bookingsResponses = new ArrayList<>();
			
			for (BookedRoom booking: bookings) {
				BookingResponse bookingResponse = getBookingResponse(booking);
				bookingsResponses.add(bookingResponse);
			}
			return ResponseEntity.ok(bookingsResponses);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@DeleteMapping("/booking/{bookingId}/delete")
	public void cancelBooking(@PathVariable Long bookingId) {
		bookingService.cancelBooking(bookingId);
	}
	

	
	
	
    private BookingResponse getBookingResponse(BookedRoom booking) {

        Room theRoom = roomService
                .getRoomById(booking.getRoom().getId())
                .get();

        RoomResponse room = new RoomResponse(
                theRoom.getId(),
                theRoom.getRoomType(),
                theRoom.getRoomPrice()
        );

        return new BookingResponse(
                booking.getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getGuestFullName(),
                booking.getGuestEmail(),
                booking.getNumOfAdults(),
                booking.getNumOfChildern(),
                booking.getTotalNumOfGuest(),
                booking.getBookingConformationCode(),
                room
        );
    }
	
}
