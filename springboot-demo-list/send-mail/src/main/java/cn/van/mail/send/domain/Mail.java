package cn.van.mail.send.domain;

import lombok.Data;
import org.thymeleaf.context.Context;

import java.util.Date;

/**
* @Title: Mail
* @Description: 邮件实体类
* @Version:1.0.0  
* @author pancm
* @date 2019年1月28日
*/
@Data
public class Mail {
	/** 邮件id */
	private String id;

	/** 邮件发送人 */
	private String sender;
	
	/** 邮件接收人 （多个邮箱则用逗号","隔开） */
	private String receiver;
	
	/** 邮件主题 */
	private String subject;
	
	/** 邮件内容*/
	private String text;

	private Date sentDate;//发送时间

	private String cc;//抄送（多个邮箱则用逗号","隔开）
	private String bcc;//密送（多个邮箱则用逗号","隔开）
	private String status;//状态
	private String error;//报错信息
//	private MultipartFile[] multipartFiles;//邮件附件

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
