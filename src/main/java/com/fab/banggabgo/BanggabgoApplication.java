package com.fab.banggabgo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BanggabgoApplication {

  static {
    System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
  }
  public static void main(String[] args) {
    SpringApplication.run(BanggabgoApplication.class, args);
  }

}
