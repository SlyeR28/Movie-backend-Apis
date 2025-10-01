package org.movieapi.Mappers;

import org.mapstruct.Mapper;
import org.movieapi.Entity.User;
import org.movieapi.Payloads.Request.RefreshTokenRequest;
import org.movieapi.Payloads.Request.UserRequest;
import org.movieapi.Payloads.Response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequest userRequest);

    // Convert User -> UserResponse (for sending to client)
    UserResponse toResponse(User user);


}
