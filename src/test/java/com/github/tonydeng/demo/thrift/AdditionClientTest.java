package com.github.tonydeng.demo.thrift;

import com.github.tonydeng.demo.thrift.client.AdditionClient;
import org.apache.thrift.TException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tonydeng on 15/9/22.
 */
public class AdditionClientTest {
    private static final Logger log = LoggerFactory.getLogger(AdditionClientTest.class);
    @Test
    public void testAdd() throws TException {
        AdditionClient client = new AdditionClient();

        client.add(100,200);
    }
}
