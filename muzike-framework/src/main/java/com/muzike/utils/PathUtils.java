package com.muzike.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PathUtils {
	public static String generateFilePath(String fileName){
		//根据日期生成路径
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/");
		String datePath=dateFormat.format(new Date());
		//uuid作为文件名
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		//后缀名和上传文件的后缀名一致
		int index = fileName.lastIndexOf(".");
		String fileType = fileName.substring(index);
		//生成文件夹和文件名2000/06/29/test.png
		return datePath + uuid + fileType;

	}
}
