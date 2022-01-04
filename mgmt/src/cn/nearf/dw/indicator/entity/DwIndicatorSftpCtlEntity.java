package cn.nearf.dw.indicator.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Long;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**   
 * @Title: Entity
 * @Description: 指标下发管理
 * @author onlineGenerator
 * @date 2018-07-16 18:55:10
 * @version V1.0   
 *
 */
@Entity
@Table(name = "dw_indicator_sftp_ctl", schema = "")
@SuppressWarnings("serial")
public class DwIndicatorSftpCtlEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**指标ID*/
	@Excel(name="指标ID")
	private java.lang.Integer indicatorId;
	/**SFTP服务器地址*/
	@Excel(name="SFTP服务器地址")
	private java.lang.String sftpServer;
	/**SFTP用户名*/
	@Excel(name="SFTP用户名")
	private java.lang.String sftpUser;
	/**SFTP用户名密码*/
	@Excel(name="SFTP用户名密码")
	private java.lang.String sftpUserPasswd;
	/**导出目录*/
	@Excel(name="导出目录")
	private java.lang.String sourceFolder;
	/**目标目录*/
	@Excel(name="目标目录")
	private java.lang.String targetFolder;
	/**导出文件格式*/
	@Excel(name="导出文件格式")
	private java.lang.String dumpFileType;
	/**导出字段是否定长*/
	@Excel(name="导出字段是否定长")
	private java.lang.String fixedColumnLength;
	/**字段分隔符*/
	@Excel(name="字段分隔符")
	private java.lang.String splitChar;
	/**创建日期*/
	@Excel(name="创建日期",format = "yyyy-MM-dd")
	private java.util.Date createDate;
	/**更新日期*/
	@Excel(name="更新日期",format = "yyyy-MM-dd")
	private java.util.Date updateDate;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="ID",nullable=false,length=20)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  主键
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  指标ID
	 */
	@Column(name ="INDICATOR_ID",nullable=false,length=10)
	public java.lang.Integer getIndicatorId(){
		return this.indicatorId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  指标ID
	 */
	public void setIndicatorId(java.lang.Integer indicatorId){
		this.indicatorId = indicatorId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  SFTP服务器地址
	 */
	@Column(name ="SFTP_SERVER",nullable=true,length=100)
	public java.lang.String getSftpServer(){
		return this.sftpServer;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  SFTP服务器地址
	 */
	public void setSftpServer(java.lang.String sftpServer){
		this.sftpServer = sftpServer;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  SFTP用户名
	 */
	@Column(name ="SFTP_USER",nullable=true,length=100)
	public java.lang.String getSftpUser(){
		return this.sftpUser;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  SFTP用户名
	 */
	public void setSftpUser(java.lang.String sftpUser){
		this.sftpUser = sftpUser;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  SFTP用户名密码
	 */
	@Column(name ="SFTP_USER_PASSWD",nullable=true,length=100)
	public java.lang.String getSftpUserPasswd(){
		return this.sftpUserPasswd;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  SFTP用户名密码
	 */
	public void setSftpUserPasswd(java.lang.String sftpUserPasswd){
		this.sftpUserPasswd = sftpUserPasswd;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  导出目录
	 */
	@Column(name ="SOURCE_FOLDER",nullable=true,length=300)
	public java.lang.String getSourceFolder(){
		return this.sourceFolder;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  导出目录
	 */
	public void setSourceFolder(java.lang.String sourceFolder){
		this.sourceFolder = sourceFolder;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  目标目录
	 */
	@Column(name ="TARGET_FOLDER",nullable=true,length=300)
	public java.lang.String getTargetFolder(){
		return this.targetFolder;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  目标目录
	 */
	public void setTargetFolder(java.lang.String targetFolder){
		this.targetFolder = targetFolder;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  导出文件格式
	 */
	@Column(name ="DUMP_FILE_TYPE",nullable=true,length=300)
	public java.lang.String getDumpFileType(){
		return this.dumpFileType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  导出文件格式
	 */
	public void setDumpFileType(java.lang.String dumpFileType){
		this.dumpFileType = dumpFileType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  导出字段是否定长
	 */
	@Column(name ="FIXED_COLUMN_LENGTH",nullable=true,length=1)
	public java.lang.String getFixedColumnLength(){
		return this.fixedColumnLength;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  导出字段是否定长
	 */
	public void setFixedColumnLength(java.lang.String fixedColumnLength){
		this.fixedColumnLength = fixedColumnLength;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  字段分隔符
	 */
	@Column(name ="SPLIT_CHAR",nullable=true,length=300)
	public java.lang.String getSplitChar(){
		return this.splitChar;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  字段分隔符
	 */
	public void setSplitChar(java.lang.String splitChar){
		this.splitChar = splitChar;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=false,length=20)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=false,length=20)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
}
