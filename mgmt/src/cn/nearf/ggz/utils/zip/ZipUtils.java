package cn.nearf.ggz.utils.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

	public static void zip(String inputPath, String outputPath) throws Exception {
		// File zipFile = new File(zipFileName);
//		System.out.println("压缩中...");

		// 创建zip输出流
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(outputPath)));

		// 创建缓冲输出流
		BufferedOutputStream bos = new BufferedOutputStream(out);

		File sourceFile = new File(inputPath);

		// 调用函数
		compress(out, bos, sourceFile, sourceFile.getName());

		bos.close();
		out.close();
//		System.out.println("压缩完成");
	}

	private static void compress(ZipOutputStream out, BufferedOutputStream bos, File sourceFile, String base) throws Exception {
		// 如果路径为目录（文件夹）
		if (sourceFile.isDirectory()) {
			// 取出文件夹中的文件（或子文件夹）
			File[] flist = sourceFile.listFiles();
			if (flist.length == 0) {// 如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
				System.out.println(base + "/");
				out.putNextEntry(new ZipEntry(base + "/"));
			} else { // 如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
				for (int i = 0; i < flist.length; i++) {
					compress(out, bos, flist[i], base + "/" + flist[i].getName());
				}
			}
		} else { // 如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
			out.putNextEntry(new ZipEntry(base));
			FileInputStream fos = new FileInputStream(sourceFile);
			BufferedInputStream bis = new BufferedInputStream(fos);

			int tag;
//			System.out.println(base);
			// 将源文件写入到zip文件中
			while ((tag = bis.read()) != -1) {
				bos.write(tag);
			}
			bos.flush();
			
			bis.close();
			fos.close();
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		zip("/Users/Chris/Desktop/tmp/src/qr", "/Users/Chris/Desktop/tmp/src/qrx2.zip");
	}
}
