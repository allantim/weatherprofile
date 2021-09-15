package org.tim.weathertracker.core.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class CreateUserDataResponseDto {
    private UUID userId;
}
