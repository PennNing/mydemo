package com.ava.dto;


import com.ava.dto.enums.Scene;

import javax.validation.constraints.NotNull;

public class AnalysisDTO {

    /**
     * 分析请求体
     *
     * @param language  语言
     * @param content   内容
     * @param scene     场景
     */
    public record AnalysisRequest(
            @NotNull(message = "语言不能为空")
            String language,
            @NotNull(message = "内容不能为空")
            String content,
            Scene scene
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
