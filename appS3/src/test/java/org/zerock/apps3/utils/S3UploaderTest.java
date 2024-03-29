package org.zerock.apps3.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class S3UploaderTest {
	
	@Autowired
	private S3Uploader s3Uploader;
	
	@Test
	public void testUpload() {
		try {
			  String filePath = "c:\\zzz\\test.png";
			  String uploadName = s3Uploader.upload(filePath);
			  
			  log.info(uploadName);
			  
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}