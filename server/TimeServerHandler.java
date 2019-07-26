package com.netty.server;

import io.netty.buffer.ByteBuf;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 *  蹦来是要继承ChanellHandle,但是这个类需要实现许多方法，所以他的实现类
 *  时ChannelHandlerAdapter,    channelHandleAdapter的继承类时
 *  ChannelInboundHandlerAdapter
 *  chanellhandle  是对I/O处理或者拦截，在他的ChanellPipeLine方法中
 *  是将他交给下一个handler
 */

public class TimeServerHandler extends ChannelInboundHandlerAdapter{


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
       /*新建一个字节数组，目的是为搞到buf缓存中的数组*/
        byte[] reg = new byte[buf.readableBytes()];
        /*将缓存中的数组放到 中去*/
        buf.readBytes(reg);
        String res = new String(reg, "UTF-8");
        System.out.println(Thread.currentThread().getName()+"+++++"+"Server return data"+"---"+res);

        /**
         * 收到消息开始回复
         */
        String resp ="I am server,消息接收：success";
        ByteBuf byteBuf = Unpooled.copiedBuffer(resp.getBytes());
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        /**flush：将消息发送队列中的消息写入到 SocketChannel 中发送给对方，为了频繁的唤醒 Selector 进行消息发送
         * Netty 的 write 方法并不直接将消息写如 SocketChannel 中，调用 write 只是把待发送的消息放到发送缓存数组中，再通过调用 flush
         * 方法，将发送缓冲区的消息全部写入到 SocketChannel 中
         * */
         ctx.flush();
            }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
