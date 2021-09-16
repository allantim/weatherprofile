package org.tim.weathertracker.app.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tim.weathertracker.core.entities.UserData;
import org.tim.weathertracker.core.entities.dto.CreateUserDataResponseDto;
import org.tim.weathertracker.core.usecase.user.UserSaver;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/user")
@Slf4j
public class UserController {

    private final UserSaver userSaver;

    public UserController(UserSaver userSaver) {
        this.userSaver = userSaver;
    }

    @PostMapping
    public CreateUserDataResponseDto registerUser(@RequestBody UserData userData) {
        UUID userId = userSaver.save(userData);
        return CreateUserDataResponseDto.builder().userId(userId).build();
    }
}
