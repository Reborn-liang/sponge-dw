package cn.nearf.ggz.utils.QR;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import cn.nearf.ggz.utils.image.ImageType;

public class QrUtils {

	public static String generateQrCode(final int width, final int height, final String content,
			Color fillColor, Color backgrondColor,
			final ImageType outputType, final String outputPath, final String outputNameWithoutSuffix) throws Exception {
		return generateQrCode(width, height, null, content, fillColor, backgrondColor, outputType, outputPath, outputNameWithoutSuffix);
	}
	
	public static String generateQrCode(final int width, final int height, final Integer margin,
			final String content, Color fillColor, Color backgrondColor,
			final ImageType outputType, final String outputPath, final String outputNameWithoutSuffix) throws Exception {
		
		File outPutImage = new File(String.format("%s/%s.%s", outputPath, outputNameWithoutSuffix, outputType.name()));
		// 如果图片不存在创建图片
		if (!outPutImage.exists()) {
			outPutImage.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(outPutImage);
		generateQrCode(width, height, margin, content, fillColor, backgrondColor, outputType, fos);
		return outPutImage.getAbsolutePath();
	}
	
	public static void generateQrCode(final int width, final int height,
			final Integer margin, final String content,
			Color fillColor, Color backgrondColor,
			final ImageType outputType, final OutputStream output) throws Exception {
		Map<EncodeHintType, Object> his = new HashMap<EncodeHintType, Object>();
		// 设置编码字符集
		his.put(EncodeHintType.CHARACTER_SET, "utf-8");
		if (margin != null) {
			his.put(EncodeHintType.MARGIN, margin);
		}

		BitMatrix encode = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE , width, height, his);

		// 2、获取二维码宽高
		int codeWidth = encode.getWidth();
		int codeHeight = encode.getHeight();

		// 3、将二维码放入缓冲流
		BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < codeWidth; i++) {
			for (int j = 0; j < codeHeight; j++) {
				// 4、循环将二维码内容定入图片
				image.setRGB(i, j, encode.get(i, j) ? fillColor.getRGB() : backgrondColor.getRGB());
			}
		}

		// 5、将二维码写入图片
		ImageIO.write(image, outputType.name(), output);
	}
	
	
	public static void generateBarCode(final int width, final int height,
			final Integer margin, final String content,
			Color fillColor, Color backgrondColor,
			final ImageType outputType, final OutputStream output) throws Exception {
		Map<EncodeHintType, Object> his = new HashMap<EncodeHintType, Object>();
		// 设置编码字符集
		his.put(EncodeHintType.CHARACTER_SET, "utf-8");
		if (margin != null) {
			his.put(EncodeHintType.MARGIN, margin);
		}

		BitMatrix encode = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128 , width, height, his);

		// 2、获取二维码宽高
		int codeWidth = encode.getWidth();
		int codeHeight = encode.getHeight();

		// 3、将二维码放入缓冲流
		BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < codeWidth; i++) {
			for (int j = 0; j < codeHeight; j++) {
				// 4、循环将二维码内容定入图片
				image.setRGB(i, j, encode.get(i, j) ? fillColor.getRGB() : backgrondColor.getRGB());
			}
		}

		// 5、将二维码写入图片
		ImageIO.write(image, outputType.name(), output);
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Result parseQrCode(String qrFilePath) throws Exception {
		MultiFormatReader formatReader = new MultiFormatReader();
		
		File file = new File(qrFilePath);
		if (!file.exists()) {
			throw new Exception("二维码文件不存在");
		}
		
		BufferedImage image = ImageIO.read(file);

		int width = image.getWidth();
		int height = image.getHeight();
		int[] data = new int[width * height];
		image.getRGB(0, 0, width, height, data, 0, width);
		LuminanceSource source = new RGBLuminanceSource(width, height, data);

		Binarizer binarizer = new HybridBinarizer(source);
		BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
		
		Map hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		Result result = formatReader.decode(binaryBitmap, hints);
		return result;
	}
}
