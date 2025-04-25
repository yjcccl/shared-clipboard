package com.clipboard.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class ClipboardItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String content;
    private String deviceInfo;
    private String type; // "text" 或 "image"
    
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] imageData;
    
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}