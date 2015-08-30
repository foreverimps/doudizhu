package com.zj.platform.gamecenter.utils;

import com.zj.platform.gamecenter.constant.ResultEnum;
import com.zj.platform.gamecenter.dto.SocketOutMessage;

/**
 * <p>
 * Title: 米谷_[交易平台]
 * </p>
 * <p>
 * Description: [结果构建工具]
 * </p>
 * 
 * @author loujinhe
 * @version $Revision$ 2014年2月12日
 * @author (lastest modification by $Author$)
 * @since 1.0
 */
public class BuildResultUtil {

	/**
	 * <p>
	 * 构建输出结果对象
	 * </p>
	 * 
	 * @param code
	 *            返回码
	 * @param content
	 *            返回信息
	 * @return 输出结果对象
	 */
	public static final SocketOutMessage buildOutResult(int code, Object content) {
		SocketOutMessage result = new SocketOutMessage();
		result.setCode(code);
		result.setContent(content);
		return result;
	}

	/**
	 * 构建输出结果对象
	 * 
	 * @param code
	 *            返回码
	 * @return
	 */
	public static final SocketOutMessage buildOutResult(int code) {
		SocketOutMessage result = new SocketOutMessage();
		result.setCode(code);
		result.setContent(ResultEnum.getDescription(code));
		return result;
	}

	/**
	 * 构建输出结果对象
	 * 
	 * @param code
	 *            返回码
	 * @return
	 */
	public static final SocketOutMessage buildOutResult(ResultEnum resultEnum) {
		SocketOutMessage result = new SocketOutMessage();
		result.setCode(resultEnum.getCode());
		result.setContent("");
		return result;
	}

	/**
	 * 构建输出结果对象
	 * 
	 * @param code
	 *            返回码
	 * @return
	 */
	public static final SocketOutMessage buildOutResult(ResultEnum resultEnum, Object obj) {
		SocketOutMessage result = new SocketOutMessage();
		result.setCode(resultEnum.getCode());
		result.setContent(obj);
		return result;
	}
}
