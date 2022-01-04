package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import cn.nearf.ggz.utils.QR.QrUtils;
import cn.nearf.ggz.utils.image.ImageType;
import cn.nearf.ggz.utils.image.generate.EmbededImage;
import cn.nearf.ggz.utils.image.generate.EmbededObject;
import cn.nearf.ggz.utils.image.generate.EmbededText;
import cn.nearf.ggz.utils.image.generate.ImageGenerate;

public class Test {
	private static void test1() throws Exception {
		String txt = "测试";
		txt = "999";

		String srcImgFile = "/Users/Chris/Desktop/tmp/src/QR_template.jpg";
		String outImgFile = "/Users/Chris/Desktop/tmp/src/QR_test.png";
		BufferedImage srcImg = ImageIO.read(new File(srcImgFile));

		Graphics2D g = srcImg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);

		// g.drawImage(srcImg, 0, 0, srcImg.getWidth(), srcImg.getHeight(), null);
		g.setColor(Color.BLUE); // 根据图片的背景设置水印颜色

		Font font = new Font("Arial", Font.BOLD, 70);

		g.setFont(font); // 设置字体

		// 设置水印的坐标
		
		FontRenderContext frc = new FontRenderContext(null, true, true);
		Rectangle2D rect = font.getStringBounds(txt, frc);
		
		int width = (int) Math.round(rect.getWidth());
		int height = (int) Math.round(rect.getHeight());
		int rX = (int) Math.round(rect.getX());
		int rY = (int) Math.round(rect.getY());

		System.err.println(rX);
		System.err.println(rY);
		
		g.drawString(txt, -rX, -rY); // 画出水印


		g.dispose();

