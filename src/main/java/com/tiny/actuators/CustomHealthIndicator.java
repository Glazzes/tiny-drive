package com.tiny.actuators;

import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
       try {
            URL url = new URL("http://localhost:8080/health");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int status = conn.getResponseCode();
            if( status >= 200 && status <300 ){
                return Health.up().build();
            }else{
                return Health.down()
                       .withDetail("Http status code", status)
                       .build();
            }
       } catch (Exception e) {
           return Health.down(e).build();
       }
    }
    
}
