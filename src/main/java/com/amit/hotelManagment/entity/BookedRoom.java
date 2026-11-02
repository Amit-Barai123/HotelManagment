package com.amit.hotelManagment.entity;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "check_in")
	private LocalDate checkInDate;
	
	@Column(name = "check_out")
	private LocalDate checkOutDate;
	
	@Column(name = "guest_fullName")
	private String guestFullName;
	
	@Column(name = "guest_email")
	private String guestEmail;
	
	@Column(name = "adults")
	private int NumOfAdults;
	
	@Column(name = "childern")
	private int NumOfChildern;
	
	@Column(name = "total_guest")
	private int totalNumOfGuest;
	
	@Column(name = "conformation_Code")
	private String bookingConformationCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="room_id")
	private Room room;
	
	@PrePersist
	@PreUpdate
	public void calculateTotalNumberOfGuest() {
		this.totalNumOfGuest = this.NumOfAdults + this.NumOfChildern;
	}
	
	   // Constructor for creating object from booking request
    public BookedRoom(
            LocalDate checkInDate,
            LocalDate checkOutDate,
            String guestFullName,
            String guestEmail,
            int NumOfAdults,
            int NumOfChildern) {

        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestFullName = guestFullName;
        this.guestEmail = guestEmail;
        this.NumOfAdults = NumOfAdults;
        this.NumOfChildern = NumOfChildern;

        // Calculate automatically
        this.totalNumOfGuest = NumOfAdults + NumOfChildern;
    }

	
	
}
