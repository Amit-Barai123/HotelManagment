package com.amit.hotelManagment.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amit.hotelManagment.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{
	
	@Query("SELECT DISTINCT r.roomType FROM Room r")
	List<String> findRoomTypes();
	
	
	@Query("""
		    SELECT r FROM Room r
		    WHERE r.roomType LIKE %:roomType%
		    AND r.id NOT IN (
		        SELECT br.room.id
		        FROM BookedRoom br
		        WHERE br.checkInDate <= :checkOutDate
		          AND br.checkOutDate >= :checkInDate
		    )
		    """)
		List<Room> findAvalibleRoomsByDatesAndTypes(
		         String roomType,
		        LocalDate checkInDate,
		       LocalDate checkOutDate
		);

//	@Query("""
//		    SELECT r FROM Room r
//		    WHERE r.roomType LIKE %:roomType%
//		    AND r.id NOT IN (
//		        SELECT br.room.id
//		        FROM BookedRoom br
//		        WHERE br.checkINDate <= :checkoutDate
//		          AND br.checkoutDate >= :checkInDate
//		    )
//		    """)
//	List<Room> findAvalibleRoomsByDatesAndTypes(LocalDate checkInDate,LocalDate checkOutDate, String roomType);
}
