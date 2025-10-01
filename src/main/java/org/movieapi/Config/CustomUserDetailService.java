package org.movieapi.Config;

import lombok.RequiredArgsConstructor;
import org.movieapi.Entity.User;
import org.movieapi.Exceptions.UserNotFoundException;
import org.movieapi.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with given username" + username));

        return new CustomUserDetail(user);
    }
}
