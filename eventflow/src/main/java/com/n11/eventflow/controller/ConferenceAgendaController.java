package com.n11.eventflow.controller;

import com.n11.eventflow.model.dto.Agenda.Response.GetConferenceAgendaResponseDTO;
import com.n11.eventflow.model.dto.Agenda.Response.TrackResponseDTO;
import com.n11.eventflow.service.ConferenceAgendaService;
import com.n11.eventflow.util.GenericResponseHandler;
import com.n11.eventflow.constants.dictionaries.ResponseDictionary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/agenda")
public class ConferenceAgendaController {

    private final ConferenceAgendaService conferenceAgendaService;

    public ConferenceAgendaController(ConferenceAgendaService conferenceAgendaService) {
        this.conferenceAgendaService = conferenceAgendaService;
    }

    @GetMapping()
    public ResponseEntity<Object> getConferenceAgenda() {
        GetConferenceAgendaResponseDTO response = conferenceAgendaService.getConferenceAgenda();
        if (ObjectUtils.isEmpty(response)) {
            return GenericResponseHandler.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseDictionary.INTERNAL_SERVER_ERROR);
        }
        return GenericResponseHandler.successResponse(HttpStatus.CREATED, response);
    }
}
