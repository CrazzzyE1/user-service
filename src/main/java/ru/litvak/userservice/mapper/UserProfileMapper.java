package ru.litvak.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.litvak.userservice.model.dto.ShortUserProfileDto;
import ru.litvak.userservice.model.dto.UserProfileDto;
import ru.litvak.userservice.model.entity.ShortUserProfile;
import ru.litvak.userservice.model.entity.UserProfile;

import static ru.litvak.userservice.enumerated.PrivacyLevel.PUBLIC;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isOwner", defaultValue = "false")
    @Mapping(target = "isPublic", source = ".", qualifiedByName = "isPublic")
    UserProfileDto toDto(UserProfile entity);

    @Named("isPublic")
    default Boolean isPublic(UserProfile userProfile) {
        return PUBLIC.equals(userProfile.getPrivacyLevel());
    }

    ShortUserProfileDto toShortDto(ShortUserProfile entity);
}
