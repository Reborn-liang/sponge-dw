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
 * @Description: dw_indicator_column_ctl
 * @author onlineGenerator
 * @date 2018-07-16 18:55:10
 * @version V1.0   
 *
 */
@Entity
@Table(name = "dw_indicator_column_ctl", schema = "")
@SuppressWarnings("serial")
public class DwIndicatorColumnCtlEntity implements java.io.Serializable {
	/**ID*/
	private java.lang.Integer id;
	/**指标*/
	@Excel(name="指标")
	private java.lang.Integer indicatorId;
	/**指标字段*/
	@Excel(name="指标字段")
	private java.lang.String indicatorColumn;
	/**字段类型*/
	@Excel(name="字段类型")
	private java.lang.String type;
	/**源schema*/
	@Excel(name="源schema")
	private java.lang.String fromSchema;
	/**源表*/
	@Excel(name="源表")
	private java.lang.String fromTable;
	/**源字段*/
	@Excel(name="源字段")
	private java.lang.String fromColumn;
	/**公式*/
	@Excel(name="公式")
	private java.lang.String formula;
	/**过滤条件*/
	@Excel(name="过滤条件")
	private java.lang.String filters;
	/**字段类型*/
	@Excel(name="字段类型")
	private java.lang.String columnType;
	/**字段长度*/
	@Excel(name="字段长度")
	private java.lang.Integer columnLength;
	/**导出字段名*/
	@Excel(name="导出字段名")
	private java.lang.String exportName;
	/**导出排序位置*/
	@Excel(name="导出排序位置")
	private java.lang.Integer exportOrder;
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
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  指标
	 */
	@Column(name ="INDICATOR_ID",nullable=false,length=10)
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
	 *@return: java.lang.String  指标字段
	 */
	@Column(name ="INDICATOR_COLUMN",nullable=false,length=40)
	public java.lang.String getIndicatorColumn(){
		return this.indicatorColumn;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  指标字段
	 */
	public void setIndicatorColumn(java.lang.String indicatorColumn){
		this.indicatorColumn = indicatorColumn;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  字段类型
	 */
	@Column(name ="TYPE",nullable=true,length=4)
	public java.lang.String getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  字段类型
	 */
	public void setType(java.lang.String type){
		this.type = type;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  源schema
	 */
	@Column(name ="FROM_SCHEMA",nullable=true,length=20)
	public java.lang.String getFromSchema(){
		return this.fromSchema;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  源schema
	 */
	public void setFromSchema(java.lang.String fromSchema){
		this.fromSchema = fromSchema;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  源表
	 */
	@Column(name ="FROM_TABLE",nullable=true,length=40)
	public java.lang.String getFromTable(){
		return this.fromTable;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  源表
	 */
	public void setFromTable(java.lang.String fromTable){
		this.fromTable = fromTable;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  源字段
	 */
	@Column(name ="FROM_COLUMN",nullable=false,length=40)
	public java.lang.String getFromColumn(){
		return this.fromColumn;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  源字段
	 */
	public void setFromColumn(java.lang.String fromColumn){
		this.fromColumn = fromColumn;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  公式
	 */
	@Column(name ="FORMULA",nullable=true,length=255)
	public java.lang.String getFormula(){
		return this.formula;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  公式
	 */
	public void setFormula(java.lang.String formula){
		this.formula = formula;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  过滤条件
	 */
	@Column(name ="FILTERS",nullable=true,length=2000)
	public java.lang.String getFilters(){
		return this.filters;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  过滤条件
	 */
	public void setFilters(java.lang.String filters){
		this.filters = filters;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  字段类型
	 */
	@Column(name ="COLUMN_TYPE",nullable=true,length=255)
	public java.lang.String getColumnType(){
		return this.columnType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  字段类型
	 */
	public void setColumnType(java.lang.String columnType){
		this.columnType = columnType;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  字段长度
	 */
	@Column(name ="COLUMN_LENGTH",nullable=true,length=11)
	public java.lang.Integer getColumnLength(){
		return this.columnLength;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  字段长度
	 */
	public void setColumnLength(java.lang.Integer columnLength){
		this.columnLength = columnLength;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  导出字段名
	 */
	@Column(name ="EXPORT_NAME",nullable=true,length=255)
	public java.lang.String getExportName(){
		return this.exportName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  导出字段名
	 */
	public void setExportName(java.lang.String exportName){
		this.exportName = exportName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  导出排序位置
	 */
	@Column(name ="EXPORT_ORDER",nullable=true,length=11)
	public java.lang.Integer getExportOrder(){
		return this.exportOrder;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  导出排序位置
	 */
	public void setExportOrder(java.lang.Integer exportOrder){
		this.exportOrder = exportOrder;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true)
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
	@Column(name ="UPDATE_DATE",nullable=true)
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
