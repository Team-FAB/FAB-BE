package com.fab.banggabgo.entity.chat;

import java.time.LocalDateTime;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "chat")
@Data
@Builder
@EnableMongoAuditing
public class Chat {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  private String roomId;
  private String userName;
  private String msg;
  @Field
  @CreatedDate
  private LocalDateTime createDate;

}
