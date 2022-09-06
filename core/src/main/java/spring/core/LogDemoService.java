package spring.core;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import spring.core.common.MyLogger;

@Service
@RequiredArgsConstructor
public class LogDemoService {

    private final ObjectProvider<MyLogger> myLoggerProvider;

    MyLogger myLogger = myLoggerProvider.getObject();
    public void logic(String id) {myLogger.log("service id = " + id);
    }
}
