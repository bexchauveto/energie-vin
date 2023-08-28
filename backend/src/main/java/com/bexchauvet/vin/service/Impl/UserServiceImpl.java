package com.bexchauvet.vin.service.Impl;

import com.bexchauvet.vin.domain.User;
import com.bexchauvet.vin.error.exception.BadLoginUnauthorizedException;
import com.bexchauvet.vin.repository.UserRepository;
import com.bexchauvet.vin.rest.dto.MessageDTO;
import com.bexchauvet.vin.rest.dto.TokenDTO;
import com.bexchauvet.vin.rest.dto.UserDTO;
import com.bexchauvet.vin.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final JwtEncoder encoder;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Provided username doesn't exists in the database"));
        return org.springframework.security.core.userdetails.User.builder().username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(String[]::new)).build();
    }

    @Override
    public TokenDTO generateToken(UserDTO user) {
        UserDetails userDetails = this.loadUserByUsername(user.getUsername());
        if (passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
            Instant now = Instant.now();
            long expiry = 36000L;
            List<String> scope = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(userDetails.getUsername())
                    .claim("roles", scope)
                    .build();
            return new TokenDTO(this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
        } else {
            throw new BadLoginUnauthorizedException();
        }
    }

    @Override
    @Transactional
    public MessageDTO create(UserDTO userDTO) {
        User user = this.userRepository.save(new User(null, userDTO.getUsername(),
                passwordEncoder.encode(userDTO.getPassword()), userDTO.getRoles(), null, null));
        return new MessageDTO(String.format("User with ID %s has been created", user.getUserId()), user.getUserId());
    }
}
