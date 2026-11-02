package com.amit.hotelManagment.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.amit.hotelManagment.entity.Room;

public interface IRoomService {

	Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException,IOException;
	
	List<Room> getAllRoom();
	
	List<String> getAllRoomTypes();
	
	byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException;
	
	void deleteRoom(Long roomId);
	
	Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes);
	
	Optional<Room> getRoomById(Long roomId);
	
	List<Room> getAvaliableRooms(LocalDate checkInDate, LocalDate checkoutDate, String roomType);
	
	
}
