package sample.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Logging
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {

    @AroundInvoke
    Object around(InvocationContext ctx) throws Exception {

        System.out.println("logging interceptror start");

        try {
            return ctx.proceed();
        } finally {
            System.out.println("logging interceptor end");
        }
    }
}