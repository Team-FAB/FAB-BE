package com.fab.banggabgo.util;

import com.amazonaws.services.s3.model.ObjectMetadata;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@UtilityClass
public class MultipartFileUtil {
  public String createNewFileName(String filename) {
    return creteUUID() + "." + getFileExtension(filename).toLowerCase();
  }

  private String creteUUID() {
    return UUID.randomUUID().toString();
  }

  private String getFileExtension(String filename) {
    return StringUtils.getFilenameExtension(filename);
  }
  public ObjectMetadata getObjMeta(MultipartFile file) {
    var objectMeta = new ObjectMetadata();
    objectMeta.setContentLength(file.getSize());
    objectMeta.setContentType(file.getContentType());

    return objectMeta;
  }
}
