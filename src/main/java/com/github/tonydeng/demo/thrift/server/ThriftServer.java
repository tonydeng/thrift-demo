package com.github.tonydeng.demo.thrift.server;

import com.github.tonydeng.demo.thrift.api.AdditionService;
import com.github.tonydeng.demo.thrift.handler.AdditionServiceHandler;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tonydeng on 15/9/22.
 */
public class ThriftServer {
    private static  final Logger log  = LoggerFactory.getLogger(ThriftServer.class);

    public static void  StartsimpleServer(AdditionService.Processor<AdditionServiceHandler> processor){
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
//            log.info("Starting the simple server.......");
            System.out.println("Starting the simple server......");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        StartsimpleServer(new AdditionService.Processor<>(new AdditionServiceHandler()));
    }
}
