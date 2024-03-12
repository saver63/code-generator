package com.yupi.web.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;


@SpringBootTest
class CosManagerTest {

    @Resource
    private CosManager cosManager;

    @Test
    void deleteObject() {
        cosManager.deleteObject("/test/logo.png");
    }

    @Test
    void deleteObjects() {
        cosManager.deleteObjects(Arrays.asList("user_avatar/1761929615175073793/IRvDowD5-logo.png","user_avatar/1761929615175073793/7NJCrHbt-logo.png"));
    }

    @Test
    void deleteDir() {
        cosManager.deleteDir("/test/");
    }
}