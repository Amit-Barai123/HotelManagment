package com.amit.hotelManagment.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amit.hotelManagment.entity.BookedRoom;
import com.amit.hotelManagment.entity.Room;
import com.amit.hotelManagment.response.BookingResponse;
import com.amit.hotelManagment.response.RoomResponse;
import com.amit.hotelManagment.service.BookingService;
import com.amit.hotelManagment.service.IRoomService;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.cfg.DateTimeFeature;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

	private final IRoomService roomService;
	private final BookingService bookBookingService;
	

	
	@PostMapping("/add/new-room")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<RoomResponse> addNewRoom(
			@RequestParam("photo") MultipartFile photo,
			@RequestParam("roomType") String roomType,
			@RequestParam("roomPrice") BigDecimal roomPrice
			) throws SQLException , IOException {
		
		Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
		
		RoomResponse response = new RoomResponse(savedRoom.getId(),savedRoom.getRoomType(),
				savedRoom.getRoomPrice());
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/room/types")
	public List<String> getRoomTypes(){
		return roomService.getAllRoomTypes();
	}
	@Transactional(readOnly = true)
	@GetMapping("/all-rooms")
	public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {

	    List<Room> rooms = roomService.getAllRoom();
	    List<RoomResponse> roomResponses = new ArrayList<>();

	    for (Room room : rooms) {

	        byte[] photobytes = roomService.getRoomPhotoByRoomId(room.getId());

	        RoomResponse roomResponse = getResponse(room);

	        if (photobytes != null && photobytes.length > 0) {
	            String base64Photo = Base64.getEncoder().encodeToString(photobytes);
	            roomResponse.setPhoto(base64Photo);
	        }

	        roomResponses.add(roomResponse);
	    }

	    return ResponseEntity.ok(roomResponses);
	}
	
	
	
@DeleteMapping("/delete/room/{roomId}")
@PreAuthorize("hasRole('ROLE_ADMIN')")

public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId){
	roomService.deleteRoom(roomId);
	return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
}


@Transactional()
@PutMapping("/update/{roomId}")
@PreAuthorize("hasRole('ROLE_ADMIN')")

public ResponseEntity<RoomResponse> updateRoom(
		@PathVariable Long roomId,
		@RequestParam(required = false) String roomType,
		@RequestParam(required = false) BigDecimal roomPrice,
		@RequestParam(required = false) MultipartFile photo
		) throws SQLException , IOException{
	
	byte[] photoByte = photo !=null && !photo.isEmpty() ?
			photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);
	
	Blob photoBlob = photoByte != null && photoByte.length > 0 ? new SerialBlob(photoByte) : null;
	Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoByte);
	theRoom.setPhoto(photoBlob);
	RoomResponse roomResponse = getResponse(theRoom);
	return ResponseEntity.ok(roomResponse);
}


@GetMapping("/room/{roomId}")
public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId){
	Optional<Room> theRoom = roomService.getRoomById(roomId);
	
	return theRoom.map(room ->{
		RoomResponse roomResponse = getResponse(room);
		return ResponseEntity.ok(Optional.of(roomResponse));
	}).orElseThrow(()-> new RuntimeException("Room not found"));
}

@Transactional

@GetMapping("/available-rooms")
public ResponseEntity<List<RoomResponse>> getAllAvaliableRooms(
		@RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
		@RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
		@RequestParam("roomType") String roomType
 		) throws SQLException{
	List<Room> avalibleRooms = roomService.getAvaliableRooms(checkInDate, checkOutDate, roomType);
	
	List<RoomResponse> roomResponses = new ArrayList<>();
	   for (Room room : avalibleRooms) {

	        byte[] photobytes = roomService.getRoomPhotoByRoomId(room.getId());

	        RoomResponse roomResponse = getResponse(room);

	        if (photobytes != null && photobytes.length > 0) {
	            String base64Photo = Base64.getEncoder().encodeToString(photobytes);
	            roomResponse.setPhoto(base64Photo);
	        }

	        roomResponses.add(roomResponse);
	    }
	   if(roomResponses.isEmpty()) {
		   return ResponseEntity.noContent().build();
	   }else {
		   return ResponseEntity.ok(roomResponses);
	   }
}


	
	public RoomResponse getResponse(Room room) {
		List<BookedRoom> bookings = getAllBookingByRoomId(room.getId());
		
		List<BookingResponse> bookingInfo = bookings.
				stream().map(
						booking -> new BookingResponse(booking.getId(),
								booking.getCheckInDate(),
								booking.getCheckOutDate(),
								booking.getBookingConformationCode()
						
						)).toList();
		byte[] photobytes = null;
				Blob photoBlob = room.getPhoto();
				if(photoBlob != null ) {
					try {
						photobytes = photoBlob.getBytes(1,(int) photoBlob.length());
					} catch (Exception e) {
						new RuntimeException("Error while retriving the image");
					}
				}
				
				return new RoomResponse(room.getId(),
						room.getRoomType(),room.getRoomPrice(),
						room.isBooked(),
						photobytes,
						bookingInfo
						);
	}
	
	private List<BookedRoom> getAllBookingByRoomId(Long roomId){
		return bookBookingService.getAllBookingByRoomId(roomId);
	}
	
	
}
