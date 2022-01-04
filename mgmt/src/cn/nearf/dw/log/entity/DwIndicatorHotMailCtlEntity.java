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
 * @Description: 告警邮件管理
 * @author onlineGenerator
 * @date 2019-05-21 11:03:58
 * @version V1.0   
 *
 */
@Entity
@Table(name = "dw_indicator_hot_mail_ctl", schema = "")
@SuppressWarnings("serial")
public class DwIndicatorHotMailCtlEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**分组标记*/
	@Excel(name="分组标记")
	private java.lang.String groupName;
	/**收件人名*/
	@Excel(name="收件人名")
	private java.lang.String userName;
	/**收件人邮箱地址*/
	@Excel(name="收件人邮箱地址")
	private java.lang.String userEmailAddr;
	/**紧急标志*/
	@Excel(name="紧急标志")
	private java.lang.String urgentFlg;
	/**记录创建时间*/
	@Excel(name="记录创建时间",format = "yyyy-MM-dd")
	private java.util.Date createDate;
	/**记录更新时间*/
	@Excel(name="记录更新时间",format = "yyyy-MM-dd")
	private java.util.Date updateDate;
	
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  分组标记
	 */
	@Column(name ="GROUP_NAME",nullable=true,length=100)
	public java.lang.String getGroupName(){
		return this.groupName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  分组标记
	 */
	public void setGroupName(java.lang.String groupName){
		this.groupName = groupName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收件人名
	 */
	@Column(name ="USER_NAME",nullable=true,length=100)
	public java.lang.String getUserName(){
		return this.userName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收件人名
	 */
	public void setUserName(java.lang.String userName){
		this.userName = userName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收件人邮箱地址
	 */
	@Column(name ="USER_EMAIL_ADDR",nullable=true,length=100)
	public java.lang.String getUserEmailAddr(){
		return this.userEmailAddr;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收件人邮箱地址
	 */
	public void setUserEmailAddr(java.lang.String userEmailAddr){
		this.userEmailAddr = userEmailAddr;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  紧急标志
	 */
	@Column(name ="URGENT_FLG",nullable=true,length=2)
	public java.lang.String getUrgentFlg(){
		return this.urgentFlg;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  紧急标志
	 */
	public void setUrgentFlg(java.lang.String urgentFlg){
		this.urgentFlg = urgentFlg;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  记录创建时间
	 */
	@Column(name ="CREATE_DATE",nullable=true,length=20)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  记录创建时间
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  记录更新时间
	 */
	@Column(name ="UPDATE_DATE",nullable=true,length=20)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  记录更新时间
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
}
