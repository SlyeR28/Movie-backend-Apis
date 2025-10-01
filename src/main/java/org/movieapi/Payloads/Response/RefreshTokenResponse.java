package org.movieapi.Payloads.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenResponse {

    private Integer tokenId;
    private String refreshToken;
    private Instant expirationTime;
    private Long userId;
    private String username;
    private String email;
}
