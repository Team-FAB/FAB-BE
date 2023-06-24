package com.fab.banggabgo.repository.mongo;

import com.fab.banggabgo.entity.chat.Chat;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface ChatRepository extends MongoRepository<Chat,String> {
  public List<Chat> findByRoomId(String roomId);

}
