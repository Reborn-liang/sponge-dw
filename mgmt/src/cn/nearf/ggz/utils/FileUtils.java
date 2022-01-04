package cn.nearf.ggz.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cn.nearf.ggz.attachment.entity.AttachmentEntity;

public class FileUtils extends org.jeecgframework.core.util.FileUtils {
	
	public static final String fileExtendSplit = ".";
	public static final char pathSplit = '/';

	public static String getFileNameWithExtend(final String url) {
		String fileName = url.substring(url.lastIndexOf(pathSplit) + 1);
		return fileName;
	}
	
	public static String getFileNameWithoutExtend(final String url) {
		final String fullFile = getFileNameWithExtend(url);
		try {
			return fullFile.substring(0, fullFile.lastIndexOf(fileExtendSplit));
		} catch(Exception ex) {
			return fullFile;
		}
	}
	
	public static void main(String[] args) {
		String url = "http://www.test.com/test/1.jpg";
		System.out.println(getFileNameWithExtend(url));
		System.out.println(getFileNameWithoutExtend(url));
	}
	

	//获取文件名
	public static String getName(String filepath) {
		if (filepath.lastIndexOf('\\') > 0) {
			return filepath.substring(filepath.lastIndexOf('\\') + 1);
		} else if (filepath.lastIndexOf('/') > 0) {
			return filepath.substring(filepath.lastIndexOf('/') + 1);
		} else {
			return filepath;
		}
	}
	
	//获取后缀名
	public static String getExtend(String filepath) {
		if (filepath.lastIndexOf('.') > 0) {
			return filepath.substring(filepath.lastIndexOf('.') + 1,filepath.length());
		}
		return "";
	}
	
	
	//获取后缀名
	public static synchronized String getRadomName(String filepath) {
		if (filepath.lastIndexOf('.') > 0) {
			return UUID.randomUUID().toString().replaceAll("-", "") + filepath.substring(filepath.lastIndexOf('.'),filepath.length());
		}
		return filepath + UUID.randomUUID() + ".file";
	}
	
	public static boolean isPicture(AttachmentEntity att) {
		if(att.getContentType() != null && att.getContentType().contains("image/")) {
			return true;
		}
		return false;
	}
	
	public static String stringArrayToJsonArray(String[] content) {
		StringBuilder sb = new StringBuilder("[");
		try {
			for (String string : content) {
				if (sb.length() > 1) {
					sb.append(",");
				}
				sb.append(string);
			}
		} catch (Exception e) {
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static void updateAttachments(SystemService systemService,String filesString, String businesskey,String id) {
		System.out.println("filesString = " + filesString);
		List<AttachmentEntity> list = new ArrayList<AttachmentEntity>();
		if(StringUtil.isNotEmpty(filesString) && StringUtil.isNotEmpty(filesString.replace("\"", ""))) {
			List<AttachmentEntity> pics = new Gson().fromJson(filesString, new TypeToken<List<AttachmentEntity>>() {}.getType());
			if(pics != null) {
				for (AttachmentEntity att : pics) {
					att.setBusinesskey(businesskey);
					att.setLinkDataId(id);
					att.setCreateDate(new Date());
					list.add(att);
				}
			}
		}
		systemService.batchSaveOrUpdate(list);
	}
	
	public static String[] getPath(List<AttachmentEntity> atts) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbf = new StringBuilder();
		if(atts != null) {
			for(AttachmentEntity att : atts) {
				if(FileUtils.isPicture(att)) {
					sb.append(att.getPath());
					sb.append(",");
				}else {
					sbf.append(att.getPath());
					sbf.append(",");
				}
			}
			if(sb.length() > 0) {
				sb.deleteCharAt(sb.length()-1);
			}
			if(sbf.length() > 0) {
				sbf.deleteCharAt(sbf.length()-1);
			}
		}
		String[] paths = new String[] {"",""};
		paths[0] = sb.toString();
		paths[1] = sbf.toString();
		return paths;
	}
}
