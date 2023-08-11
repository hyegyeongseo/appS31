package org.zerock.apps3.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.apps3.dto.SampleDTO;
import org.zerock.apps3.utils.LocalUploader;
import org.zerock.apps3.utils.S3Uploader;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/sample")
public class SampleController {
	
	private final LocalUploader localUploader;
	
	private final S3Uploader s3Uploader;
	
	@PostMapping("/upload")
	public List<String> upload(SampleDTO sampleDTO){
		MultipartFile[] files = sampleDTO.getFiles();
		
		if(files == null || files.length <=0) return null;
		
		List<String> uploadFilePaths = new ArrayList<>();
		
		for(MultipartFile file:files) {
			uploadFilePaths.addAll(localUploader.uploadLocal(file));
		}
		log.info("-----------------------------------");
		log.info(uploadFilePaths);
		
		List<String> s3Paths = uploadFilePaths
				               .stream()
				               .map((fileName)->s3Uploader.upload(fileName))
				               .collect(Collectors.toList());
		
		return s3Paths;
		
	}
	
	@GetMapping("/upload") //http://ec2-13-209-72-32.ap-northeast-2.compute.amazonaws.com:8080/api/sample/upload가 되는지 상세 설정(위의 post방식처럼)은 안하고 주소창으로 확인만 함
	public void upload() {}
}