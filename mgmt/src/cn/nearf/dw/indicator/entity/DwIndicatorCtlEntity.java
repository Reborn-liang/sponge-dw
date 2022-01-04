package cn.nearf.dw.indicator.entity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.lang.String;
import java.lang.Double;
import java.lang.Long;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**   
 * @Title: Entity
 * @Description: dw_indicator_ctl
 * @author onlineGenerator
 * @date 2018-07-16 18:55:11
 * @version V1.0   
 *
 */
@Entity
@Table(name = "dw_indicator_ctl", schema = "")
@SuppressWarnings("serial")
public class DwIndicatorCtlEntity implements java.io.Serializable {
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
    /**来源类型*/
    @Excel(name="数据源来源")
    private java.lang.String sourceType;
    @Excel(name="数据源加载类型")
    private java.lang.String loadType;
    @Excel(name="数据源目录")
    private java.lang.String sourcePath;
    @Excel(name="分隔符")
	private java.lang.String delimiter;
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
	/**是否需要除重处理*/
	@Excel(name="是否需要除重处理")
	private java.lang.String duplicationCheckFlg;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务分组
	 */
	
	@Column(name ="BIZ_GROUP",nullable=false,length=20)
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
	
	@Column(name ="CODE",nullable=false,length=40)
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
	
	@Column(name ="TARGET_TABLE",nullable=false,length=40)
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
	
	@Column(name ="TYPE",nullable=true,length=4)
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
	
	@Column(name ="SOURCE_FROM",nullable=true,length=4)
	public java.lang.String getSourceType() {
		return sourceType;
	}

	public void setSourceType(java.lang.String sourceType) {
		this.sourceType = sourceType;
	}
	
	@Column(name ="SOURCE_LOAD_TYPE",nullable=true,length=4)
	public java.lang.String getLoadType() {
		return loadType;
	}

	public void setLoadType(java.lang.String loadType) {
		this.loadType = loadType;
	}

	@Column(name ="SOURCE_FILE_PATH",nullable=true,length=4)
	public java.lang.String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(java.lang.String sourcePath) {
		this.sourcePath = sourcePath;
	}

	@Column(name ="SOURCE_FILE_DELIMITER",nullable=true,length=4)
	public java.lang.String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(java.lang.String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  状态
	 */
	
	@Column(name ="STATUS",nullable=false,length=1)
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
	
	@Column(name ="BIZ_KEY",nullable=false,length=200)
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
	
	@Column(name ="MODEL_CODE",nullable=true,length=40)
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
	
	@Column(name ="HISTORY_KEEP_DAYS",nullable=true,length=10)
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
	
	@Column(name ="JOB_TYPE",nullable=true,length=1)
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
	
	@Column(name ="JOB_INC_FLG",nullable=true,length=1)
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
	
	@Column(name ="JOB_START_TIME",nullable=true,length=10)
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
	
	@Column(name ="SFTP_FLG",nullable=false,length=4)
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
	 *@return: java.lang.String  是否需要除重处理
	 */
	@Column(name ="DUPLICATION_CHECK_FLG",nullable=true,length=1)
	public java.lang.String getDuplicationCheckFlg(){
		return this.duplicationCheckFlg;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否需要除重处理
	 */
	public void setDuplicationCheckFlg(java.lang.String duplicationCheckFlg){
		this.duplicationCheckFlg = duplicationCheckFlg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  名称
	 */
	
	@Column(name ="NAME",nullable=true,length=100)
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
	
	@Column(name ="MEMO",nullable=true,length=2000)
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
	
	@Column(name ="SQLS",nullable=true,length=1000)
	public java.lang.String getSqls(){
		return this.sqls;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  SQL
	 */
	public void setSqls(java.lang.String sqls){
		this.sqls = sqls;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	
	@Column(name ="CREATE_DATE",nullable=false)
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
	
	@Column(name ="UPDATE_DATE",nullable=false)
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
