package org.movieapi.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.movieapi.Entity.RefreshToken;
import org.movieapi.Payloads.Request.RefreshTokenRequest;
import org.movieapi.Payloads.Response.RefreshTokenResponse;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {

    @Mapping(source = "userId", target = "user.id")
    RefreshToken toEntity(RefreshTokenRequest request);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.email", target = "email")
    RefreshTokenResponse toResponse(RefreshToken token);
}
