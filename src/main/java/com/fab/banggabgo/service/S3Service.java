package com.fab.banggabgo.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.util.MultipartFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3Client amazonS3Client;
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;


  public String fileUpload(MultipartFile file) throws CustomException {
    var newFileName = MultipartFileUtil.createNewFileName(file.getOriginalFilename());
    var objMeta = MultipartFileUtil.getObjMeta(file);
    try {
      amazonS3Client.putObject(
          new PutObjectRequest(bucket, newFileName, file.getInputStream(), objMeta));
    } catch (Exception e) {
      throw new CustomException(ErrorCode.AMAZON_S3_UPLOAD_ERROR);
    }
    return amazonS3Client.getUrl(bucket, newFileName).toString();
  }




}
