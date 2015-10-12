package com.github.tonydeng.demo;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple App.
 */
public class BaseTest{
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void test(){
        log.info("base test......");
    }
}
