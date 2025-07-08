package ru.litvak.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.litvak.userservice.model.dto.FriendRequestDto;
import ru.litvak.userservice.model.entity.FriendRequest;
import ru.litvak.userservice.model.entity.UserProfile;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface FriendRequestMapper {

    @Mapping(target = "sender", source = "sender", qualifiedByName = "mapUserProfileToId")
    @Mapping(target = "receiver", source = "receiver", qualifiedByName = "mapUserProfileToId")
    FriendRequestDto toDto(FriendRequest entity);

    @Named("mapUserProfileToId")
    default UUID mapUserProfileToId(UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }
        return userProfile.getId();
    }

    List<FriendRequestDto> toListDto(List<FriendRequest> list);
}
