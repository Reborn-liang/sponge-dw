package cn.nearf.dw.indicatormodel.entity;

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
 * @Description: dw_indicator_model_ctl
 * @author onlineGenerator
 * @date 2018-07-16 19:57:44
 * @version V1.0   
 *
 */
@Entity
@Table(name = "dw_indicator_model_ctl", schema = "")
@SuppressWarnings("serial")
public class DwIndicatorModelCtlEntity implements java.io.Serializable {
	/**ID*/
	private java.lang.Integer id;
	/**模型编码*/
	@Excel(name="模型编码")
	private java.lang.String modelCode;
	/**模型名称*/
	@Excel(name="模型名称")
	private java.lang.String name;
	/**模型说明*/
	@Excel(name="模型说明")
	private java.lang.String memo;
	/**事实表*/
	@Excel(name="事实表Schema")
	private java.lang.String factSchema;
	/**事实表*/
	@Excel(name="事实表")
	private java.lang.String factTable;
	/**维表*/
	@Excel(name="维表Schema")
	private java.lang.String dimSchema;
	/**维表*/
	@Excel(name="维表")
	private java.lang.String dimTable;
	/**维表是否生效*/
	@Excel(name="是否生效")
	private java.lang.Integer dimActiveFlg;
	/**关联类型*/
	@Excel(name="关联类型")
	private java.lang.String joinType;
	/**关联条件*/
	@Excel(name="关联条件")
	private java.lang.String joinCondition;
	/**创建日期*/
	@Excel(name="创建日期",format = "yyyy-MM-dd")
	private java.util.Date createDate;
	/**更新日期*/
	@Excel(name="更新日期",format = "yyyy-MM-dd")
	private java.util.Date updateDate;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="ID",nullable=false,length=10)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  ID
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	
	@Column(name ="FACT_SCHEMA",nullable=false,length=40)
	public java.lang.String getFactSchema() {
		return factSchema;
	}

	public void setFactSchema(java.lang.String factSchema) {
		this.factSchema = factSchema;
	}

	@Column(name ="DIM_SCHEMA",nullable=false,length=40)
	public java.lang.String getDimSchema() {
		return dimSchema;
	}

	public void setDimSchema(java.lang.String dimSchema) {
		this.dimSchema = dimSchema;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  模型编码
	 */
	@Column(name ="MODEL_CODE",nullable=false,length=40)
	public java.lang.String getModelCode(){
		return this.modelCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  模型编码
	 */
	public void setModelCode(java.lang.String modelCode){
		this.modelCode = modelCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  模型名称
	 */
	@Column(name ="NAME",nullable=true,length=200)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  模型名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  模型说明
	 */
	@Column(name ="MEMO",nullable=true,length=2000)
	public java.lang.String getMemo(){
		return this.memo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  模型说明
	 */
	public void setMemo(java.lang.String memo){
		this.memo = memo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  事实表
	 */
	@Column(name ="FACT_TABLE",nullable=false,length=40)
	public java.lang.String getFactTable(){
		return this.factTable;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  事实表
	 */
	public void setFactTable(java.lang.String factTable){
		this.factTable = factTable;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  维表
	 */
	@Column(name ="DIM_TABLE",nullable=false,length=40)
	public java.lang.String getDimTable(){
		return this.dimTable;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  维表
	 */
	public void setDimTable(java.lang.String dimTable){
		this.dimTable = dimTable;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  维表是否生效
	 */
	@Column(name ="DIM_ACTIVE_FLG",nullable=false,length=10)
	public java.lang.Integer getDimActiveFlg(){
		return this.dimActiveFlg;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  维表是否生效
	 */
	public void setDimActiveFlg(java.lang.Integer dimActiveFlg){
		this.dimActiveFlg = dimActiveFlg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  关联类型
	 */
	@Column(name ="JOIN_TYPE",nullable=false,length=10)
	public java.lang.String getJoinType(){
		return this.joinType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  关联类型
	 */
	public void setJoinType(java.lang.String joinType){
		this.joinType = joinType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  关联条件
	 */
	@Column(name ="JOIN_CONDITION",nullable=false,length=1000)
	public java.lang.String getJoinCondition(){
		return this.joinCondition;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  关联条件
	 */
	public void setJoinCondition(java.lang.String joinCondition){
		this.joinCondition = joinCondition;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=false)
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
	@Column(name ="UPDATE_DATE",nullable=false)
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
