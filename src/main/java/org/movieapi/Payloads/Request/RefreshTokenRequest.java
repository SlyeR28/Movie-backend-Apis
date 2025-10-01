package org.movieapi.Payloads.Request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRequest {


    @NotBlank(message = "Refresh token cannot be blank")
    @Size(max = 500, message = "Refresh token cannot exceed 500 characters")
    private String refreshToken;

    @NotNull(message = "Expiration time is required")
    @Future(message = "Expiration time must be a future date and time")
    private Instant expirationTime;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

}
