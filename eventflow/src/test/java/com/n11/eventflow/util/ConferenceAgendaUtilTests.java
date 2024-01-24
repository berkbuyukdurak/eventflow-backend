package com.n11.eventflow.util;

import com.n11.eventflow.constants.CommonConstants;
import com.n11.eventflow.model.dto.Agenda.ConferenceAgendaUtilDTO;
import com.n11.eventflow.model.dto.Agenda.Response.TrackResponseDTO;
import com.n11.eventflow.model.dto.Presentation.PresentationInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ConferenceAgendaUtilTests {

    @InjectMocks
    private ConferenceAgendaUtil conferenceAgendaUtil;

    private List<PresentationInfoDTO> presentations;

    @BeforeEach
    public void setUp() {
        // Create a list of presentations for testing
        presentations = Arrays.asList(
                new PresentationInfoDTO("Talk 1", 60),
                new PresentationInfoDTO("Talk 2", 45),
                new PresentationInfoDTO("Talk 3", 30),
                new PresentationInfoDTO("Talk 4", 45),
                new PresentationInfoDTO("Talk 5", 5),
                new PresentationInfoDTO("Talk 6", 60),
                new PresentationInfoDTO("Talk 7", 45),
                new PresentationInfoDTO("Talk 8", 30),
                new PresentationInfoDTO("Talk 9", 30),
                new PresentationInfoDTO("Talk 9", 45)
        );
    }

    @Test
    public void testAllocateSessions_WithValidPresentations() {
        // Test allocateSessions with a valid list of presentations
        List<TrackResponseDTO> tracks = conferenceAgendaUtil.allocateSessions(presentations);
        assertFalse(tracks.isEmpty(), "Tracks should not be empty");

        // Find the index of "Lunch" in the first track
        TrackResponseDTO firstTrack = tracks.get(0);
        int lunchIndex = -1;
        for (int i = 0; i < firstTrack.getTrack().size(); i++) {
            if ("Lunch".equals(firstTrack.getTrack().get(i).getName())) {
                lunchIndex = i;
                break;
            }
        }

        // Assert that "Lunch" is scheduled
        assertTrue(lunchIndex != -1, "Lunch should be scheduled");
        assertEquals("12:00PM", firstTrack.getTrack().get(lunchIndex).getStartTime(), "Lunch should be scheduled at noon");


        // Assert that the networking event is scheduled correctly
        int networkingEventIndex = firstTrack.getTrack().size() - 1; // Networking event should be the last item
        assertEquals("Networking Event", firstTrack.getTrack().get(networkingEventIndex).getName(), "Networking event should be the last scheduled event");

        // Check the time of the networking event
        try {
            LocalTime lastTalkEndTime = LocalTime.parse(firstTrack.getTrack().get(networkingEventIndex - 1).getStartTime(), CommonConstants.TIME_FORMATTER)
                    .plusMinutes(firstTrack.getTrack().get(networkingEventIndex - 1).getDuration());
            boolean isTimeCorrect = (lastTalkEndTime.isAfter(LocalTime.of(15, 59)) && lastTalkEndTime.isBefore(LocalTime.of(17, 1)));
            assertTrue(isTimeCorrect, "Networking event should be scheduled between 4 PM and 5 PM");
        } catch (DateTimeParseException e) {
            fail("Failed to parse time string: " + e.getMessage());
        }
    }

    @Test
    public void testAllocateSessions_WithEmptyList() {
        List<TrackResponseDTO> tracks = conferenceAgendaUtil.allocateSessions(Arrays.asList());
        assertTrue(tracks.isEmpty(), "Tracks should be empty for no presentations");
    }

    @Test
    public void testWithPresentationsFillingExactlyOneSession() {
        List<PresentationInfoDTO> oneSessionPresentations = Arrays.asList(
                new PresentationInfoDTO("Talk 1", 180)
        );
        List<TrackResponseDTO> tracks = conferenceAgendaUtil.allocateSessions(oneSessionPresentations);
        assertEquals(1, tracks.size(), "Should be exactly one track");
    }

    @Test
    public void testWithExcessiveNumberOfPresentations() {
        List<PresentationInfoDTO> manyPresentations = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            manyPresentations.add(new PresentationInfoDTO("Talk " + i, 30));
        }
        List<TrackResponseDTO> tracks = conferenceAgendaUtil.allocateSessions(manyPresentations);
    }

    @Test
    public void testWithPresentationsThatExactlyFitAMorningSession() {
        List<PresentationInfoDTO> exactFitPresentations = Arrays.asList(
                new PresentationInfoDTO("Talk 1", 90),
                new PresentationInfoDTO("Talk 2", 90)
        );
        List<TrackResponseDTO> tracks = conferenceAgendaUtil.allocateSessions(exactFitPresentations);
        assertEquals(1, tracks.size(), "Should be exactly one track");
        assertEquals(2, tracks.get(0).getTrack().size() - 2, "Morning session should have exactly two talks excluding lunch and networking event");
    }

    @Test
    public void testWithMaximumDurationPresentations() {
        List<PresentationInfoDTO> maxDurationPresentations = Arrays.asList(
                new PresentationInfoDTO("Morning Talk", 180), // Maximum duration for morning
                new PresentationInfoDTO("Afternoon Talk", 240) // Maximum duration for afternoon
        );
        List<TrackResponseDTO> tracks = conferenceAgendaUtil.allocateSessions(maxDurationPresentations);

        assertEquals(1, tracks.size(), "Should be exactly one track");

        TrackResponseDTO track = tracks.get(0);
        List<ConferenceAgendaUtilDTO> scheduledTalks = track.getTrack();

        // Check if morning talk is scheduled at the beginning
        assertEquals("Morning Talk", scheduledTalks.get(0).getName(), "First talk should be the morning talk");
        assertEquals("09:00AM", scheduledTalks.get(0).getStartTime(), "Morning talk should start at 9 AM");

        // Check if lunch is scheduled at noon
        assertEquals("Lunch", scheduledTalks.get(1).getName(), "Lunch should be scheduled at noon");
        assertEquals("12:00PM", scheduledTalks.get(1).getStartTime(), "Lunch should be scheduled at noon");

        // Check if afternoon talk is scheduled right after lunch
        assertEquals("Afternoon Talk", scheduledTalks.get(2).getName(), "Afternoon talk should be scheduled right after lunch");
        assertEquals("01:00PM", scheduledTalks.get(2).getStartTime(), "Afternoon talk should start at 1 PM");

        // Networking event should not be present due to afternoon talk's duration
        assertEquals("Afternoon Talk", scheduledTalks.get(scheduledTalks.size() - 1).getName(), "Networking event should not be scheduled");
    }
}
