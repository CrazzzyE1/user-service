package ru.litvak.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.litvak.userservice.model.dto.UserProfileDto;
import ru.litvak.userservice.model.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    // FIXME 20.06.2025:11:08: Возможно нужно удалить username вообще
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isOwner", defaultValue = "false")
    UserProfileDto toDto(UserProfile userProfile);
}
