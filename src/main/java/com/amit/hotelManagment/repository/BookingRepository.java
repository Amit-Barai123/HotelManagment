package com.amit.hotelManagment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amit.hotelManagment.entity.BookedRoom;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<BookedRoom, Long> {
	
	List<BookedRoom> findByRoomId(Long roomId);
	

List<BookedRoom> findByGuestEmail(String guestEmail);

Optional<BookedRoom>  findByBookingConformationCode(String bookingConformationCode);
}
