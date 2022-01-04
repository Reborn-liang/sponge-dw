package cn.nearf.ggz.config;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

import cn.nearf.ggz.utils.FileUtils;
import cn.nearf.ggz.utils.SequenceUtils;
import cn.nearf.ggz.utils.image.ImageUtils;
import cn.nearf.ggz.utils.image.ImageUtils.ImageSize;

public class ErpConfig extends SysConfig {
	
	public static final Set<String> MenuPicSupportType = new HashSet<String>() {
		private static final long serialVersionUID = 4615599186897735702L;
		{
			add("jpg");
			add("jpeg");
			add("png");
		}
	};
	
	public static final ImageSize MenuPicRelSize = ImageSize.newInstance(470, 290);
	
	public static final ImageSize MenuThumbPicSize = ImageSize.newInstance(180, 130);
	
	public static final ImageSize ElemePicSize = ImageSize.newInstance(480, 480);
	public static final long ElemePicLength = 5 * 1024 * 1024;
	
	
	public static final long MAX_ORDER_TS = 15 * 60 * 1000l;
	
	private static SocketAddress erpSocketAddress = null;
	
	public static final int ErpSocketTimeout = 10 * 1000;
	
	
	
	public static String getProperty(String key) {
		return getStringProperty(key);
	}
	
	public static boolean isShowTableCapacity() {
		return getBooleanProperty("Show_Table_Capacity");
	}
	
	public static synchronized SocketAddress getErpSocketAddress() {
		if (erpSocketAddress != null) {
			return erpSocketAddress;
		}
		try {
			String add = getStringProperty("ERP_SOCKET_SERVER");
			int port = getIntProperty("ERP_SOCKET_SERVER_PORT");
			erpSocketAddress = new InetSocketAddress(add, port);
			
		} catch (Exception e) {
			erpSocketAddress = new InetSocketAddress("erptest.helens.com.cn", 8888);
		}
		return erpSocketAddress;
	}
	
	public static synchronized String getManagerPhone() {
		return getStringProperty("Manager_Phone");
	}
	
	
	
	public static String getShareUrl() {
		return getStringProperty("SHARE_URL");
	}
	
	public static String getErpReportTemplateFilePath() {
		return getStringProperty("ERP_REPORT_TEMPLATE_FILE_PATH");
	}
	
	public static String updateServerImage(final String picPath) {
		File serverFile = new File(getServerFilePath(picPath));
		if (!serverFile.exists() || serverFile.isDirectory()) {
			return null;
		}
		
		final String fileExtend = FileUtils.getExtend(picPath);
		final String fileFullName = SequenceUtils.generateNumberCharSequence() + "." + fileExtend;
		
		final String newFilePath = getCKBaseDir() + fileFullName;
		
		File newFile = new File(newFilePath);
		if (newFile.exists()) {
			newFile.delete();
		}
		
		serverFile.renameTo(newFile);
		
		newFile = new File(newFilePath);
		newFile.setReadable(true, false);
		
		String path = getProperty("ck.userfiles");
		return path + fileFullName;
	}

	public static ImageSize getServerImageSize(final String url) throws Exception {
		String serverFilePath = getServerFilePath(url);
		ImageSize size =  ImageUtils.getFileSize(serverFilePath);
		if (size != null) {
			size.fileUrl = serverFilePath;
		} else {
			serverFilePath = URLDecoder.decode(serverFilePath, "utf-8");
			size = ImageUtils.getFileSize(serverFilePath);
			if (size != null) {
				size.fileUrl = serverFilePath;
			}
		}
		
		return size;
	}
	
	public static long getServerImageLength(final String url) throws Exception {
		String serverFilePath = getServerFilePath(url);
		try {
			File size = new File(serverFilePath);
			if (size.exists()) {
				return size.length();
			} else {
				serverFilePath = URLDecoder.decode(serverFilePath, "utf-8");
				size = new File(serverFilePath);
				if (size.exists()) {
					return size.length();
				}
			}
		} catch (Exception e) {
		}
		
		return 0l;
	}
	
	
	public static String getServerFilePath(final String url) {
		return getCKBaseDir() + "files/" + FileUtils.getFileNameWithExtend(url);
	}
	
	public static String getServerOtherPathBySubFolder(final String folder) {
		return getCKBaseDir() + folder + FileUtils.pathSplit;
	}
	
	private static String ckBaseDir;
	private static String getCKBaseDir() {
		if (ckBaseDir == null) {
			String baseDir = getProperty("ck.baseDir");
			if (baseDir.charAt(baseDir.length() - 1) == FileUtils.pathSplit) {
				ckBaseDir =  baseDir;
			} else {
				ckBaseDir = baseDir + "/";
			}
		}
		return ckBaseDir;
	}
	
	
	
	public static void main(String[] args) {
//		String filePath = getProperty("ck.baseDir");
//		System.err.println(getServerFilePath("http://erpimg.helens.com.cn/menuimg/big_watermelon_juice.jpg"));
//		System.err.println(chcekImage("http://erpimg.helens.com.cn/menuimg/big_watermelon_juice.jpg", "http://erpimg.helens.com.cn/menuimg/watermelon_juice.jpg"));
	}
}
