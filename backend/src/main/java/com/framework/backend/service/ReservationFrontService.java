package com.framework.backend.service;

import com.framework.backend.dto.ReservationFrontDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationFrontService {

    private final ReservationApiClient reservationApiClient;

    public ReservationFrontService(ReservationApiClient reservationApiClient) {
        this.reservationApiClient = reservationApiClient;
    }

    public List<ReservationFrontDto> listReservation() {
        return reservationApiClient.getReservationsFromBackOffice();
    }

    public List<ReservationFrontDto> listReservationByDate(LocalDate startDate, LocalDate endDate) {
        return reservationApiClient.getReservationsByDate(startDate, endDate);
    }
}
