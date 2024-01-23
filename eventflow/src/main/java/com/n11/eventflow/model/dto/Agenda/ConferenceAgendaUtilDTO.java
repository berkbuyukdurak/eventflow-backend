package com.n11.eventflow.model.dto.Agenda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConferenceAgendaUtilDTO {
    private String startTime;
    private String name;
    private int duration;

    public ConferenceAgendaUtilDTO(String startTime, String name) {
        this.startTime = startTime;
        this.name = name;
    }
}
