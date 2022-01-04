package cn.nearf.dw.mon.entity;

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
 * @Description: 指标执行状态历史
 * @author onlineGenerator
 * @date 2018-07-12 23:06:31
 * @version V1.0   
 *
 */
@Entity
@Table(name = "dw_h_indicator_job_monitor_ctl", schema = "")
@SuppressWarnings("serial")
public class DwHIndicatorJobMonitorCtlEntity implements java.io.Serializable {
	/**ID*/
	private java.lang.Integer id;
	/**指标*/
	@Excel(name="指标")
	private java.lang.Integer indicatorId;
	/**任务类型*/
	@Excel(name="任务类型")
	private java.lang.String jobType;
	/**加载类型*/
	@Excel(name="加载类型")
	private java.lang.String jobIncFlg;
	/**任务状态*/
	@Excel(name="任务状态")
	private java.lang.String jobStatus;
	/**影响数据笔数*/
	@Excel(name="影响数据笔数")
	private java.lang.Integer processData;
	/**任务开始时间*/
	@Excel(name="任务开始时间",format = "yyyy-MM-dd")
	private java.util.Date jobStartTime;
	/**任务结束时间*/
	@Excel(name="任务结束时间",format = "yyyy-MM-dd")
	private java.util.Date jobEndTime;
	/**任务执行时间*/
	@Excel(name="任务执行时间")
	private java.lang.Integer jobRunTime;
	
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
	 *@return: java.lang.String  任务类型
	 */
	@Column(name ="JOB_TYPE",nullable=true,length=1)
	public java.lang.String getJobType(){
		return this.jobType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务类型
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  任务状态
	 */
	@Column(name ="JOB_STATUS",nullable=true,length=1)
	public java.lang.String getJobStatus(){
		return this.jobStatus;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务状态
	 */
	public void setJobStatus(java.lang.String jobStatus){
		this.jobStatus = jobStatus;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  影响数据笔数
	 */
	@Column(name ="PROCESS_DATA",nullable=true,length=10)
	public java.lang.Integer getProcessData(){
		return this.processData;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  影响数据笔数
	 */
	public void setProcessData(java.lang.Integer processData){
		this.processData = processData;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  任务开始时间
	 */
	@Column(name ="JOB_START_TIME",nullable=true)
	public java.util.Date getJobStartTime(){
		return this.jobStartTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  任务开始时间
	 */
	public void setJobStartTime(java.util.Date jobStartTime){
		this.jobStartTime = jobStartTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  任务结束时间
	 */
	@Column(name ="JOB_END_TIME",nullable=true)
	public java.util.Date getJobEndTime(){
		return this.jobEndTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  任务结束时间
	 */
	public void setJobEndTime(java.util.Date jobEndTime){
		this.jobEndTime = jobEndTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  任务执行时间
	 */
	@Column(name ="JOB_RUN_TIME",nullable=true,length=10)
	public java.lang.Integer getJobRunTime(){
		return this.jobRunTime;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  任务执行时间
	 */
	public void setJobRunTime(java.lang.Integer jobRunTime){
		this.jobRunTime = jobRunTime;
	}
}