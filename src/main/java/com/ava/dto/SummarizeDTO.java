package com.ava.dto;

import lombok.Data;

@Data
public class SummarizeDTO {
    private String language;           // "en", "zh", "ja" 等
    private String content;     // 转录内容
    private String scene;  // "meeting", "interview", "sales" 等
}
