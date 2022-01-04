package cn.nearf.ggz.utils.image.generate;

import java.awt.Graphics2D;

public abstract class EmbededObject {

	private Integer xBegin;
	private Integer xEnd;
	
	private Integer yBegin;
	private Integer yEnd;
	

	public Integer getXBegin() {
		return xBegin;
	}

	public Integer getXEnd() {
		return xEnd;
	}

	public Integer getYBegin() {
		return yBegin;
	}

	public Integer getYEnd() {
		return yEnd;
	}

	public void withStartPoint(Integer xBegin, Integer yBegin) {
		this.xBegin = xBegin;
		this.xEnd = null;
		this.yBegin = yBegin;
		this.yEnd = null;
	}
	
	public void withEndPoint(Integer xEnd, Integer yEnd) {
		this.xBegin = null;
		this.xEnd = xEnd;
		this.yBegin = null;
		this.yEnd = yEnd;
	}
	
	public void withXCenter(Integer yBegin, Integer yEnd) {
		this.xBegin = null;
		this.xEnd = null;
		this.yBegin = yBegin;
		this.yEnd = yEnd;
	}
	
	public void withYCenter(Integer xBegin, Integer xEnd) {
		this.xBegin = xBegin;
		this.xEnd = xEnd;
		this.yBegin = null;
		this.yEnd = null;
	}
	
	public void withCenter() {
		this.xBegin = null;
		this.xEnd = null;
		this.yBegin = null;
		this.yEnd = null;
	}
	
	public abstract void paint(final Graphics2D canvas, final int canvasWidth, final int canvasHeight) throws Exception;
	
}
