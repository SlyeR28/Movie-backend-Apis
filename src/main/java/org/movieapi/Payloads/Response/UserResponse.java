package org.movieapi.Payloads.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.movieapi.Entity.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {


    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private boolean isEnabled;
}
