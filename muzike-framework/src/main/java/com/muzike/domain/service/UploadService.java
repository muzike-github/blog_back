package com.muzike.domain.service;


import com.muzike.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
	ResponseResult uploadImg(MultipartFile img);
}
