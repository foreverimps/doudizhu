package com.zj.platform.gamecenter.utils;

import java.nio.charset.Charset;
import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class FixedHeadProtocalEncoder extends ProtocolEncoderAdapter {
	@SuppressWarnings("unused")
	private final Charset charset;

	public FixedHeadProtocalEncoder(Charset charset) {
		this.charset = charset;
	}

	// 在此处实现包的编码工作，并把它写入输出流中
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		String value = (String) message;
		IoBuffer buf = IoBuffer.allocate(value.getBytes().length + 4);
		buf.setAutoExpand(true);
		if (value != null) {
			// buf.putUnsignedInt(value.getBytes().length);
			buf.putInt(value.getBytes().length);
			buf.put(value.trim().getBytes());
		}
		buf.flip();
		out.write(buf);
		System.out.println(new Date() + " 发送：" + value);
	}

}