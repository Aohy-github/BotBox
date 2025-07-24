package com.kob.backend.consumer.utils;

import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;



// 该类会手动将webSocketServer 注册为Spring 的容器中进行管理
/// 因为带有 @ServerEndpoint 的类是由底层的 WebSocket 容器（Tomcat）自己 new 出来的，
/// 并不是 Spring IoC 容器管理的 Bean，所以 Spring 的 @Autowired／@Scope("prototype") 都不会生效。
@Component
public class SpringConfigurator extends ServerEndpointConfig.Configurator {
    private static ApplicationContext ctx;
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringConfigurator.ctx = applicationContext;
    }
    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return ctx.getBean(endpointClass);
    }
}