package org.jeecgframework.web.system.pojo.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jeecgframework.core.common.entity.IdEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 部门机构表
 * @author  张代浩
 */
@Entity
@Table(name = "t_s_depart", schema = "")
public class TSDepart extends IdEntity implements java.io.Serializable {
	private TSDepart TSPDepart;//上级部门
	private String departname;//部门名称
	private String description;//部门描述
    private String orgCode;//机构编码
    private String orgType;//机构类型
    private Integer storeId;//门店ID
    
    private Integer ignoreHealthCert;
    private Integer noEntry;
    
    private Integer staffing;
    
    
    /**排序*/
	@Excel(name="排序")
	private java.lang.Integer sort;
    
	private List<TSDepart> TSDeparts = new ArrayList<TSDepart>();//下属部门

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentdepartid")
	public TSDepart getTSPDepart() {
		return this.TSPDepart;
	}

	public void setTSPDepart(TSDepart TSPDepart) {
		this.TSPDepart = TSPDepart;
	}

	@Column(name = "departname", nullable = false, length = 100)
	public String getDepartname() {
		return this.departname;
	}

	public void setDepartname(String departname) {
		this.departname = departname;
	}

	@Column(name = "description", length = 500)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TSPDepart")
	public List<TSDepart> getTSDeparts() {
		return TSDeparts;
	}

	public void setTSDeparts(List<TSDepart> tSDeparts) {
		TSDeparts = tSDeparts;
	}

    @Column(name = "org_code", length = 64)
    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Column(name = "org_type", length = 1)
    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    @Column(name ="STORE_ID",nullable=false,length=20)
	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	
	
	@Column(name ="IGNORE_HEALTH_CERT",nullable=true,length=20)
	public Integer getIgnoreHealthCert() {
		return ignoreHealthCert;
	}

	public void setIgnoreHealthCert(Integer ignoreHealthCert) {
		this.ignoreHealthCert = ignoreHealthCert;
	}

	@Column(name ="NO_ENTRY",nullable=true,length=20)
	public Integer getNoEntry() {
		return noEntry;
	}

	public void setNoEntry(Integer noEntry) {
		this.noEntry = noEntry;
	}
	

	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  人员标配
	 */
	@Column(name ="STAFFING",nullable=true,length=11)
	public final java.lang.Integer getStaffing() {
		return staffing;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  人员标配
	 */
	public final void setStaffing(java.lang.Integer staffing) {
		this.staffing = staffing;
	}
	

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  排序
	 */
	@Column(name ="SORT",nullable=true,length=20)
	public java.lang.Integer getSort(){
		return this.sort;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  排序
	 */
	public void setSort(java.lang.Integer sort){
		this.sort = sort;
	}
    
}