package cn.nearf.ggz.utils.image.generate;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class EmbededImage extends EmbededObject {

	public final String path;
	
	private final BufferedImage image;
	
	public EmbededImage(String path) throws Exception {
		this.path = path;
		image = ImageIO.read(new File(this.path));
	}

	public int width() {
		return image.getWidth();
	}
	
	public int height() {
		return image.getHeight();
	}
	
	public void paint(final Graphics2D canvas, final int canvasWidth, final int canvasHeight) throws Exception {
		
		int x = 0;
		int y = 0;
		int width = image.getWidth();
		int height = image.getHeight();
		
		if (getXBegin() != null && getXEnd() != null) {
			x = getXBegin();
			width = getXEnd() - getXBegin();
		} else if (getXBegin() != null && getXEnd() == null) {
			x = getXBegin();
		} else if (getXBegin() == null && getXEnd() != null) {
			x = getXEnd() - width;
		} else {
			x = (canvasWidth - width) / 2;
		}
		
		if (getYBegin() != null && getYEnd() != null) {
			y = getYBegin();
			height = getYEnd() - getYBegin();
		} else if (getYBegin() != null && getYEnd() == null) {
			y = getYBegin();
		} else if (getYBegin() == null && getYEnd() != null) {
			y = getYEnd() - height;
		} else {
			y = (canvasHeight - height) / 2;
		}
		
		canvas.drawImage(image, x, y, width, height, null);
	}
}
