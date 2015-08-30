package com.zj.platform.gamecenter.utils;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class FixedHeadProtocalDecoder implements ProtocolDecoder {

	private final AttributeKey CONTEXT = new AttributeKey(FixedHeadProtocalDecoder.class, "context");

	private final CharsetDecoder decoder;

	public FixedHeadProtocalDecoder() {
		this(Charset.defaultCharset());
	}

	public FixedHeadProtocalDecoder(Charset charset) {
		System.out.println("init FixedHeadProtocalDecoder...");
		this.decoder = charset.newDecoder();
	}

	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		// System.out.println("start decode.");
		IoBuffer bufTmp = null;
		byte[] buf = (byte[]) session.getAttribute(CONTEXT);
		if (buf == null) {
			// System.out.println("没有尚未处理的数据" + in.remaining());
			bufTmp = in;
			if (bufTmp.limit() >= 4 && bufTmp.getInt(bufTmp.position()) == 0) {
				bufTmp.getInt();
			}
		} else {
			// System.out.println("合并尚未处理的数据" + in.remaining());
			bufTmp = IoBuffer.allocate(buf.length + in.remaining());
			bufTmp.setAutoExpand(true);
			bufTmp.put(buf);
			bufTmp.put(in);
			bufTmp.flip();
		}
		boolean isReading = false;
		while (bufTmp.remaining() >= 4 && bufTmp.remaining() - 4 >= bufTmp.getInt(bufTmp.position())) { // 循环处理数据包
			// System.out.println("循环处理数据包");
			// System.out.println("当前剩余字节数：" + bufTmp.remaining());
			int dataLen = bufTmp.getInt();
			// System.out.println("当前剩余字节数：" + bufTmp.remaining());
			String content = bufTmp.getString(dataLen, decoder);
			System.out.println(new Date() + " 内容：" + content);
			out.write(content);
			session.removeAttribute(CONTEXT);
			isReading = true;
		}
		if (bufTmp.hasRemaining()) {
			// System.out.println("如果有剩余的数据，则放入Session中, Session ID 为：" +
			// CONTEXT
			// + "，存入字节数为 ：" + bufTmp.remaining());
			if (isReading && bufTmp.remaining() >= 4 && bufTmp.getInt(bufTmp.position()) == 0)
				bufTmp.getInt();
			byte[] tmpb = new byte[bufTmp.remaining()];
			bufTmp.get(tmpb);
			session.setAttribute(CONTEXT, tmpb);
		}

	}

	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
	}

	public void dispose(IoSession session) throws Exception {

		session.removeAttribute(CONTEXT);

	}

}