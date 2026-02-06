package com.framework.backend.service;

import com.framework.backend.dto.ReservationFrontDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class ReservationApiClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ReservationApiClient(RestTemplate restTemplate,
                                @Value("${backoffice.api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<ReservationFrontDto> getReservationsFromBackOffice() {
        String url = baseUrl + "/reservations";
        ResponseEntity<List<ReservationFrontDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReservationFrontDto>>() {}
        );
        return response.getBody() != null ? response.getBody() : Collections.emptyList();
    }

    public List<ReservationFrontDto> getReservationsByDate(LocalDate startDate, LocalDate endDate) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/reservations");
        if (startDate != null) {
            builder.queryParam("startDate", startDate);
        }
        if (endDate != null) {
            builder.queryParam("endDate", endDate);
        }

        ResponseEntity<List<ReservationFrontDto>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReservationFrontDto>>() {}
        );
        return response.getBody() != null ? response.getBody() : Collections.emptyList();
    }
}
