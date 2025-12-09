package  com.app.relief.service;

import com.app.relief.entity.RefreshToken;
import com.app.relief.entity.User;
import com.app.relief.repository.RefreshTokenRepository;
import com.app.relief.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${security.jwt.refresh-expiration}")
    private long refreshExpiration;

    private final RefreshTokenRepository refreshTokenrepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenrepository) {
        this.refreshTokenrepository = refreshTokenrepository;
    }

    public RefreshToken generateRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        // refreshExpiration is treated as milliseconds; adjust to seconds with plusSeconds(...) if needed
        token.setExpiryDate(Date.from(Instant.now().plusMillis(refreshExpiration)));
        return refreshTokenrepository.save(token);
    }

    public Date refreshTokenExpirationDate() {
        return new Date(System.currentTimeMillis() + refreshExpiration);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().before(new Date()) || token.isRevoked()) {
            token.setRevoked(true);
            refreshTokenrepository.save(token);
            throw new RuntimeException("Refresh token expired or revoked.");
        }

        return token;
    }
}

