package com.ava.service;

import com.ava.util.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LanguageServiceTest {

    @Mock
    private RedisUtil redisUtil;

    @InjectMocks
    private LanguageService languageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteAllVoiceSource() {
        languageService.deleteAllVoiceSource();
    }

    /*@Test
    void setVoiceSourceByLang_ShouldSaveVoiceSourceToRedis_WhenRedisDoesNotThrowException() throws Exception {
        // Given
        String langCode = "en";
        String voiceSource = "http://example.com/voice/en";

        // When
        languageService.setVoiceSourceByLang(langCode, voiceSource);
    }*/

    @Test
    void getVoiceSourceByLang_ShouldReturnVoiceSource_WhenRedisReturnsValue() throws Exception {
        // Given
        String langCode = "en";
        String expectedVoiceSource = "http://example.com/voice/en";
        when(redisUtil.getCacheObject("lang:" + langCode)).thenReturn(expectedVoiceSource);

        // When
        String result = languageService.getVoiceSourceByLang(langCode);

        // Then
        assertEquals(expectedVoiceSource, result);
        verify(redisUtil).getCacheObject("lang:" + langCode);
    }

    /*@Test
    void deleteVoiceSourceByLang_ShouldDeleteVoiceSourceFromRedis_WhenRedisDoesNotThrowException() throws Exception {
        // Given
        String langCode = "en";
        when(redisUtil.getCacheObject("lang:" + langCode)).thenReturn(null);

        // When
        languageService.deleteVoiceSourceByLang(langCode);
    }*/

    /*@Test
    void getVoiceSourceByLang_ShouldReturnNull_WhenRedisThrowsException() throws Exception {
        // Given
        String langCode = "fr";
        when(redisUtil.getCacheObject("lang:" + langCode)).thenThrow(new RuntimeException("Redis error"));

        // When
        String result = languageService.getVoiceSourceByLang(langCode);

        // Then
        assertNull(result);
        verify(redisUtil).getCacheObject("lang:" + langCode);
    }*/

    /*@Test
    void getVoiceSourceByLang_ShouldReturnNull_WhenRedisReturnsNull() throws Exception {
        // Given
        String langCode = "es";
        when(redisUtil.getCacheObject("lang:" + langCode)).thenReturn(null);

        // When
        String result = languageService.getVoiceSourceByLang(langCode);

        // Then
        assertNull(result);
        verify(redisUtil).getCacheObject("lang:" + langCode);
    }*/
}
