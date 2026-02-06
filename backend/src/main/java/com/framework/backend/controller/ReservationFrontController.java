package com.framework.backend.controller;

import com.framework.backend.dto.ReservationFrontDto;
import com.framework.backend.service.ReservationFrontService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/front/reservations")
public class ReservationFrontController {

    private final ReservationFrontService reservationFrontService;

    public ReservationFrontController(ReservationFrontService reservationFrontService) {
        this.reservationFrontService = reservationFrontService;
    }

    @GetMapping
    public String showReservations(Model model) {
        List<ReservationFrontDto> reservations = reservationFrontService.listReservation();
        model.addAttribute("reservations", reservations);
        return "listeReservation";
    }

    @GetMapping("/filter")
    public String filterReservationsByDate(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        List<ReservationFrontDto> reservations = reservationFrontService.listReservationByDate(startDate, endDate);
        model.addAttribute("reservations", reservations);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "listeReservation";
    }
}
