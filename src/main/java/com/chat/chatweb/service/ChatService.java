package com.chat.chatweb.service;

import com.chat.chatweb.model.ChatMessage;
import com.chat.chatweb.model.ChatRoom;
import com.chat.chatweb.repository.ChatMessageRepository;
import com.chat.chatweb.repository.ChatRoomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ChatService {

    private final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public ChatService(ChatRoomRepository chatRoomRepository,
                       ChatMessageRepository chatMessageRepository,
                       KafkaTemplate<String, String> kafkaTemplate) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public ChatRoom createRoom(String name){
        ChatRoom room = new ChatRoom();
        room.setName(name);
        return chatRoomRepository.save(room);
    }

    public ChatRoom getRoom(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
    }

    public List<ChatMessage> getRoomMessages(Long roomId) {
        // First verify the room exists
        if (!chatRoomRepository.existsById(roomId)) {
            throw new RuntimeException("Room not found with id: " + roomId);
        }

        return chatMessageRepository.findByChatRoomIdOrderByTimestampDesc(roomId);
    }

    public void sendMessage(Long roomId, String content, String sender){
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(
                        ()->new RuntimeException("Room not found"));

        ChatMessage message = new ChatMessage();
        message.setContent(content);
        message.setSender(sender);
        message.setTimestamp(LocalDateTime.now());
        message.setChatRoom(room);
        message.setConsumed(false);

        ChatMessage savedMessage = chatMessageRepository.save(message);

        try {
            // Serialize the message to JSON
            String serializedMessage = objectMapper.writeValueAsString(savedMessage);

            // Send the JSON string to Kafka
            kafkaTemplate.send("chat-topic", serializedMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize ChatMessage", e);
        }
    }

    public void markAsSeen(Long messageId, String username){
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(()-> new RuntimeException("Message not found"));

        message.getSeenby().add(username);
        chatMessageRepository.save(message);

        //Assuming 2 users seen the message
        if(message.getSeenby().size() >=2){
            message.setConsumed(true);
            chatMessageRepository.save(message);
        }
    }

    @KafkaListener(topics = "chat-topic",groupId = "chat-group")
    public void listen(ConsumerRecord<String,String> record){
        try{
            String messageJson = record.value();
            ChatMessage message = objectMapper.readValue(messageJson,ChatMessage.class);
            messagingTemplate.convertAndSend("/topic/room/" + message.getChatRoom().getId(),message);

        }catch (JsonProcessingException e){
            log.error("Error while processing message from kafka", e);
        }
    }


}
