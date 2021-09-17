package org.tim.weathertracker.core.usecase.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.server.ResponseStatusException;
import org.tim.weathertracker.core.entities.UserData;
import org.tim.weathertracker.core.repository.UserDataRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class UserSaverTest {

    @Mock
    private UserDataRepository userDataRepository;

    @InjectMocks
    private UserSaver testee;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    public void save_blankName_exception() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> testee.save(new UserData()));
        assertThat(exception.getMessage()).isEqualTo("400 BAD_REQUEST \"UserData.name is not populated\"");
    }

    @Test
    public void save_blankEmail_exception() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> testee.save(UserData.builder().name("tim1").build()));
        assertThat(exception.getMessage()).isEqualTo("400 BAD_REQUEST \"UserData.email is not populated\"");
    }

    @Test
    public void save_repositoryCalled() {
        UUID userid = testee.save(
            UserData.builder().name("tim1").email("aaaa").build()
        );
        verify(userDataRepository).save(any());
        assertThat(userid).isNotNull();
    }
}