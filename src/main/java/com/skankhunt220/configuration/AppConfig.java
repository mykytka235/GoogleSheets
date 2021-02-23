package com.skankhunt220.configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "google.sheets")
public class AppConfig {
    private @Setter @Getter String creds;
    private @Setter @Getter String appname;

    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS);

    @Bean
    public Sheets createSheetsService() throws IOException, GeneralSecurityException {
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                GoogleCredential.fromStream(Thread.currentThread()
                                                .getContextClassLoader()
                                                .getResourceAsStream(creds)
                                            ).createScoped(SCOPES)
                                ).setApplicationName(appname).build();
    }
}
