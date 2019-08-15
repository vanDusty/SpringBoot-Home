package cn.van.mail.send.domain;

import lombok.Data;
import org.thymeleaf.context.Context;

/**
* @Title: Mail
* @Description: 邮件实体类
* @Version:1.0.0  
* @author pancm
* @date 2019年1月28日
*/
@Data
public class Mail {
	
	/** 发送者*/
	private String sender;
	
	/** 接受者  */
	private String receiver;
	
	/** 消息主题 */
	private String subject;
	
	/** 消息内容*/
	private String text;
	/**
	 * 附件/文件地址
	 */
	private String filePath;
	/**
	 * 附件/文件名称
	 */
	private String fileName;
	/**
	 * 模版名称
	 */
	private String emailTemplateName;
	/**
	 * 模版内容
	 */
	private Context emailTemplateContext;
	
}
