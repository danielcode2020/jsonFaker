package com.example.jsonfaker.configuration;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwoFactorAuthConfig {
    @Bean

    public SecretGenerator secretGenerator(){
        return new DefaultSecretGenerator(64);
    }

    @Bean
    public QrGenerator qrGenerator(){
        return new ZxingPngQrGenerator();
    }

    @Bean
    public CodeVerifier codeVerifier(){
        return new DefaultCodeVerifier(new DefaultCodeGenerator(), new SystemTimeProvider());
    }
}
