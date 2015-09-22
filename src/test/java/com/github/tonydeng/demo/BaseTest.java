package com.github.tonydeng.demo;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple App.
 */
public class BaseTest{
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @Test
    public void test(){
        log.info("base test......");
    }
}
