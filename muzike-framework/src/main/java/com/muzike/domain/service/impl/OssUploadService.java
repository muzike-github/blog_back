package com.muzike.domain.service.impl;

import com.google.gson.Gson;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.enums.AppHttpCodeEnum;
import com.muzike.domain.service.UploadService;
import com.muzike.exception.SystemException;
import com.muzike.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;

@Service
@ConfigurationProperties(prefix = "oss")
@Data
public class OssUploadService implements UploadService {

	private String accessKey;
	private String secretKey;
	private String bucket;
	@Override
	public ResponseResult uploadImg(MultipartFile img) {
		//todo 判断文件类型或文件大小
		//获得文件名，并对文件类型进行监测
		String originalFilename = img.getOriginalFilename();
		if(!originalFilename.endsWith(".png")&&!originalFilename.endsWith("jpg")&&
			!originalFilename.endsWith("jpeg")){
			throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
		}
		String filePath = PathUtils.generateFilePath(originalFilename);
		String url = OssUpload(img,filePath);
		return ResponseResult.okResult(url);
	}
	//参考官方文档
	private String OssUpload(MultipartFile imgFile,String filePath){
		//构造一个带指定 Region 对象的配置类
		Configuration cfg = new Configuration(Region.autoRegion());
		cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本

		UploadManager uploadManager = new UploadManager(cfg);

		//默认不指定key的情况下，以文件内容的hash值作为文件名
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		try {
			InputStream fileInputStream=imgFile.getInputStream();
			//filePath,文件上传路径
			Response response = uploadManager.put(fileInputStream, filePath, upToken,null,null);
			//解析上传成功的结果
			DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			System.out.println(putRet.key);
			System.out.println(putRet.hash);
			return "http://rmh2657md.hb-bkt.clouddn.com/"+ filePath;
		} catch (QiniuException ex) {
			Response r = ex.response;
			System.err.println(r.toString());
			try {
				System.err.println(r.bodyString());
			} catch (QiniuException ex2) {
				//ignore
			}
		}catch (Exception e){
			//ignore
		}
		return "www";
	}

}
