package cn.nearf.ggz.attachment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

@Entity
@Table(name = "erp_attachment", schema = "")
@SuppressWarnings("serial")
public class AttachmentEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**创建人名称*/
	private java.lang.String createName;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人名称*/
	private java.lang.String updateName;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**显示名*/
	@Excel(name="显示名")
	private java.lang.String title;
	/**路径*/
	@Excel(name="路径")
	private java.lang.String path;
	/**后缀名*/
	@Excel(name="后缀名")
	private java.lang.String extend;
	/**ContentType*/
	@Excel(name="ContentType")
	private java.lang.String contentType;
	/**业务关键字*/
	@Excel(name="业务关键字")
	private java.lang.String businesskey;
	/**关联数据id*/
	@Excel(name="关联数据id")
	private java.lang.String linkDataId;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=false,length=36)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主键
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public java.lang.String getCreateName(){
		return this.createName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateName(java.lang.String createName){
		this.createName = createName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true,length=20)
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人名称
	 */
	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	public java.lang.String getUpdateName(){
		return this.updateName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人名称
	 */
	public void setUpdateName(java.lang.String updateName){
		this.updateName = updateName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true,length=20)
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
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  显示名
	 */
	@Column(name ="TITLE",nullable=true,length=100)
	public java.lang.String getTitle(){
		return this.title;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  显示名
	 */
	public void setTitle(java.lang.String title){
		this.title = title;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  路径
	 */
	@Column(name ="PATH",nullable=true,length=200)
	public java.lang.String getPath(){
		return this.path;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  路径
	 */
	public void setPath(java.lang.String path){
		this.path = path;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  后缀名
	 */
	@Column(name ="EXTEND",nullable=true,length=32)
	public java.lang.String getExtend(){
		return this.extend;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  后缀名
	 */
	public void setExtend(java.lang.String extend){
		this.extend = extend;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  ContentType
	 */
	@Column(name ="CONTENT_TYPE",nullable=true,length=32)
	public java.lang.String getContentType(){
		return this.contentType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  ContentType
	 */
	public void setContentType(java.lang.String contentType){
		this.contentType = contentType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务关键字
	 */
	@Column(name ="BUSINESSKEY",nullable=true,length=32)
	public java.lang.String getBusinesskey(){
		return this.businesskey;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务关键字
	 */
	public void setBusinesskey(java.lang.String businesskey){
		this.businesskey = businesskey;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  关联数据id
	 */
	@Column(name ="LINK_DATA_ID",nullable=true,length=32)
	public java.lang.String getLinkDataId(){
		return this.linkDataId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  关联数据id
	 */
	public void setLinkDataId(java.lang.String linkDataId){
		this.linkDataId = linkDataId;
	}
}
