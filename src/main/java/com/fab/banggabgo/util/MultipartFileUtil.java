package com.fab.banggabgo.util;

import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

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
}
