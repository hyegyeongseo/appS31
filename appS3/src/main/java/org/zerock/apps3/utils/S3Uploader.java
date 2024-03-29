package org.zerock.apps3.utils;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class S3Uploader {
	
 private final AmazonS3Client amazonS3Client; //final 필드는 생성과 동시에 초기화 or 생성자에서 초기화
 
 @Value("${cloud.aws.s3.bucket}")
 public String bucket;//s3버킷 이름 = "zerock-s3-bucket-01-01-01"
 
 public String upload(String filePath) throws RuntimeException {
	 //업로드할 파일 정보 생성
	 File targetFile = new File(filePath);
	 //S3로 파일 업로드 후 uploadurl 정보 얻기
	 String uploadImageUrl = putS3(targetFile, targetFile.getName());//s3로 업로드 처리
	 //S3로 파일 업로드 후 원본 파일 삭제
	 removeOriginalFile(targetFile);
	 //업로드 한 image url 리턴
	 return uploadImageUrl;
	 
 }
 //S3 업로드 후 원본 파일 삭제
private void removeOriginalFile(File targetFile) {
	if(targetFile.exists() && targetFile.delete()) {
		log.info("File delete success");
		return;
	}
	log.info("fail to remove");
}

//s3로 업로드
private String putS3(File uploadFile, String fileName) throws RuntimeException{
	amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
			      .withCannedAcl(CannedAccessControlList.PublicRead));
	
	return amazonS3Client.getUrl(bucket, fileName).toString();
}

//S3 객체 삭제
public void removeS3File(String fileName) {
	final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, fileName);
	amazonS3Client.deleteObject(deleteObjectRequest);
}

}
