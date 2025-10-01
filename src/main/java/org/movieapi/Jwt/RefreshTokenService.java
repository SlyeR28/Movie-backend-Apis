package org.movieapi.Jwt;

import lombok.RequiredArgsConstructor;
import org.movieapi.Entity.RefreshToken;
import org.movieapi.Entity.User;
import org.movieapi.Exceptions.UserNotFoundException;
import org.movieapi.Repository.RefreshTokenRepository;
import org.movieapi.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserRepository  userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("username not found"));
       RefreshToken refreshToken = user.getRefreshToken();
       if(refreshToken == null){
           RefreshToken newRefreshToken =  RefreshToken.builder()
                   .refreshToken(UUID.randomUUID().toString())
                   .expirationTime(Instant.now().plusMillis(5*60*60*100000))
                   .user(user)
                   .build();
           refreshTokenRepository.save(newRefreshToken);
       }

       return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken byRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new UserNotFoundException("refreshToken not found"));

        if(byRefreshToken.getExpirationTime().compareTo(Instant.now().minusMillis(5*60*60*1000))<0){
            refreshTokenRepository.delete(byRefreshToken);
            throw new UserNotFoundException("refreshToken expired");
        }
        return byRefreshToken;
    }
}
