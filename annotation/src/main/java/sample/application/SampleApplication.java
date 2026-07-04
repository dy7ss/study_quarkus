package sample.application;

import jakarta.enterprise.context.ApplicationScoped;
import sample.interceptor.Logging;
import sample.interceptor.TypeCheck;

@ApplicationScoped
public class SampleApplication {

    @Logging
    @TypeCheck
    public String getMessage() {
        return "hello application";
    }

    @Logging
    @TypeCheck("annotation-value")
    public String getMessage(String type1, String type2) {
        return "hello application with type";
    }
}