		FileOutputStream outImgStream = new FileOutputStream(outImgFile);
		ImageIO.write(srcImg, "png", outImgStream);
		System.out.println("添加水印完成");
		outImgStream.flush();
		outImgStream.close();
	}
	
	public static void test2() throws Exception {
		String srcImgFile = "/Users/Chris/Desktop/tmp/src/QR_template.jpg";
		String outputPath = "/Users/Chris/Desktop/tmp/src/QR_ok2.png";
		
		List<EmbededObject> embeds = new ArrayList<>();
		
		EmbededText text1 = new EmbededText("999", Color.white, new Font("Arial", Font.BOLD, 70));
		text1.withXCenter(0, 105);
		embeds.add(text1);
		
		EmbededText text2 = new EmbededText("武汉光谷店", Color.white, new Font("黑体", Font.PLAIN, 25));
		text2.withXCenter(483, 528);
		embeds.add(text2);
		
		EmbededImage img1 = new EmbededImage("/Users/Chris/Desktop/tmp/src/yumen1.png");
		img1.withXCenter(118, null);
		embeds.add(img1);
		
		
		EmbededImage img2 = new EmbededImage("/Users/Chris/Desktop/tmp/src/logo_big.png");
		int logoTop = 118 + (img1.height() - img2.height()) / 2;
		int logoBottom = logoTop + img2.getYEnd();
		img2.withXCenter(logoTop, null);
		embeds.add(img2);
		
		ImageGenerate.generate(srcImgFile, outputPath, embeds);
		
	}
	
	
	public static void test3() {
	}
	
	public static void main(String[] args) throws Exception {
		// String text = "http://www.baidu.com";
		// int width = 800;
		// int height = 800;
		// String outFile = "/Users/Chris/Desktop/tmp/src/test.jpg";
		
		QrUtils.generateQrCode(340, 340, "http://127.0.0.1:8080/ggz/api/wechat/auth?hstype=2&poId=oMj1H1Ko9cF8xnz87Ru4uRsikduU&ts=1521625537", Color.black, Color.white, ImageType.png, "/Users/Chris/Desktop/tmp/src/", "yumen0");
		if (true) {
			return;
		}
		
		test2();
		if (true) {
			return;
		}

		QrUtils.generateQrCode(340, 340, "http://127.0.0.1:8080/ggz/api/wechat/auth?hstype=2&poId=oMj1H1Ko9cF8xnz87Ru4uRsikduU&ts=1521625537", Color.black, Color.white, ImageType.png, "/Users/Chris/Desktop/tmp/src/", "yumen1");

		String txt = "测试";
		txt = "999";

		String srcImgFile = "/Users/Chris/Desktop/tmp/src/QR_template.jpg";
		String outImgFile = "/Users/Chris/Desktop/tmp/src/QR_6.png";
		BufferedImage srcImg = ImageIO.read(new File(srcImgFile));

		Graphics2D g = srcImg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);

		// g.drawImage(srcImg, 0, 0, srcImg.getWidth(), srcImg.getHeight(), null);
		g.setColor(Color.white); // 根据图片的背景设置水印颜色

		Font font = new Font("Arial", Font.BOLD, 70);

		g.setFont(font); // 设置字体

		// 设置水印的坐标

		FontRenderContext frc = new FontRenderContext(null, true, true);
		Rectangle2D r2D = font.getStringBounds(txt, frc);
		int rWidth = (int) Math.round(r2D.getWidth());
		int rHeight = (int) Math.round(r2D.getHeight());
		int rX = (int) Math.round(r2D.getX());
		int rY = (int) Math.round(r2D.getY());

		int a = (srcImg.getWidth() / 2) - (rWidth / 2) - rX;
		int b = (105 / 2) - (rHeight / 2) - rY;

		System.err.println("a:" + a);
		System.err.println("b:" + b);

		g.drawString(txt, a, b); // 画出水印

		String qrImgFile = "/Users/Chris/Desktop/tmp/src/yumen1.png";
		BufferedImage qrImg = ImageIO.read(new File(qrImgFile));
		g.drawImage(qrImg, (srcImg.getWidth() - qrImg.getWidth()) / 2, 118, qrImg.getWidth(), qrImg.getHeight(), null);

		String logoImgFile = "/Users/Chris/Desktop/tmp/src/logo_big.png";
		BufferedImage logoImg = ImageIO.read(new File(logoImgFile));
		g.drawImage(logoImg, (srcImg.getWidth() - logoImg.getWidth()) / 2, 118 + qrImg.getHeight() / 2 - logoImg.getHeight() / 2, logoImg.getWidth(), logoImg.getHeight(), null);

		String storeName = "武汉光谷店";

		Font descfont = new Font("黑体", Font.PLAIN, 20);
		g.setFont(descfont); // 设置字体

		// 设置水印的坐标
		Rectangle2D rs2D = descfont.getStringBounds(storeName, frc);
		int rsWidth = (int) Math.round(rs2D.getWidth());
		int rsHeight = (int) Math.round(rs2D.getHeight());
		int rsX = (int) Math.round(rs2D.getX());
		int rsY = (int) Math.round(rs2D.getY());

		int sa = (srcImg.getWidth() / 2) - (rsWidth / 2) - rsX;
		int sb = 483 + ((528 - 483) / 2) - (rsHeight / 2) - rsY;
		g.drawString(storeName, sa, sb); // 画出水印

		g.dispose();

		FileOutputStream outImgStream = new FileOutputStream(outImgFile);
		ImageIO.write(srcImg, "png", outImgStream);
		System.out.println("添加水印完成");
		outImgStream.flush();
		outImgStream.close();

		// QrUtils.generateQrCode(400, 400, "http://www.taobao.com", Color.red,
		// Color.white, ImageType.png, "/Users/Chris/Desktop/tmp/src/", "yumen");
	}

	public static void generateQr(final int width, final int height, final String content, Color fillColor, Color backgrondColor, final String outputFile) throws Exception {
		Map<EncodeHintType, String> his = new HashMap<EncodeHintType, String>();
		// 设置编码字符集
		his.put(EncodeHintType.CHARACTER_SET, "utf-8");

		BitMatrix encode = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, his);

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

		File outPutImage = new File(outputFile);
		// 如果图片不存在创建图片
		if (!outPutImage.exists()) {
			outPutImage.createNewFile();
		}

		// 5、将二维码写入图片
		ImageIO.write(image, "jpg", outPutImage);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object zxingCodeAnalyze(String analyzePath) throws Exception {
		MultiFormatReader formatReader = new MultiFormatReader();
		Object result = null;
		try {
			File file = new File(analyzePath);
			if (!file.exists()) {
				return "二维码不存在";
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
			result = formatReader.decode(binaryBitmap, hints);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
