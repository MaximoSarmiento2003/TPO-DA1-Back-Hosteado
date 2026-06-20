package com.example.tpoDA.controller;

import com.example.tpoDA.dtos.notification.NotificationResponseDTO;
import com.example.tpoDA.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/mine")
    public ResponseEntity<List<NotificationResponseDTO>> getMine(Authentication auth) {
        return ResponseEntity.ok(notificationService.getMyNotifications(auth.getName()));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<NotificationResponseDTO>> getRecent(Authentication auth) {
        return ResponseEntity.ok(notificationService.getRecent(auth.getName()));
    }

    @GetMapping("/others")
    public ResponseEntity<List<NotificationResponseDTO>> getOthers(Authentication auth) {
        return ResponseEntity.ok(notificationService.getOthers(auth.getName()));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(Authentication auth) {
        return ResponseEntity.ok(notificationService.getUnreadCount(auth.getName()));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable Integer id, Authentication auth) {
        notificationService.markAsRead(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
