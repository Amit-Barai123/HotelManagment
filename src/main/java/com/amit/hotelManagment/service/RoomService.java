package com.amit.hotelManagment.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amit.hotelManagment.entity.Room;
import com.amit.hotelManagment.exception.InternalServerException;
import com.amit.hotelManagment.exception.ResourceNotFoundException;
import com.amit.hotelManagment.repository.RoomRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoomService implements IRoomService {
	
	private final RoomRepository roomRepository;
	

	@Override
	public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {
		Room room = new Room();
		room.setRoomType(roomType);
		room.setRoomPrice(roomPrice);
		if(!file.isEmpty()) {
			byte[] photoBytes = file.getBytes();
			Blob  photoBlob = new SerialBlob(photoBytes);
			room.setPhoto(photoBlob);
		}
		
		return roomRepository.save(room);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Room> getAllRoom() {
		return roomRepository.findAll();
	}

	
	@Override
	public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
		Optional<Room> theRoom = roomRepository.findById(roomId);
		if(theRoom.isEmpty()) {
			throw new ResourceNotFoundException("sorry, Room Not Found");
		}
		
		Blob photoBlob = theRoom.get().getPhoto();
		if(photoBlob !=null) {
			return photoBlob.getBytes(1,(int) photoBlob.length());
		}
		
		return null;
	}

	@Override
	public void deleteRoom(Long roomId) {
		
		Optional<Room> theRoom = roomRepository.findById(roomId);
		
		if(theRoom.isPresent()) {
			roomRepository.deleteById(roomId);
		}
		
		
	}

	@Override
	public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
	
		Room room = roomRepository.findById(roomId).get();
		
		if(roomType !=null) {
			room.setRoomType(roomType);
		}
		

		if(roomPrice !=null) {
			room.setRoomPrice(roomPrice);
		}
		
		if(photoBytes !=null && photoBytes.length > 0) {
			try {
				room.setPhoto(new SerialBlob(photoBytes));
			} catch (Exception e) {
				throw new InternalServerException("fail to update room");
			}
		}
		
		return roomRepository.save(room);
		
		
		}

	@Override
	public Optional<Room> getRoomById(Long roomId) {
		
		return Optional.of(roomRepository.findById(roomId).get());
	}

	@Override
	public List<Room> getAvaliableRooms(LocalDate checkInDate, LocalDate checkoutDate, String roomType) {
		return roomRepository.findAvalibleRoomsByDatesAndTypes(roomType, checkInDate, checkoutDate );
	}

	@Override
	public List<String> getAllRoomTypes() {
		
		return roomRepository.findRoomTypes();
	}


}
