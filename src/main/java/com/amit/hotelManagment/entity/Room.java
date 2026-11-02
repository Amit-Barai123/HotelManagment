package com.amit.hotelManagment.entity;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String roomType;
	
	private BigDecimal roomPrice;
	
	private boolean isBooked;
	
	@Lob
	private Blob photo;
	
	@OneToMany(mappedBy = "room" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<BookedRoom> bookings;
	
	public Room() {
		this.bookings= new ArrayList<>();
	}
	
	public void addBooking(BookedRoom booking) {
		if(bookings == null) {
			bookings= new ArrayList<BookedRoom>();
		}
		bookings.add(booking);
		booking.setRoom(this);
		isBooked= true;
		String bookingCode = generateBookingCode();
		booking.setBookingConformationCode(bookingCode);
	}
	
	
	
	private String generateBookingCode() {
	    SecureRandom random = new SecureRandom();
	    StringBuilder code = new StringBuilder(10);

	    for (int i = 0; i < 10; i++) {
	        code.append(random.nextInt(10));
	    }

	    return code.toString();
	}
}
