package cn.nearf.ggz.utils.image.generate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageGenerate {

	public static void generate(String sourcePath, String outputPath, List<EmbededObject> embeds) throws Exception {

		BufferedImage srcImg = ImageIO.read(new File(sourcePath));

		Graphics2D g = srcImg.createGraphics();

		// 平滑
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);

		for (EmbededObject embed : embeds) {
			embed.paint(g, srcImg.getWidth(), srcImg.getHeight());
		}
		
		g.dispose();

		FileOutputStream outImgStream = new FileOutputStream(outputPath);
		ImageIO.write(srcImg, "png", outImgStream);
		outImgStream.flush();
		outImgStream.close();
	}

}
