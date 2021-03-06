package org.jeecgframework.web.system.pojo.base;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jeecgframework.core.common.entity.IdEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统用户父类表
 * @author  张代浩
 */
@Entity
@Table(name = "t_s_base_user", schema = "")
@Inheritance(strategy = InheritanceType.JOINED)
public class TSBaseUser extends IdEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String userName;// 用户名
	private String realName;// 真实姓名
	private String browser;// 用户使用浏览器类型
	private String userKey;// 用户验证唯一标示
	private String password;//用户密码
	private Short activitiSync;//是否同步工作流引擎
	private Short status;// 状态1：在线,2：离线,0：禁用
	private byte[] signature;// 签名文件
	private int errorNum;// 密码输入错误的次数

	@Column(name = "error_num", length = 1)
	public int getErrorNum() { return errorNum; }

	public void setErrorNum(int errorNum) { this.errorNum = errorNum; }

	/**是否出现在移动端*/
	@Excel(name="是否出现在移动端")
	private java.lang.Integer isShowMob;
	
    //	private TSDepart TSDepart = new TSDepart();// 部门
    private List<TSUserOrg> userOrgList = new ArrayList<TSUserOrg>();
	private TSDepart currentDepart = new TSDepart();// 当前部门

	@Column(name = "signature",length=3000)
	public byte[] getSignature() {
		return signature;
	}
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	@Column(name = "browser", length = 20)
	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	@Column(name = "userkey", length = 200)
	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	@Column(name = "status")
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}
	public Short getActivitiSync() {
		return activitiSync;
	}
	@Column(name = "activitisync")
	public void setActivitiSync(Short activitiSync) {
		this.activitiSync = activitiSync;
	}
	
	
	@Column(name = "password", length = 100)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
//	@JsonIgnore    //getList查询转换为列表时处理json转换异常
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "departid")
//	public TSDepart getTSDepart() {
//		return this.TSDepart;
//	}
//
//	public void setTSDepart(TSDepart TSDepart) {
//		this.TSDepart = TSDepart;
//	}
	@Column(name = "username", nullable = false, length = 10)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name = "realname", length = 50)
	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
    @Transient
    public TSDepart getCurrentDepart() {
        return currentDepart;
    }

    public void setCurrentDepart(TSDepart currentDepart) {
        this.currentDepart = currentDepart;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "tsUser")
    public List<TSUserOrg> getUserOrgList() {
        return userOrgList;
    }

    public void setUserOrgList(List<TSUserOrg> userOrgList) {
        this.userOrgList = userOrgList;
    }
    
    /**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  是否出现在移动端
	 */
	@Column(name ="IS_SHOW_MOB",nullable=true,length=20)
	public java.lang.Integer getIsShowMob(){
		return this.isShowMob;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  是否出现在移动端
	 */
	public void setIsShowMob(java.lang.Integer isShowMob){
		this.isShowMob = isShowMob;
	}
}