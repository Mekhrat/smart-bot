package kz.kaznu.smartbot.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Configuration
public class PropertyConfig {

    @Bean
    public Properties properties() {
        Properties properties = new Properties();
        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("messages.properties");
            if (input != null) {
                properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
