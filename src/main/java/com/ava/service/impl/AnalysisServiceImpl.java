package com.ava.service.impl;

import com.ava.dto.AnalysisDTO;
import com.ava.dto.enums.Scene;
import com.ava.service.AnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
public class AnalysisServiceImpl implements AnalysisService {
    // 从 classpath 加载提示词模板文件
    @Value("classpath:/prompts/summary-prompt.st")
    private Resource keyPointsPromptResource;

    @Value("classpath:/prompts/analysis-report-prompt.st")
    private Resource analysisReportPromptResource;

    @Value("classpath:/prompts/class/cornell-notes-prompt.st")
    private Resource classroomCornellNotesPrompt;

    @Value("classpath:/prompts/class/ebbinghaus-review-plan-prompt.st")
    private Resource classroomEbbinghausCurvePrompt;

    @Value("classpath:/prompts/class/class-quality-evaluation-prompt.st")
    private Resource classroomQualityAssessmentPrompt;

    @Value("classpath:/prompts/class/lecture-record-prompt.st")
    private Resource classroomRecordsPrompt;

    @Value("classpath:/prompts/class/common-errors-diagnosis-prompt.st")
    private Resource classroomWrongPointsPrompt;

    // 会议相关场景的提示词模板
    @Value("classpath:/prompts/meeting/brainstorming-summary-prompt.st")
    private Resource meetingBrainstormingSummaryPrompt;

    @Value("classpath:/prompts/meeting/business-insights-prompt.st")
    private Resource meetingBusinessInsightsPrompt;

    @Value("classpath:/prompts/meeting/decision-and-delegation-prompt.st")
    private Resource meetingDecisionDelegationPrompt;

    @Value("classpath:/prompts/meeting/meeting-summary-prompt.st")
    private Resource meetingSummaryPrompt;

    @Value("classpath:/prompts/meeting/meeting-visualization-prompt.st")
    private Resource meetingVisualizationPrompt;

    // 访谈相关场景的提示词模板
    @Value("classpath:/prompts/interview/content-value-prompt.st")
    private Resource interviewContentValuePrompt;

    @Value("classpath:/prompts/interview/emotional-analysis-prompt.st")
    private Resource interviewEmotionalAnalysisPrompt;

    @Value("classpath:/prompts/interview/logical-coherence-prompt.st")
    private Resource interviewLogicalCoherencePrompt;

    @Value("classpath:/prompts/interview/question-effectiveness-prompt.st")
    private Resource interviewQuestionEffectivenessPrompt;

    // 沟通相关场景的提示词模板
    @Value("classpath:/prompts/conversation/consulting-service-record-prompt.st")
    private Resource communicationConsultingServiceRecordPrompt;

    @Value("classpath:/prompts/conversation/counseling-session-record-prompt.st")
    private Resource communicationCounselingSessionRecordPrompt;

    @Value("classpath:/prompts/conversation/customer-service-record-prompt.st")
    private Resource communicationCustomerServiceRecordPrompt;

    @Value("classpath:/prompts/conversation/home-school-comms-log-prompt.st")
    private Resource communicationHomeSchoolCommsLogPrompt;

    @Value("classpath:/prompts/conversation/workplace-comms-record-prompt.st")
    private Resource communicationWorkplaceCommsRecordPrompt;

    // 培训相关场景的提示词模板
    @Value("classpath:/prompts/training/action-follow-up-plan-prompt.st")
    private Resource trainingActionFollowUpPlanPrompt;

    @Value("classpath:/prompts/training/learning-summary-report-prompt.st")
    private Resource trainingLearningSummaryReportPrompt;

    @Value("classpath:/prompts/training/training-effectiveness-evaluation-prompt.st")
    private Resource trainingEffectivenessEvaluationPrompt;

    @Value("classpath:/prompts/training/training-feedback-summary-prompt.st")
    private Resource trainingFeedbackSummaryPrompt;

    @Value("classpath:/prompts/training/training-quality-assessment-prompt.st")
    private Resource trainingQualityAssessmentPrompt;

    private final ChatClient chatClient;

