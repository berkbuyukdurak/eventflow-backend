package com.n11.eventflow.model.dto.Agenda.Response;

import com.n11.eventflow.model.dto.Agenda.ConferenceAgendaUtilDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackResponseDTO {
    private List<ConferenceAgendaUtilDTO> track;
}
