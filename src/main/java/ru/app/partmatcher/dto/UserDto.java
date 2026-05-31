package ru.app.partmatcher.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Set<String> roles;
}
