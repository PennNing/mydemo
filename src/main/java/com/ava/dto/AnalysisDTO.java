package com.ava.dto;

import jakarta.validation.constraints.NotBlank;

public class AnalysisDTO {

    /**
     * 分析请求体
     * @param text 需要分析的原始文本内容
     */
    public record AnalysisRequest(
            @NotBlank(message = "分析内容不能为空")
            String text
    ) {}

    /**
     * 分析结果响应体
     *
     * @param keyPoints      关键纪要 (Markdown 格式)
     * @param analysisReport 分析报告 (Markdown 格式)
     */
    public record AnalysisResponse(
            String keyPoints,
            String analysisReport
    ) {}
}
