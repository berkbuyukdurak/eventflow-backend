package com.n11.eventflow.util;

import com.n11.eventflow.constants.CommonConstants;
import com.n11.eventflow.model.dto.Agenda.ConferenceAgendaUtilDTO;
import com.n11.eventflow.model.dto.Agenda.Response.TrackResponseDTO;
import com.n11.eventflow.model.dto.Presentation.PresentationInfoDTO;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.*;


/**
 * Utility class for organizing a conference agenda.
 *
 * The ConferenceAgendaUtil class is responsible for allocating presentations into tracks
 * for a conference schedule. It manages the placement of presentations into morning and
 * afternoon sessions, ensuring that each track follows the conference's time constraints
 * and includes a lunch break and a networking event if possible.
 *
 * The class operates by taking a list of presentations and arranging them into tracks.
 * Presentations are scheduled based on their duration and the available time in each session.
 * A FIFO (First-In-First-Out) queue mechanism is used to process the presentations, ensuring
 * that they are considered in the order they are received. The queue is implemented using a
 * LinkedList for efficient addition and removal of elements.
 *
 * Each track consists of a morning session starting at 9:00 AM, followed by a lunch break at
 * 12:00 PM, and an afternoon session starting at 1:00 PM. The afternoon session may conclude
 * with a networking event, starting no earlier than 4:00 PM and no later than 5:00 PM, if time
 * permits after the scheduled presentations.
 *
 * This utility class ensures that the scheduling of presentations is optimized to utilize
 * the available time effectively while adhering to the constraints of the conference's schedule.
 */
@Component
public class ConferenceAgendaUtil {

    /**
     * Allocates sessions to tracks in the conference agenda.
     *
     * This method processes a list of presentations, organizing them into tracks with morning
     * and afternoon sessions. Each session has a fixed start time, and the method ensures
     * that a lunch break is scheduled. It also adds a networking event at the end of the day
     * if time permits. The presentations are processed using a FIFO queue for efficient scheduling.
     * The LinkedList implementation of Queue is used for its efficiency in insertion and removal operations.
     *
     * @param presentations A list of PresentationInfoDTO objects representing the presentations to be scheduled.
     * @return List of TrackResponseDTO objects representing the organized tracks of the conference agenda.
     * Time Complexity -> O(n)
     * Space Complexity -> O(n)
     */
    public List<TrackResponseDTO> allocateSessions(List<PresentationInfoDTO> presentations) {
        Queue<PresentationInfoDTO> presentationsQueue = new LinkedList<>(presentations);
        List<TrackResponseDTO> tracks = new ArrayList<>();

        while (!presentationsQueue.isEmpty()) {
            TrackResponseDTO track = new TrackResponseDTO();
            List<ConferenceAgendaUtilDTO> morningAgenda = createAgenda(presentationsQueue, CommonConstants.MORNING_SESSION_START_TIME, CommonConstants.MORNING_SESSION_DURATION);
            List<ConferenceAgendaUtilDTO> afternoonAgenda = createAgenda(presentationsQueue, CommonConstants.AFTERNOON_SESSION_START_TIME, CommonConstants.AFTERNOON_SESSION_DURATION);

            // Add lunch
            morningAgenda.add(new ConferenceAgendaUtilDTO(CommonConstants.LUNCH_TIME.format(CommonConstants.TIME_FORMATTER), "Lunch", 60));

            // Determine networking event start time
            LocalTime lastPresentationEndTime = afternoonAgenda.isEmpty()
                    ? CommonConstants.AFTERNOON_SESSION_START_TIME
                    : LocalTime.parse(afternoonAgenda.get(afternoonAgenda.size() - 1).getStartTime(), CommonConstants.TIME_FORMATTER)
                    .plusMinutes(afternoonAgenda.get(afternoonAgenda.size() - 1).getDuration());


            // Add networking event only if there is time available
            if (lastPresentationEndTime.isBefore(CommonConstants.NETWORKING_EVENT_END_TIME)) {
                LocalTime networkingEventStartTime = lastPresentationEndTime.isBefore(CommonConstants.NETWORKING_EVENT_START_TIME)
                        ? CommonConstants.NETWORKING_EVENT_START_TIME
                        : lastPresentationEndTime;
                afternoonAgenda.add(new ConferenceAgendaUtilDTO(networkingEventStartTime.format(CommonConstants.TIME_FORMATTER), "Networking Event"));
            }

            track.setTrack(combineAgendas(morningAgenda, afternoonAgenda));
            tracks.add(track);
        }
        return tracks;
    }

    /**
     * Creates an agenda for a conference session, either morning or afternoon.
     *
     * This method iterates through a queue of presentations, fitting each into the session
     * based on its duration and the session's remaining time. The method prioritizes filling
     * the session to its maximum capacity. If a presentation's duration is exactly equal to the
     * remaining session duration, it gets added, and the session ends. The method ensures
     * that presentations exceeding the total session duration are skipped to maintain the session
     * boundaries.
     *
     * @param presentations A queue of PresentationInfoDTO objects to be scheduled in the session.
     * @param startTime The start time of the session (morning or afternoon).
     * @param sessionDuration The total duration available for the session.
     * @return List of ConferenceAgendaUtilDTO objects representing the scheduled presentations for the session.
     */
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

    /**
     * Combines the agendas of morning and afternoon sessions into a single track.
     *
     * This method merges two lists of scheduled presentations (morning and afternoon) to form
     * a complete track for a day. It is a straightforward concatenation of two lists, reflecting
     * the chronological order of events in the conference from morning to afternoon, including lunch
     * and possibly a networking event.
     *
     * @param morningAgenda List of ConferenceAgendaUtilDTO for the morning session.
     * @param afternoonAgenda List of ConferenceAgendaUtilDTO for the afternoon session.
     * @return Combined list of ConferenceAgendaUtilDTO objects for the entire track.
     */
    private List<ConferenceAgendaUtilDTO> combineAgendas(List<ConferenceAgendaUtilDTO> morningAgenda, List<ConferenceAgendaUtilDTO> afternoonAgenda) {
        List<ConferenceAgendaUtilDTO> combinedAgenda = new ArrayList<>(morningAgenda);
        combinedAgenda.addAll(afternoonAgenda);
        return combinedAgenda;
    }
}
