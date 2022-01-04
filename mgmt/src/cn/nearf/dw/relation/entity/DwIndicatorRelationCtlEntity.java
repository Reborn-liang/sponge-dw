package cn.nearf.dw.relation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jeecgframework.poi.excel.annotation.Excel;

/**   
 * @Title: Entity
 * @Description: 指标关系
 * @author onlineGenerator
 * @date 2018-07-24 15:15:29
 * @version V1.0   
 *
 */
@Entity
@Table(name = "dw_indicator_relation_ctl", schema = "")
@SuppressWarnings("serial")
public class DwIndicatorRelationCtlEntity implements java.io.Serializable {
	/**ID*/
	private java.lang.Integer id;
	/**指标*/
	@Excel(name="指标")
	private java.lang.Integer indicatorId;
	/**依赖指标*/
	@Excel(name="依赖指标")
	private java.lang.Integer parentIndicatorId;
	/**是否强关联*/
	@Excel(name="是否强关联")
	private java.lang.String couplingFlg;
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
	public java.lang.Integer getIndicatorId() {
		return indicatorId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  指标
	 */
	public void setIndicatorId(java.lang.Integer indicatorId) {
		this.indicatorId = indicatorId;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  依赖指标
	 */
	@Column(name ="PARENT_INDICATOR_ID",nullable=false,length=10)
	public java.lang.Integer getParentIndicatorId(){
		return this.parentIndicatorId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  依赖指标
	 */
	public void setParentIndicatorId(java.lang.Integer parentIndicatorId){
		this.parentIndicatorId = parentIndicatorId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  是否强关联
	 */
	@Column(name ="COUPLING_FLG",nullable=false,length=10)
	public String getCouplingFlg(){
		return this.couplingFlg;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  是否强关联
	 */
	public void setCouplingFlg(String couplingFlg){
		this.couplingFlg = couplingFlg;
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
