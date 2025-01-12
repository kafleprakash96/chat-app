package com.chat.chatweb.controller;

import com.chat.chatweb.model.ChatRoom;
import com.chat.chatweb.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RoomController {

    private final ChatService chatService;

    public RoomController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<ChatRoom> rooms = chatService.getAllRooms(); // You'll need to add this method
        model.addAttribute("rooms", rooms);
        return "index";
    }

    @PostMapping("/rooms/create")
    public String createRoom(@RequestParam String name) {
        chatService.createRoom(name);
        return "redirect:/";
    }
}
