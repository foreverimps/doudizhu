package com.zj.platform.gamecenter.test;

import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;

import com.zj.platform.gamecenter.utils.FixedHeadProtocalDecoder;
import com.zj.platform.gamecenter.utils.FixedHeadProtocalEncoder;

public class SocketIoHandler extends IoHandlerAdapter {

	private static final IoFilter LOGGING_FILTER = new LoggingFilter();

	private static final IoFilter CODEC_FILTER = new ProtocolCodecFilter(new FixedHeadProtocalEncoder(Charset.forName("UTF-8")),
	        new FixedHeadProtocalDecoder());

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		session.getFilterChain().addLast("codec", CODEC_FILTER);
		session.getFilterChain().addLast("logger", LOGGING_FILTER);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String msg = (String) message;
		System.out.println("Client Received: " + msg);
	}
}
