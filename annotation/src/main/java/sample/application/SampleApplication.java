package sample.application;

import jakarta.enterprise.context.ApplicationScoped;
import sample.interceptor.Logging;

@ApplicationScoped
public class SampleApplication {
    
    @Logging
    public String getMessage() {
        return "hello application";
    }
}
