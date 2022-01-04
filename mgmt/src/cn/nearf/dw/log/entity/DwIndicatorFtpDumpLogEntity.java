package cn.nearf.dw.log.entity;

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
 * @Description: 指标导出日志
 * @author onlineGenerator
 * @date 2019-05-21 11:04:21
 * @version V1.0   
 *
 */
@Entity
@Table(name = "dw_indicator_ftp_dump_log", schema = "")
@SuppressWarnings("serial")
public class DwIndicatorFtpDumpLogEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**指标*/
	@Excel(name="指标")
	private java.lang.Integer indicatorId;
	/**SFTP状态*/
	@Excel(name="SFTP状态")
	private java.lang.String sftpStatus;
	/**错误信息*/
	@Excel(name="错误信息")
	private java.lang.String errorInfo;
	/**创建时间*/
	@Excel(name="创建时间",format = "yyyy-MM-dd")
	private java.util.Date createDate;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="ID",nullable=false,length=10)
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
	 *@return: java.lang.Integer  指标
	 */
	@Column(name ="INDICATOR_ID",nullable=true,length=10)
	public java.lang.Integer getIndicatorId(){
		return this.indicatorId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  指标
	 */
	public void setIndicatorId(java.lang.Integer indicatorId){
		this.indicatorId = indicatorId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  SFTP状态
	 */
	@Column(name ="SFTP_STATUS",nullable=true,length=50)
	public java.lang.String getSftpStatus(){
		return this.sftpStatus;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  SFTP状态
	 */
	public void setSftpStatus(java.lang.String sftpStatus){
		this.sftpStatus = sftpStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  错误信息
	 */
	@Column(name ="ERROR_INFO",nullable=true,length=3000)
	public java.lang.String getErrorInfo(){
		return this.errorInfo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  错误信息
	 */
	public void setErrorInfo(java.lang.String errorInfo){
		this.errorInfo = errorInfo;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_DATE",nullable=true,length=20)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
}
