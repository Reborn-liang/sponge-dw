package cn.nearf.dw.common;

public class TypeVO implements java.io.Serializable {
	private java.lang.String id;
	private java.lang.String name;
	private java.lang.String t;
	private java.lang.Integer l;

	public TypeVO() {
		
	}
	
	public TypeVO(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public TypeVO(String id, String name, String t, Integer l) {
		super();
		this.id = id;
		this.name = name;
		this.t = t;
		this.l = l;
	}

	public java.lang.String getT() {
		return t;
	}

	public java.lang.Integer getL() {
		return l;
	}

	public void setT(java.lang.String t) {
		this.t = t;
	}

	public void setL(java.lang.Integer l) {
		this.l = l;
	}

	public java.lang.String getId() {
		return id;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "TypeVO [id=" + id + ", name=" + name + ", t=" + t + ", l=" + l + "]";
	}

}