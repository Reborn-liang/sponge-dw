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
 * @Description: dw_indicator_std_ctl
 * @author onlineGenerator
 * @date 2018-07-12 19:57:24
 * @version V1.0   
 *
 */
@Entity
@Table(name = "dw_indicator_std_ctl", schema = "")
@SuppressWarnings("serial")
public class DwIndicatorStdCtlEntity implements java.io.Serializable {
	/**ID*/
	private java.lang.Integer id;
	/**原表名*/
	@Excel(name="原表名")
	private java.lang.String originalTable;
	/**原字段名*/
	@Excel(name="原字段名")
	private java.lang.String originalColumn;
	/**标准化类型*/
	@Excel(name="标准化类型")
	private java.lang.String stdType;
	/**标准化名*/
	@Excel(name="标准化名")
	private java.lang.String stdName;
	/**标准化中文名*/
	@Excel(name="标准化中文名")
	private java.lang.String chineseName;
	/**值域*/
	@Excel(name="值域")
	private java.lang.String valueRangeCode;
	/**创建时间*/
	@Excel(name="创建时间",format = "yyyy-MM-dd")
	private java.util.Date createDate;
	/**更新时间*/
	@Excel(name="更新时间",format = "yyyy-MM-dd")
	private java.util.Date updateDate;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="ID",nullable=false,length=20)
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原表名
	 */
	@Column(name ="ORIGINAL_TABLE",nullable=false,length=40)
	public java.lang.String getOriginalTable(){
		return this.originalTable;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原表名
	 */
	public void setOriginalTable(java.lang.String originalTable){
		this.originalTable = originalTable;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原字段名
	 */
	@Column(name ="ORIGINAL_COLUMN",nullable=false,length=40)
	public java.lang.String getOriginalColumn(){
		return this.originalColumn;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原字段名
	 */
	public void setOriginalColumn(java.lang.String originalColumn){
		this.originalColumn = originalColumn;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  标准化类型
	 */
	@Column(name ="STD_TYPE",nullable=false,length=1)
	public java.lang.String getStdType(){
		return this.stdType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  标准化类型
	 */
	public void setStdType(java.lang.String stdType){
		this.stdType = stdType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  标准化名
	 */
	@Column(name ="STD_NAME",nullable=false,length=40)
	public java.lang.String getStdName(){
		return this.stdName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  标准化名
	 */
	public void setStdName(java.lang.String stdName){
		this.stdName = stdName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  标准化中文名
	 */
	@Column(name ="CHINESE_NAME",nullable=true,length=60)
	public java.lang.String getChineseName(){
		return this.chineseName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  标准化中文名
	 */
	public void setChineseName(java.lang.String chineseName){
		this.chineseName = chineseName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  值域
	 */
	@Column(name ="VALUE_RANGE_CODE",nullable=true,length=20)
	public java.lang.String getValueRangeCode(){
		return this.valueRangeCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  值域
	 */
	public void setValueRangeCode(java.lang.String valueRangeCode){
		this.valueRangeCode = valueRangeCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_DATE",nullable=true)
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
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新时间
	 */
	@Column(name ="UPDATE_DATE",nullable=true)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新时间
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
}
