package com.hamkelasi.bll;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailSettings {
    private static EmailSettings defaultInstance;

    private String emailAddress;
    private String emailHost;
    private String emailPort;
    private String adminEmail;

    private EmailSettings() {
        loadSettings();
    }

    public static EmailSettings getDefault() {
        if (defaultInstance == null) {
            defaultInstance = new EmailSettings();
        }
        return defaultInstance;
    }

    private void loadSettings() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("email.properties")) {
            if (input == null) {
                // Fallback to hardcoded defaults if file not found
                emailAddress = "info@hamkelasi.com";
                emailHost = "localhost";
                emailPort = "25";
                adminEmail = "a@b.com";
                return;
            }
            props.load(input);
            emailAddress = props.getProperty("emailAddress", "info@hamkelasi.com");
            emailHost = props.getProperty("emailHost", "localhost");
            emailPort = props.getProperty("emailPort", "25");
            adminEmail = props.getProperty("adminEmail", "a@b.com");
        } catch (IOException e) {
            // Log error if needed; fallback to defaults
            emailAddress = "info@hamkelasi.com";
            emailHost = "localhost";
            emailPort = "25";
            adminEmail = "a@b.com";
        }
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getEmailHost() {
        return emailHost;
    }

    public String getEmailPort() {
        return emailPort;
    }

    public String getAdminEmail() {
        return adminEmail;
    }
}