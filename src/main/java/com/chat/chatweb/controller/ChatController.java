package com.chat.chatweb.controller;

import com.chat.chatweb.model.ChatRoom;
import com.chat.chatweb.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class ChatController {


    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/room/{roomId}")
    public String getRoom(@PathVariable Long roomId,Model model){
        model.addAttribute("room", chatService.getRoom(roomId));
        model.addAttribute("messages", chatService.getRoomMessages(roomId));
        model.addAttribute("currentUser", "User-" + UUID.randomUUID().toString().substring(0, 4));
        model.addAttribute("roomId", roomId);
        return "chat";
    }

    @GetMapping("/create-room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatService.createRoom(name);
    }

    @PostMapping("/room/{roomId}/send")
    @ResponseBody
    public void sendMessage(@PathVariable Long roomId,
                            @RequestParam String content,
                            @RequestParam String sender){
        chatService.sendMessage(roomId,content,sender);
    }

    @PostMapping("/message/{messageId}/seen")
    @ResponseBody
    public void markAsSeen(@PathVariable Long messageId,
                           @RequestParam String username) {
        chatService.markAsSeen(messageId, username);
    }
}
