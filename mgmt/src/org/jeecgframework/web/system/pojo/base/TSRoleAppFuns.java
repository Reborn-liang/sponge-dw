package org.jeecgframework.web.system.pojo.base;

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
 * @Description: 角色app权限
 * @author onlineGenerator
 * @date 2018-05-04 15:29:28
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_s_role_app_funs", schema = "")
@SuppressWarnings("serial")
public class TSRoleAppFuns implements java.io.Serializable {
	/**主键*/
	private java.lang.Integer id;
	/**角色id*/
	@Excel(name="角色id")
	private java.lang.String roleId;
	/**app菜单*/
	@Excel(name="app菜单")
	private java.lang.String appFunIds;
	/**pad菜单*/
	@Excel(name="pad菜单")
	private java.lang.String pdaFunIds;
	/**创建日期*/
	private java.util.Date createDate;
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
	 *@return: java.lang.String  角色id
	 */
	@Column(name ="ROLE_ID",nullable=true,length=32)
	public java.lang.String getRoleId(){
		return this.roleId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  角色id
	 */
	public void setRoleId(java.lang.String roleId){
		this.roleId = roleId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  app菜单
	 */
	@Column(name ="APP_FUN_IDS",nullable=true,length=200)
	public java.lang.String getAppFunIds(){
		return this.appFunIds;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  app菜单
	 */
	public void setAppFunIds(java.lang.String appFunIds){
		this.appFunIds = appFunIds;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  pad菜单
	 */
	@Column(name ="PDA_FUN_IDS",nullable=true,length=200)
	public java.lang.String getPdaFunIds(){
		return this.pdaFunIds;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  pad菜单
	 */
	public void setPdaFunIds(java.lang.String pdaFunIds){
		this.pdaFunIds = pdaFunIds;
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
}
