package com.n11.eventflow.model.dto.Agenda.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetConferenceAgendaResponseDTO {
    List<TrackResponseDTO> conferenceAgenda;
}
