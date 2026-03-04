package com.teamLead.reservationVoiture.controller;

import com.teamLead.reservationVoiture.dto.ReservationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/front")
public class ReservationFrontController {

    private final String apiUrl;
    private final String apiToken;
    private final RestTemplate restTemplate = new RestTemplate();

    public ReservationFrontController(
            @Value("${external.api.reservations.url:http://localhost:8081/reservation/api/reservation/list}") String apiUrl,
            @Value("${external.api.reservations.token:}") String apiToken
    ) {
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
    }

    @GetMapping("/reservations")
    public String listReservations(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            Model model
    ) {
        // Gestion du token manquant
        if (apiToken == null || apiToken.isBlank()) {
            model.addAttribute("errorMessage", "Accès refusé : token manquant.");
            model.addAttribute("reservations", Collections.emptyList());
            return "reservations";
        }

        FetchResult result = fetchReservationsWithStatus();
        if (result.status == FetchStatus.TOKEN_EXPIRED) {
            model.addAttribute("errorMessage", "Token expiré : veuillez renouveler votre accès.");
            model.addAttribute("reservations", Collections.emptyList());
            return "reservations";
        } else if (result.status == FetchStatus.ACCESS_DENIED) {
            model.addAttribute("errorMessage", "Accès refusé : token invalide.");
            model.addAttribute("reservations", Collections.emptyList());
            return "reservations";
        }

        List<ReservationDto> reservations = result.reservations;
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;
        if (date != null && !date.isEmpty()) {
            LocalDate d = LocalDate.parse(date, fmt);
            reservations = reservations.stream()
                    .filter(r -> d.equals(r.getArrivalDate()))
                    .collect(Collectors.toList());
        } else if ((from != null && !from.isEmpty()) || (to != null && !to.isEmpty())) {
            LocalDate f = (from != null && !from.isEmpty()) ? LocalDate.parse(from, fmt) : LocalDate.MIN;
            LocalDate t = (to != null && !to.isEmpty()) ? LocalDate.parse(to, fmt) : LocalDate.MAX;
            reservations = reservations.stream()
                    .filter(r -> r.getArrivalDate() != null && !r.getArrivalDate().isBefore(f) && !r.getArrivalDate().isAfter(t))
                    .collect(Collectors.toList());
        }

        model.addAttribute("reservations", reservations);
        return "reservations";
    }

    // Enum pour le statut de récupération
    private enum FetchStatus {
        OK, TOKEN_EXPIRED, ACCESS_DENIED, ERROR
    }

    // Structure de retour
    private static class FetchResult {
        List<ReservationDto> reservations;
        FetchStatus status;
        FetchResult(List<ReservationDto> reservations, FetchStatus status) {
            this.reservations = reservations;
            this.status = status;
        }
    }

    private FetchResult fetchReservationsWithStatus() {
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("Authorization", "Bearer " + apiToken);
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

            // Gestion des codes d'erreur HTTP
            if (response.getStatusCode().value() == 401) {
                return new FetchResult(Collections.emptyList(), FetchStatus.TOKEN_EXPIRED);
            } else if (response.getStatusCode().value() == 403) {
                return new FetchResult(Collections.emptyList(), FetchStatus.ACCESS_DENIED);
            } else if (!response.getStatusCode().is2xxSuccessful()) {
                return new FetchResult(Collections.emptyList(), FetchStatus.ERROR);
            }

            String json = response.getBody();
            if (json == null || json.isBlank()) {
                return new FetchResult(Collections.emptyList(), FetchStatus.OK);
            }

            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(json);

            com.fasterxml.jackson.databind.JsonNode arrayNode = root;
            if (root.has("data") && root.get("data").isArray()) {
                arrayNode = root.get("data");
            } else if (root.has("reservations") && root.get("reservations").isArray()) {
                arrayNode = root.get("reservations");
            }

            if (!arrayNode.isArray()) {
                return new FetchResult(Collections.emptyList(), FetchStatus.OK);
            }

            List<ReservationDto> list = new java.util.ArrayList<>();
            for (com.fasterxml.jackson.databind.JsonNode node : arrayNode) {
                ReservationDto dto = new ReservationDto();
                // id can be named idReservation or id
                if (node.has("idReservation")) dto.setId(node.path("idReservation").asLong());
                else if (node.has("id")) dto.setId(node.path("id").asLong());

                // customer name may be idClient or customerName
                if (node.has("idClient")) dto.setCustomerName(node.path("idClient").asText());
                else dto.setCustomerName(node.path("customerName").asText(null));

                // hotel name may be nested
                if (node.has("hotel") && node.get("hotel").has("nom")) {
                    dto.setHotelName(node.get("hotel").path("nom").asText());
                } else {
                    dto.setHotelName(node.path("hotelName").asText(null));
                }

                // arrival date: dateHeureArrive like 2026-02-06T18:35 -> take date part
                if (node.has("dateHeureArrive")) {
                    String s = node.path("dateHeureArrive").asText(null);
                    if (s != null && s.length() >= 10) {
                        dto.setArrivalDate(LocalDate.parse(s.substring(0, 10)));
                    }
                } else if (node.has("arrivalDate")) {
                    String s = node.path("arrivalDate").asText(null);
                    if (s != null && s.length() >= 10) dto.setArrivalDate(LocalDate.parse(s.substring(0, 10)));
                }

                list.add(dto);
            }

            return new FetchResult(list, FetchStatus.OK);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                return new FetchResult(Collections.emptyList(), FetchStatus.TOKEN_EXPIRED);
            } else if (e.getStatusCode().value() == 403) {
                return new FetchResult(Collections.emptyList(), FetchStatus.ACCESS_DENIED);
            } else {
                return new FetchResult(Collections.emptyList(), FetchStatus.ERROR);
            }
        } catch (Exception e) {
            return new FetchResult(Collections.emptyList(), FetchStatus.ERROR);
        }
    }
}
