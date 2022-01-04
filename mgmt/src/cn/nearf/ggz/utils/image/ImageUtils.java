package cn.nearf.ggz.utils.image;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageUtils {

	public static String getImageType(Object imgPath) {
		try {
			String path = imgPath.toString();
			String typeSplit = ".";
			String type = path.substring(path.lastIndexOf(typeSplit) + typeSplit.length());
			return type;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] getJpgData(String imgUrl) {
		try {
			BufferedImage sourceImg = ImageIO.read(new URL(imgUrl));
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(sourceImg, "JPG", os);
			return os.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ImageSize getFileSize(String imgFile) {
		ImageSize size = null;
		try {
			BufferedImage sourceImg = ImageIO.read(new FileInputStream(imgFile));
			size = getSize(sourceImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}
	
	public static ImageSize getUrlSize(String imgUrl) {
		ImageSize size = null;
		try {
			BufferedImage sourceImg = ImageIO.read(new URL(imgUrl));
			size = getSize(sourceImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}
	
	public static ImageSize getSize(BufferedImage bufferedImage) {
		ImageSize size = null;
		try {
			size = ImageSize.newInstance(bufferedImage.getWidth(), bufferedImage.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	public static class ImageSize {
		
		public static ImageSize newInstance(int width, int height) {
			ImageSize size = new ImageSize();
			size.width = width;
			size.height = height;
			return size;
		}
		
		public int width;

		public int height;
		
		public String fileUrl;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + height;
			result = prime * result + width;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ImageSize other = (ImageSize) obj;
			if (height != other.height)
				return false;
			if (width != other.width)
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return width + "*" + height;
		}
		
	}
	
	
	public static void scaleAndClipImage(final String srcFileFullPath, final String targetFilePath, final String targetFileName, final String targetFileSuffix, final int targetWidth, final int targetHeight) throws Exception {
		String targetFileFullPath = targetFilePath + "/" + targetFileName + "." + targetFileSuffix;
		scaleAndClipImage(srcFileFullPath, targetFileFullPath, targetWidth, targetHeight);
	}
	
	public static void scaleAndClipImage(final String srcFileFullPath, final String targetFilePath, final String targetFileName, final int targetWidth, final int targetHeight) throws Exception {
		String fileWithExtend = targetFileName;
		try {
			int dotIndex = targetFileName.lastIndexOf(".");
			if (dotIndex < 0) {
				fileWithExtend = targetFileName + "." + srcFileFullPath.substring(srcFileFullPath.lastIndexOf(".") + 1);
			}
		} catch (Exception e) {
		}
		String targetFileFullPath = targetFilePath + "/" + fileWithExtend;
		scaleAndClipImage(srcFileFullPath, targetFileFullPath, targetWidth, targetHeight);
	}
	
	public static void scaleAndClipImage(final String srcFileFullPath, final String targetFileFullPath, final int targetWidth, final int targetHeight) throws Exception {
		// fileExtNmae是图片的格式 gif JPG 或png
		String targetExtend = "png";
		try {
			targetExtend = targetFileFullPath.substring(targetFileFullPath.lastIndexOf(".") + 1);
		} catch (Exception e) {
		}
		
		File srcFile = new File(srcFileFullPath);
		File targetFile = new File(targetFileFullPath);
		BufferedImage buffer = ImageIO.read(srcFile);
		/*
		 * 核心算法，计算图片的压缩比
		 */
		int w = buffer.getWidth();
		int h = buffer.getHeight();
		double ratiox = 1.0;
		double ratioy = 1.0;

		ratiox = w * ratiox / targetWidth;
		ratioy = h * ratioy / targetHeight;

		if (ratiox >= 1) {
			if (ratioy < 1) {
				ratiox = targetHeight * 1.0 / h;
			} else {
				if (ratiox > ratioy) {
					ratiox = targetHeight * 1.0 / h;
				} else {
					ratiox = targetWidth * 1.0 / w;
				}
			}
		} else {
			if (ratioy < 1) {
				if (ratiox > ratioy) {
					ratiox = targetHeight * 1.0 / h;
				} else {
					ratiox = targetWidth * 1.0 / w;
				}
			} else {
				ratiox = targetWidth * 1.0 / w;
			}
		}
		/*
		 * 对于图片的放大或缩小倍数计算完成，ratiox大于1，则表示放大，否则表示缩小
		 */
		AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratiox, ratiox), null);
		buffer = op.filter(buffer, null);
		// 从放大的图像中心截图
		buffer = buffer.getSubimage((buffer.getWidth() - targetWidth) / 2, (buffer.getHeight() - targetHeight) / 2, targetWidth, targetHeight);
		ImageIO.write(buffer, targetExtend, targetFile);
	}
	
	public static void main(String[] args) {
		System.out.println(getFileSize("/Volumes/Storage/180x180.png"));
	}
}
