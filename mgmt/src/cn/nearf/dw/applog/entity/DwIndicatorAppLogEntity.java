package cn.nearf.dw.applog.entity;

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
 * @Description: app日志记录
 * @author onlineGenerator
 * @date 2018-08-08 17:11:07
 * @version V1.0   
 *
 */
@Entity
@Table(name = "dw_indicator_app_log", schema = "")
@SuppressWarnings("serial")
public class DwIndicatorAppLogEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**应用程序名*/
	@Excel(name="应用程序名")
	private java.lang.String app;
	/**发生时间*/
	@Excel(name="发生时间",format = "yyyy-MM-dd")
	private java.util.Date createDate;
	/**级别*/
	@Excel(name="级别")
	private java.lang.String level;
	/**处理状态*/
	@Excel(name="处理状态")
	private java.lang.Integer status;
	/**日志内容*/
	@Excel(name="日志内容")
	private java.lang.String content;
	/**更新日期*/
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  应用程序名
	 */
	@Column(name ="APP",nullable=false,length=255)
	public java.lang.String getApp(){
		return this.app;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  应用程序名
	 */
	public void setApp(java.lang.String app){
		this.app = app;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  发生时间
	 */
	@Column(name ="CREATE_DATE",nullable=false,length=20)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  发生时间
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  级别
	 */
	@Column(name ="LEVEL",nullable=false,length=20)
	public java.lang.String getLevel(){
		return this.level;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  级别
	 */
	public void setLevel(java.lang.String level){
		this.level = level;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  处理状态
	 */
	@Column(name ="STATUS",nullable=false,length=1)
	public java.lang.Integer getStatus(){
		return this.status;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  处理状态
	 */
	public void setStatus(java.lang.Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  日志内容
	 */
	@Column(name ="CONTENT",nullable=false,length=2000)
	public java.lang.String getContent(){
		return this.content;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  日志内容
	 */
	public void setContent(java.lang.String content){
		this.content = content;
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