    @Autowired
    public AnalysisServiceImpl(@Qualifier("openAiChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * 根据输入文本生成关键纪要
     *
     * @param text 语音转录的原始文本
     * @return AI 生成的关键纪要
     */
    public String generateKeyPoints(String text) {
        PromptTemplate promptTemplate = new PromptTemplate(keyPointsPromptResource);
        // 将原始文本填充到提示词模板的 "text" 变量中
        var prompt = promptTemplate.create(Map.of("text", text));
        log.info("Generating key points for text: {}", text);

        return chatClient.prompt(prompt)
                .call()
                .content();
    }

    /**
     * 根据输入文本生成分析报告
     *
     * @param text 语音转录的原始文本
     * @return AI 生成的分析报告
     */
    public String generateReport(String text) {
        PromptTemplate promptTemplate = new PromptTemplate(analysisReportPromptResource);
        // 将原始文本填充到提示词模板的 "text" 变量中
        var prompt = promptTemplate.create(Map.of("text", text));
        log.info("Generating all report for text: {}", text);

        return chatClient.prompt(prompt)
                .call()
                .content();
    }

    /**
     * 根据输入文本生成分析报告
     *
     * @param request 分析请求，包含内容和场景
     * @return AI 生成的分析报告
     */
    public String generateReport(AnalysisDTO.AnalysisRequest request) {
        // 获取对应场景的提示词模板资源
        Resource promptResource = getPromptResourceByScene(request.scene());

        PromptTemplate promptTemplate = new PromptTemplate(promptResource);
        // 将原始文本填充到提示词模板的 "text" 变量中
        var prompt = promptTemplate.create(Map.of("text", request.content()));
        log.info("Generating {} report for text length: {}", promptResource, request.content().length());

        return chatClient.prompt(prompt)
                .call()
                .content();
    }

    /**
     * 根据场景选择对应的提示词模板资源
     *
     * @param scene 场景类型
     * @return 对应场景的提示词模板资源
     */
    private Resource getPromptResourceByScene(Scene scene) {
        // 根据场景类型返回对应的提示词模板资源
        switch (scene) {
            // 课堂相关场景
            case CLASS_QUALITY_EVALUATION_PROMPT:
                return classroomQualityAssessmentPrompt;
            case COMMON_ERRORS_DIAGNOSIS_PROMPT:
                return classroomWrongPointsPrompt;
            case CORNELL_NOTES_PROMPT:
                return classroomCornellNotesPrompt;
            case EBBINGHAUS_REVIEW_PLAN_PROMPT:
                return classroomEbbinghausCurvePrompt;
            case LECTURE_RECORD_PROMPT:
                return classroomRecordsPrompt;

            // 会议相关场景
            case BRAINSTORMING_SUMMARY_PROMPT:
                return meetingBrainstormingSummaryPrompt;
            case BUSINESS_INSIGHTS_PROMPT:
                return meetingBusinessInsightsPrompt;
            case DECISION_AND_DELEGATION_PROMPT:
                return meetingDecisionDelegationPrompt;
            case MEETING_SUMMARY_PROMPT:
                return meetingSummaryPrompt;
            case MEETING_VISUALIZATION_PROMPT:
                return meetingVisualizationPrompt;

            // 访谈相关场景
            case CONTENT_VALUE_PROMPT:
                return interviewContentValuePrompt;
            case EMOTIONAL_ANALYSIS_PROMPT:
                return interviewEmotionalAnalysisPrompt;
            case LOGICAL_COHERENCE_PROMPT:
                return interviewLogicalCoherencePrompt;
            case QUESTION_EFFECTIVENESS_PROMPT:
                return interviewQuestionEffectivenessPrompt;

            // 沟通相关场景
            case CONSULTING_SERVICE_RECORD_PROMPT:
                return communicationConsultingServiceRecordPrompt;
            case COUNSELING_SESSION_RECORD_PROMPT:
                return communicationCounselingSessionRecordPrompt;
            case CUSTOMER_SERVICE_RECORD_PROMPT:
                return communicationCustomerServiceRecordPrompt;
            case HOME_SCHOOL_COMMS_LOG_PROMPT:
                return communicationHomeSchoolCommsLogPrompt;
            case WORKPLACE_COMMS_RECORD_PROMPT:
                return communicationWorkplaceCommsRecordPrompt;

            // 培训相关场景
            case ACTION_FOLLOW_UP_PLAN_PROMPT:
                return trainingActionFollowUpPlanPrompt;
            case LEARNING_SUMMARY_REPORT_PROMPT:
                return trainingLearningSummaryReportPrompt;
            case TRAINING_EFFECTIVENESS_EVALUATION_PROMPT:
                return trainingEffectivenessEvaluationPrompt;
            case TRAINING_FEEDBACK_SUMMARY_PROMPT:
                return trainingFeedbackSummaryPrompt;
            case TRAINING_QUALITY_ASSESSMENT_PROMPT:
                return trainingQualityAssessmentPrompt;

            // 其他场景
            case OTHER:
            default:
                return analysisReportPromptResource;
        }
    }

    private String readResource(Resource promptResource) {
        String templateString;
        try {
            try (InputStream inputStream = promptResource.getInputStream()) {
                templateString = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            // 处理文件读取异常
            log.error("Failed to read prompt template resource.", e);
            throw new RuntimeException("加载Prompt模板失败", e);
        }
        return templateString;
    }

    /**
     * 生成总结
     *
     * @param input
     * @return
     */
    public String generateSummary(AnalysisDTO.AnalysisRequest input) {
        // Step 1: 从 Resource 中将模板文件读取为字符串
        Resource promptResource = getPromptResourceByScene(input.scene());
        String templateString = readResource(promptResource);

        // Step 2: 获取 language 参数
        String language = "中文";
        switch (input.language()) {
            case "en-US":
                language = "英文";
                break;
            case "ja-JP":
                language = "日文";
                break;
            case "ko-KR":
                language = "韩文";
                break;
            case "zh-TW", "zh-HK":
                language = "繁体中文";
                break;
            default:
                language = "中文";
                break;
        }

        // Step 3: 动态构建最终的 System Prompt
        // 3.1 替换模板中已有的 {language} 占位符
        String processedTemplate = templateString.replace("{language}", language);

        // 3.2 构造并附加新的语言指令
        // 为了让LLM更好地理解，建议使用清晰的标题和换行
        String languageInstruction = String.format("\n\n# 输出语言指令\n请严格使用%s语言进行回复。", language);
        String finalSystemPrompt = processedTemplate + languageInstruction;
        log.info("Final System Prompt: {}", finalSystemPrompt);

        // Step 4: 调用 ChatClient，传入处理后的字符串
        // 注意：.system() 方法可以直接接收一个 String
        return this.chatClient.prompt()
                .system(finalSystemPrompt)
                .user(input.content()) // .user()方法会填充 {text} 占位符
                .call()
                .content();
    }
}
