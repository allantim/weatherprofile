package org.tim.weathertracker.core.usecase.weatherprofile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.server.ResponseStatusException;
import org.tim.weathertracker.core.entities.UserData;
import org.tim.weathertracker.core.entities.UserProfile;
import org.tim.weathertracker.core.entities.dto.GeneralResponseDto;
import org.tim.weathertracker.core.repository.UserDataRepository;
import org.tim.weathertracker.core.repository.UserProfileRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class WeatherProfileDeleterTest {

    @Mock
    private UserDataRepository userDataRepository;
    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private WeatherProfileDeleter testee;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    public void delete_noUserFoundWithId() {
        UUID userId = UUID.fromString("959ea936-23ea-4807-a4ff-6e9f65143651");
        String nickname = "xxxx";
        when(userDataRepository.findByUserId(eq(userId))).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> testee.delete(userId, nickname));
        assertThat(exception.getMessage()).isEqualTo("404 NOT_FOUND \"User with userID 959ea936-23ea-4807-a4ff-6e9f65143651 not found. Cannot delete\"");

        verifyNoInteractions(userProfileRepository);
    }


    @Test
    public void delete_noMatchingProfile() {
        UUID userId = UUID.fromString("959ea936-23ea-4807-a4ff-6e9f65143651");
        String nickname = "xxxx";
        when(userDataRepository.findByUserId(eq(userId)))
            .thenReturn(
                UserData.builder()
                    .userId(userId)
                    .name("dave")
                    .email("aaaa")
                    .userProfiles(
                        Set.of(
                            UserProfile.builder()
                                .id(1L)
                                .nickname("yyyy") // <-- not the same
                                .cityWeathers(Set.of())
                                .build()
                        )
                    )
                    .build()
            );

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> testee.delete(userId, nickname));
        assertThat(exception.getMessage()).isEqualTo("404 NOT_FOUND \"UserProfile with nickname xxxx not found. Cannot delete\"");

        verifyNoInteractions(userProfileRepository);
    }

    @Test
    public void delete_matchingProfileFound() {
        UUID userId = UUID.fromString("959ea936-23ea-4807-a4ff-6e9f65143651");
        String nickname = "xxxx";
        when(userDataRepository.findByUserId(eq(userId)))
            .thenReturn(
                UserData.builder()
                    .userId(userId)
                    .name("dave")
                    .email("aaaa")
                    .userProfiles(
                        new HashSet<>(
                            Set.of(
                                UserProfile.builder()
                                    .id(1L)
                                    .nickname(nickname) // <-- now matching
                                    .cityWeathers(Set.of())
                                    .build()
                            )
                        )
                    )
                    .build()
            );

        GeneralResponseDto responseDto = testee.delete(userId, nickname);
        assertThat(responseDto.getStatus()).isEqualTo("deleted");

        // Verify that the Profile was deleted
        ArgumentCaptor<UserProfile> userProfileCaptor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).delete(userProfileCaptor.capture());
        assertThat(userProfileCaptor.getValue().getCityWeathers()).isEmpty();
        assertThat(userProfileCaptor.getValue().getId()).isEqualTo(1);
        assertThat(userProfileCaptor.getValue().getNickname()).isEqualTo(nickname);
    }
}