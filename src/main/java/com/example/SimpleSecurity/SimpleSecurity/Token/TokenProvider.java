package com.example.SimpleSecurity.SimpleSecurity.Token;

import com.example.SimpleSecurity.SimpleSecurity.Exception.UserNotFoundException;
import com.example.SimpleSecurity.SimpleSecurity.Model.MyUserDetails;
import com.example.SimpleSecurity.SimpleSecurity.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Getter
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final UserRepository userRepository;
    @Value("${security.token.expired-time}")
    private Integer tokenValidityInMilis;
    @Value("${security.token.secret-key}")
    private String secretKey;

    public String createToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.tokenValidityInMilis);

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(username)
                .setIssuedAt(now).signWith(SignatureAlgorithm.HS512, this.secretKey)
                .setExpiration(validity).compact();
    }

    @Transactional
    public Authentication getAuthentication(String token) {
        String username = Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        MyUserDetails userDetails = new MyUserDetails(
                userRepository.findUserByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException(username)));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
