package com.clipboard.controller;

import com.clipboard.model.ClipboardItem;
import com.clipboard.repository.ClipboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clipboard")
public class ClipboardController {

    @Autowired
    private ClipboardRepository clipboardRepository;

    @GetMapping
    public ResponseEntity<?> getClipboardItems(@RequestParam(required = false) String old) {
        try {
            List<ClipboardItem> items;
            if (old == null || old.isEmpty()) {
                // 如果old参数为空，获取最新的10条记录
                items = clipboardRepository.findAll(
                        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
                ).getContent();
            } else {
                // 获取ID小于old的10条记录
                Long oldId = Long.parseLong(old);
                items = clipboardRepository.findByIdLessThan(
                        oldId, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
                );
            }

            return ResponseEntity.ok(items);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid old parameter"));
        }
    }

    @PostMapping
    public ResponseEntity<?> addClipboardItem(@RequestBody ClipboardItemRequest request) {
        ClipboardItem item = new ClipboardItem();
        item.setContent(request.getContent());
        item.setDeviceInfo(request.getDeviceInfo());
        item.setType(request.getType());
        
        if ("image".equals(request.getType()) && request.getImageData() != null) {
            item.setImageData(request.getImageData());
        }
        
        ClipboardItem savedItem = clipboardRepository.save(item);
        return ResponseEntity.ok(savedItem);
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestClipboardItems(@RequestParam String newId) {
        try {
            Long id = Long.parseLong(newId);
            List<ClipboardItem> items = clipboardRepository.findByIdGreaterThan(
                    id, Sort.by(Sort.Direction.ASC, "id")
            );
            return ResponseEntity.ok(items);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid new parameter"));
        }
    }

    static class ClipboardItemRequest {
        private String content;
        private String deviceInfo;
        private String type;
        private byte[] imageData;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDeviceInfo() {
            return deviceInfo;
        }

        public void setDeviceInfo(String deviceInfo) {
            this.deviceInfo = deviceInfo;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public byte[] getImageData() {
            return imageData;
        }

        public void setImageData(byte[] imageData) {
            this.imageData = imageData;
        }
    }
}