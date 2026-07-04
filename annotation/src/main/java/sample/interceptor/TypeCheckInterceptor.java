package sample.interceptor;

import io.quarkus.logging.Log;
import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Logging
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 1)
public class TypeCheckInterceptor {

    @AroundInvoke
    Object around(InvocationContext ctx) throws Exception {

        var typeCheck = ctx.getMethod().getAnnotation(TypeCheck.class);
        System.out.println("annotation value: " + typeCheck.value());

        var argParams =  ctx.getParameters();
        System.out.println("number of parameters: " + argParams.length);

        for (var param : argParams) {
            System.out.println(param);
        }
        
        try {
            return ctx.proceed();
        } finally {
            // pass
        }
    }
}
