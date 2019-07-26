package com.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
    public static void main(String[] args) {
        int port =9897;
        new TimeServer().bind(port);
    }

    private void bind(int port)  {
        /**
         * bossGroup用于建立与客户端的连接
         * workerGroup用进行SocketChannel网络读写
         */

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new ChildChannelHandler());

        /*服务器启动辅助类配置完成后 ，服务端绑定端口*/
        ChannelFuture f = b.bind(port).sync();
        System.out.println(Thread.currentThread().getName()+"服务器开始监听9897端口:");
        /**
         * 关闭服务端连接，在关闭main
         */
         f.channel().closeFuture().sync();
        }catch(InterruptedException e){
           e.getMessage();
        }finally {
            /*优雅推出，关闭线程池**/
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel arg0) throws Exception {
            arg0.pipeline().addLast(new TimeServerHandler());
        }
    }


}
