package com.ava.controller;

import com.ava.dto.AnalysisDTO;
import com.ava.service.AnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    /**
     * 接收文本并返回关键纪要和分析报告
     * @param request 包含待分析文本的请求体
     * @return 包含关键纪要和分析报告的响应体
     */
    @PostMapping("/analyze")
    public AnalysisDTO.AnalysisResponse analyzeText(@Valid @RequestBody AnalysisDTO.AnalysisRequest request) {
        log.info("Received request to analyze text: {}", request.text());
        // 并行调用两个AI任务可以提高效率，但为了简单起见，这里我们串行调用
        // 在实际生产中，可以考虑使用 CompletableFuture 来并行化
        String keyPoints = analysisService.generateKeyPoints(request.text());
        log.info("Generated key points: {}", keyPoints);
        String analysisReport = analysisService.generateReport(request.text());
        log.info("Generated analysis report: {}", analysisReport);

        return new AnalysisDTO.AnalysisResponse(keyPoints, analysisReport);
    }
}
