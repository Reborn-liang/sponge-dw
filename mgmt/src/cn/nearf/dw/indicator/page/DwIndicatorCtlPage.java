
package cn.nearf.dw.indicator.page;
import cn.nearf.dw.indicator.entity.DwIndicatorCtlEntity;
import cn.nearf.dw.indicator.entity.DwIndicatorSftpCtlEntity;
import cn.nearf.dw.indicator.entity.DwIndicatorColumnCtlEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

/**   
 * @Title: Entity
 * @Description: dw_indicator_ctl
 * @author onlineGenerator
 * @date 2018-07-16 18:55:11
 * @version V1.0   
 *
 */
public class DwIndicatorCtlPage implements java.io.Serializable {
	/**ID*/
	private java.lang.Integer id;
	/**业务分组*/
    @Excel(name="业务分组")
	private java.lang.String bizGroup;
	/**编码*/
    @Excel(name="编码")
	private java.lang.String code;
	/**指标表名*/
    @Excel(name="指标表名")
	private java.lang.String targetTable;
	/**类型*/
    @Excel(name="类型")
	private java.lang.String type;
	/**状态*/
    @Excel(name="状态")
	private java.lang.String status;
	/**业务主键*/
    @Excel(name="业务主键")
	private java.lang.String bizKey;
	/**模型编码*/
    @Excel(name="模型编码")
	private java.lang.String modelCode;
	/**数据保留天数*/
    @Excel(name="数据保留天数")
	private java.lang.Integer historyKeepDays;
	/**执行周期类型*/
    @Excel(name="执行周期类型")
	private java.lang.String jobType;
	/**加载类型*/
    @Excel(name="加载类型")
	private java.lang.String jobIncFlg;
	/**任务开始时间点*/
    @Excel(name="任务开始时间点")
	private java.lang.Integer jobStartTime;
	/**是否需要下发*/
    @Excel(name="是否需要下发")
	private java.lang.String sftpFlg;
	/**名称*/
    @Excel(name="名称")
	private java.lang.String name;
	/**描述*/
    @Excel(name="描述")
	private java.lang.String memo;
	/**SQL*/
    @Excel(name="SQL")
	private java.lang.String sqls;
	/**创建时间*/
	private java.util.Date createDate;
	/**更新时间*/
	private java.util.Date updateDate;
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  ID
	 */
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
	 *@return: java.lang.String  业务分组
	 */
	public java.lang.String getBizGroup(){
		return this.bizGroup;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务分组
	 */
	public void setBizGroup(java.lang.String bizGroup){
		this.bizGroup = bizGroup;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  编码
	 */
	public java.lang.String getCode(){
		return this.code;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  编码
	 */
	public void setCode(java.lang.String code){
		this.code = code;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  指标表名
	 */
	public java.lang.String getTargetTable(){
		return this.targetTable;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  指标表名
	 */
	public void setTargetTable(java.lang.String targetTable){
		this.targetTable = targetTable;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  类型
	 */
	public java.lang.String getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  类型
	 */
	public void setType(java.lang.String type){
		this.type = type;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  状态
	 */
	public java.lang.String getStatus(){
		return this.status;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  状态
	 */
	public void setStatus(java.lang.String status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务主键
	 */
	public java.lang.String getBizKey(){
		return this.bizKey;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务主键
	 */
	public void setBizKey(java.lang.String bizKey){
		this.bizKey = bizKey;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  模型编码
	 */
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  数据保留天数
	 */
	public java.lang.Integer getHistoryKeepDays(){
		return this.historyKeepDays;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  数据保留天数
	 */
	public void setHistoryKeepDays(java.lang.Integer historyKeepDays){
		this.historyKeepDays = historyKeepDays;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  执行周期类型
	 */
	public java.lang.String getJobType(){
		return this.jobType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  执行周期类型
	 */
	public void setJobType(java.lang.String jobType){
		this.jobType = jobType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  加载类型
	 */
	public java.lang.String getJobIncFlg(){
		return this.jobIncFlg;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  加载类型
	 */
	public void setJobIncFlg(java.lang.String jobIncFlg){
		this.jobIncFlg = jobIncFlg;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  任务开始时间点
	 */
	public java.lang.Integer getJobStartTime(){
		return this.jobStartTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  任务开始时间点
	 */
	public void setJobStartTime(java.lang.Integer jobStartTime){
		this.jobStartTime = jobStartTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否需要下发
	 */
	public java.lang.String getSftpFlg(){
		return this.sftpFlg;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否需要下发
	 */
	public void setSftpFlg(java.lang.String sftpFlg){
		this.sftpFlg = sftpFlg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  名称
	 */
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  描述
	 */
	public java.lang.String getMemo(){
		return this.memo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  描述
	 */
	public void setMemo(java.lang.String memo){
		this.memo = memo;
	}
	/**
	 *方法: 取得javax.xml.soap.Text
	 *@return: javax.xml.soap.Text  SQL
	 */
	public java.lang.String getSqls(){
		return this.sqls;
	}

	/**
	 *方法: 设置javax.xml.soap.Text
	 *@param: javax.xml.soap.Text  SQL
	 */
	public void setSqls(java.lang.String sqls){
		this.sqls = sqls;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
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

	/**保存-指标明细*/
    @ExcelCollection(name="指标明细")
	private List<DwIndicatorColumnCtlEntity> dwIndicatorColumnCtlList = new ArrayList<DwIndicatorColumnCtlEntity>();
		public List<DwIndicatorColumnCtlEntity> getDwIndicatorColumnCtlList() {
		return dwIndicatorColumnCtlList;
		}
		public void setDwIndicatorColumnCtlList(List<DwIndicatorColumnCtlEntity> dwIndicatorColumnCtlList) {
		this.dwIndicatorColumnCtlList = dwIndicatorColumnCtlList;
		}
	/**保存-dw_indicator_ctl*/
    @ExcelCollection(name="dw_indicator_ctl")
	private List<DwIndicatorSftpCtlEntity> dwIndicatorSftpCtlList = new ArrayList<DwIndicatorSftpCtlEntity>();
		public List<DwIndicatorSftpCtlEntity> getDwIndicatorSftpCtlList() {
		return dwIndicatorSftpCtlList;
		}
		public void setDwIndicatorSftpCtlList(List<DwIndicatorSftpCtlEntity> dwIndicatorSftpCtlList) {
		this.dwIndicatorSftpCtlList = dwIndicatorSftpCtlList;
		}
}
