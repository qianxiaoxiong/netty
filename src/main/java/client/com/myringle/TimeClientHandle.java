package client.com.myringle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Logger;

/**
 * 用与对网络读写操作
 */
 class TimeClientHandle extends ChannelInboundHandlerAdapter {
    /*客户端与server端建立TCP连接后 激活 channelactive */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String reqMsg = "我是客户端" + Thread.currentThread().getName();
        byte[] bytes = reqMsg.getBytes("UTF-8");
        /*缓存对象 ByteBuf.writeBytes(Byte b) 容易混淆*/
        ByteBuf buf = Unpooled.buffer(bytes.length);
        buf.writeBytes(bytes);

        ctx.writeAndFlush(buf);

    }

    /**
     * 读取服务器上的信息，或者说服务器放回应答时
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println(Thread.currentThread().getName()+"-----" + "Server return Message" +"-----   "+body);
        ctx.close();
    }

    /**
     * 发生异常时打印日志  释放客户端资源
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Logger.getLogger("Unexpected exception from downstream : " + cause.getMessage());
        ctx.close();
    }
}