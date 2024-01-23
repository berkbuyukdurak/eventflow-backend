package com.n11.eventflow.service;

import com.n11.eventflow.model.dto.Agenda.Response.GetConferenceAgendaResponseDTO;
import org.springframework.stereotype.Service;
import com.n11.eventflow.model.dto.Presentation.PresentationListDTO;
import com.n11.eventflow.util.ConferenceAgendaUtil;

@Service
public class ConferenceAgendaService {

    private final PresentationService presentationService;
    private final ConferenceAgendaUtil conferenceAgendaUtil;

    public ConferenceAgendaService(PresentationService presentationService, ConferenceAgendaUtil conferenceAgendaUtil) {
        this.presentationService = presentationService;
        this.conferenceAgendaUtil = conferenceAgendaUtil;
    }

    public GetConferenceAgendaResponseDTO getConferenceAgenda() {
        PresentationListDTO responseDTO = presentationService.getAllPresentation();
        GetConferenceAgendaResponseDTO response = new GetConferenceAgendaResponseDTO();
        response.setConferenceAgenda(conferenceAgendaUtil.allocateSessions(responseDTO.getPresentations()));
        return response;
    }
}