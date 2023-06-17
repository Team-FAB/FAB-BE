package com.fab.banggabgo.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3Client;
import java.net.URL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

  @Mock
  AmazonS3Client amazonS3ClientMock;

  @InjectMocks
  S3Service s3Service;

  @Test
  @DisplayName("S3 - 이미지 업로드테스트 (이미지 url 반환)")
  public void testCreateNewFileName() throws Exception {

    var bucket = "test-bucket";
    var mockFile = new MockMultipartFile(
        "file",
        "test.jpg",
        "image/jpeg",
        "test file content".getBytes()
    );

    var fileName = "test.jpg";
    var stub_url = "http://test.com/" + fileName;
    ReflectionTestUtils.setField(s3Service, "bucket", bucket);

    //when
    when(amazonS3ClientMock.putObject(any())).thenReturn(null);
    when(amazonS3ClientMock.getUrl(eq(bucket), anyString())).thenReturn(new URL(stub_url));

    var result = s3Service.fileUpload(mockFile);

    //then
    verify(amazonS3ClientMock, times(1)).putObject(any());
    Assertions.assertEquals(result, stub_url);

  }
}