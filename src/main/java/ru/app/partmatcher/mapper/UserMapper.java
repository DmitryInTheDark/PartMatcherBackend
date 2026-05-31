package ru.app.partmatcher.mapper;

import ru.app.partmatcher.dto.UserDto;
import ru.app.partmatcher.entity.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
                .build();
    }
}
