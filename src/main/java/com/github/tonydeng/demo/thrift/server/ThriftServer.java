package com.github.tonydeng.demo.thrift.server;

import com.github.tonydeng.demo.thrift.api.AdditionService;
import com.github.tonydeng.demo.thrift.api.LoginService;
import com.github.tonydeng.demo.thrift.api.RegisterService;
import com.github.tonydeng.demo.thrift.handler.AdditionServiceHandler;
import com.github.tonydeng.demo.thrift.handler.LoginServiceHandler;
import com.github.tonydeng.demo.thrift.handler.RegisterServiceHandler;
import com.github.tonydeng.demo.thrift.ping.api.PingPongService;
import com.github.tonydeng.demo.thrift.ping.server.PingPongServiceBean;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tonydeng on 15/9/22.
 */
public class ThriftServer {
    private static final Logger log = LoggerFactory.getLogger(ThriftServer.class);
    private static final int PORT = 7911;

    public static void StartsimpleServer(AdditionService.Processor<AdditionServiceHandler> processor) {
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

    private void startMuitiServer() {

        try {
            TServerSocket serverTransport = new TServerSocket(PORT);
            // 用户登录
            LoginService.Processor loginProcessor = new LoginService.Processor(
                    new LoginServiceHandler());
            // 用户注册
            RegisterService.Processor registerProcessor = new RegisterService.Processor(
                    new RegisterServiceHandler());
            // Factory protFactory = new TBinaryProtocol.Factory(true, true);
            // TServer server = new TThreadPoolServer(new
            // TThreadPoolServer.Args(serverTransport)
            // .processor(loginProcessor));
            TMultiplexedProcessor processor = new TMultiplexedProcessor();
            processor.registerProcessor("LoginService", loginProcessor);
            processor.registerProcessor("RegisterService", registerProcessor);
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(
                    serverTransport).processor(processor));
            System.out.println("Starting server on port 7911 ...");
            server.serve();


        } catch (TTransportException e) {
            e.printStackTrace();
        }

    }

    private void start() {
        if (log.isInfoEnabled())
            log.info("start thrift server, port is {} ......", PORT);

        try {
            TServerTransport transport = new TServerSocket(PORT);
            TThreadPoolServer.Args processor = new TThreadPoolServer.Args(transport)
                    .inputTransportFactory(new TFramedTransport.Factory())
                    .outputTransportFactory(new TFramedTransport.Factory())
                    .protocolFactory(new TCompactProtocol.Factory())
                    .processor(new PingPongService.Processor<>(new PingPongServiceBean()));

            TThreadPoolServer server = new TThreadPoolServer(processor);

            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
            if (log.isErrorEnabled())
                log.error("start thrift server error:'{}'", e);
        }
    }

    public static void main(String[] args) {
//        StartsimpleServer(new AdditionService.Processor<>(new AdditionServiceHandler()));
//        startMuitiServer();
        ThriftServer server = new ThriftServer();
        server.start();
    }
}
