package com.yupi.web.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;

/**
 * 可以看成一个服务器，设置一个vertical，相当于提供一个服务器，当做一个小的Tomcat
 */
public class MainVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {

        // 服务器创建服务
        vertx.createHttpServer()
                // 处理请求
                .requestHandler(reg->{
                    reg.response().putHeader("Content-Type","application/json")
                            .end("ok");
                })
                // Start listening
                .listen(8888)
                // Print the port
                .onSuccess(server ->
                        System.out.println(
                                "HTTP server started on port " + server.actualPort()
                        )
                );
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Verticle mainVerticl = new MainVerticle();
        vertx.deployVerticle(mainVerticl);
    }
}