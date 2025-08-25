package com.ava.service;


import com.ava.dto.AnalysisDTO;

public interface AnalysisService {
    /**
     * 根据输入文本生成关键纪要
     *
     * @param text 语音转录的原始文本
     * @return AI 生成的关键纪要
     */
    public String generateKeyPoints(String text);

    /**
     * 根据输入文本生成报告
     *
     * @param text 语音转录的原始文本
     * @return AI 生成的报告
     */
    public String generateReport(String text);

    /**
     * 根据输入文本生成报告
     *
     * @param request 语音转录的输入参数
     * @return AI 生成的报告
     */
    public String generateReport(AnalysisDTO.AnalysisRequest request);

    /**
     * 根据输入文本生成摘要
     *
     * @param input 输入参数
     * @return AI 生成的摘要
     */
    public String generateSummary(AnalysisDTO.AnalysisRequest input);
}
