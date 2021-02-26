package com.tracker.portfolio.jwt;

import com.google.common.net.HttpHeaders;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "application.jwt")
@Data
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;
    private String tokenExpirationAfterDays = "14";

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
