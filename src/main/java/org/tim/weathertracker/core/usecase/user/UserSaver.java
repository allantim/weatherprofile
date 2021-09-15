package org.tim.weathertracker.core.usecase.user;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.tim.weathertracker.core.entities.UserData;
import org.tim.weathertracker.core.repository.UserDataRepository;

import java.util.UUID;

@Component
public class UserSaver {

    private final UserDataRepository userDataRepository;

    public UserSaver(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public UUID save(UserData userData) {
        if (StringUtils.isBlank(userData.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserData.name is not populated");
        }
        if (StringUtils.isBlank(userData.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserData.email is not populated");
        }
        // TODO Normally I would get the DB to do it randomly
        // But I could not get it to work
        userData.setUserId(UUID.randomUUID());
        userDataRepository.save(userData);

        return userData.getUserId();
    }
}
