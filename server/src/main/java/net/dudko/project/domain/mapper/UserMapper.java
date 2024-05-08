package net.dudko.project.domain.mapper;

import net.dudko.project.domain.entity.User;
import net.dudko.project.model.dto.ProfileDto;

public class UserMapper {

    public static ProfileDto mapToProfileDto(User user) {
        return ProfileDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .passwordHash(user.getPassword())
                .build();
    }

}
