package ru.litvak.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.litvak.userservice.model.dto.ShortUserProfileDto;
import ru.litvak.userservice.model.dto.UserProfileDto;
import ru.litvak.userservice.model.entity.ShortUserProfile;
import ru.litvak.userservice.model.entity.UserProfile;

import java.util.List;

import static ru.litvak.userservice.enumerated.PrivacyLevel.PUBLIC;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isOwner", defaultValue = "false")
    @Mapping(target = "isPublic", source = ".", qualifiedByName = "isPublic")
    UserProfileDto toDto(UserProfile entity);

    @Mapping(target = "fullName", source = ".", qualifiedByName = "fullName")
    UserProfile toEntity(UserProfileDto dto);

    @Named("isPublic")
    default Boolean isPublic(UserProfile entity) {
        return PUBLIC.equals(entity.getPrivacyLevel());
    }

    @Named("fullName")
    default String fullName(UserProfileDto dto) {
        return dto.getFirstName() + "  " + dto.getFamilyName();
    }

    ShortUserProfileDto toShortDto(ShortUserProfile entity);

    List<UserProfileDto> toListDto(List<UserProfile> list);
}
