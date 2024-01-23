package com.n11.eventflow.util;

import com.n11.eventflow.constants.CommonConstants;
import com.n11.eventflow.model.dto.Agenda.ConferenceAgendaUtilDTO;
import com.n11.eventflow.model.dto.Agenda.Response.TrackResponseDTO;
import com.n11.eventflow.model.dto.Presentation.PresentationInfoDTO;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class ConferenceAgendaUtil {

    public List<TrackResponseDTO> allocateSessions(List<PresentationInfoDTO> presentations) {
        Queue<PresentationInfoDTO> presentationsQueue = new LinkedList<>(presentations);
        List<TrackResponseDTO> tracks = new ArrayList<>();

        while (!presentationsQueue.isEmpty()) {
            TrackResponseDTO track = new TrackResponseDTO();
            List<ConferenceAgendaUtilDTO> morningAgenda = createAgenda(presentationsQueue, LocalTime.of(9, 0), CommonConstants.MORNING_SESSION_DURATION);
            List<ConferenceAgendaUtilDTO> afternoonAgenda = createAgenda(presentationsQueue, LocalTime.of(13, 0), CommonConstants.AFTERNOON_SESSION_DURATION);

            // Add lunch
            morningAgenda.add(new ConferenceAgendaUtilDTO("12:00PM", "Lunch", 60));

            // Determine networking event start time
            LocalTime lastPresentationEndTime = afternoonAgenda.isEmpty() ? LocalTime.of(13, 0) : LocalTime.parse(afternoonAgenda.get(afternoonAgenda.size() - 1).getStartTime(), CommonConstants.TIME_FORMATTER).plusMinutes(afternoonAgenda.get(afternoonAgenda.size() - 1).getDuration());
            LocalTime networkingEventStartTime = lastPresentationEndTime.isBefore(LocalTime.of(16, 0)) ? LocalTime.of(16, 0) : lastPresentationEndTime;
            if (networkingEventStartTime.isAfter(LocalTime.of(17, 0))) {
                networkingEventStartTime = LocalTime.of(17, 0);
            }
            afternoonAgenda.add(new ConferenceAgendaUtilDTO(networkingEventStartTime.format(CommonConstants.TIME_FORMATTER), "Networking Event"));

            track.setTrack(combineAgendas(morningAgenda, afternoonAgenda));
            tracks.add(track);
        }
        return tracks;
    }

    private List<ConferenceAgendaUtilDTO> createAgenda(Queue<PresentationInfoDTO> presentations, LocalTime startTime, int sessionDuration) {
        List<ConferenceAgendaUtilDTO> agenda = new ArrayList<>();
        int remainingDuration = sessionDuration;

        while (!presentations.isEmpty() && presentations.peek().getDuration() <= remainingDuration) {
            PresentationInfoDTO presentation = presentations.poll();
            agenda.add(new ConferenceAgendaUtilDTO(startTime.format(CommonConstants.TIME_FORMATTER), presentation.getName(), presentation.getDuration()));
            startTime = startTime.plusMinutes(presentation.getDuration());
            remainingDuration -= presentation.getDuration();
        }
        return agenda;
    }

    private List<ConferenceAgendaUtilDTO> combineAgendas(List<ConferenceAgendaUtilDTO> morningAgenda, List<ConferenceAgendaUtilDTO> afternoonAgenda) {
        List<ConferenceAgendaUtilDTO> combinedAgenda = new ArrayList<>(morningAgenda);
        combinedAgenda.addAll(afternoonAgenda);
        return combinedAgenda;
    }
}
