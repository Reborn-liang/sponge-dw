package cn.nearf.ggz.utils.image.generate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;


public class EmbededText extends EmbededObject {
	public final String text;
	public final Color color;
	public final Font font;
	
	public EmbededText(String text, Color color, Font font) {
		this.text = text;
		this.color = color;
		this.font = font;
	}
	
	public void paint(final Graphics2D canvas, final int canvasWidth, final int canvasHeight) throws Exception {
		
		canvas.setColor(color); // 根据图片的背景设置水印颜色
		canvas.setFont(font); // 设置字体
		
		FontRenderContext frc = new FontRenderContext(null, true, true);
		Rectangle2D rect = font.getStringBounds(text, frc);
		
		int width = (int) Math.round(rect.getWidth());
		int height = (int) Math.round(rect.getHeight());
		int rX = (int) Math.round(rect.getX());
		int rY = (int) Math.round(rect.getY());

		
		int x = 0;
		int y = 0;
		
		if (getXBegin() != null && getXEnd() != null) {
			x = getXBegin() + (getXEnd() - getXBegin() - width) / 2 - rX;
		} else if (getXBegin() != null && getXEnd() == null) {
			x = getXBegin() - rX;
		} else if (getXBegin() == null && getXEnd() != null) {
			x = getXEnd() - width;
		} else {
			x = (canvasWidth - width) / 2 - rX;
		}
		
		if (getYBegin() != null && getYEnd() != null) {
			y = getYBegin() + (getYEnd() - getYBegin() - height) / 2 - rY;
		} else if (getYBegin() != null && getYEnd() == null) {
			y = getYBegin() - rY;
		} else if (getYBegin() == null && getYEnd() != null) {
			y = getYEnd() - height;
		} else {
			y = (canvasHeight - height) / 2 - rY;
		}


		canvas.drawString(text, x, y); // 画出水印
	}

}
