package com.ava.service;

import com.ava.util.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LanguageService {

    @Resource
    private RedisUtil redisUtil;

    private static final String VOICE_SOURCE_KEY_PREFIX = "lang:";

    public String getVoiceSourceByLang(String langCode) {
        String voiceSource= null;
        try {
            voiceSource = (String) redisUtil.getCacheObject(VOICE_SOURCE_KEY_PREFIX + langCode);
            log.info("从redis获取语音源成功，langCode: {}, voiceSource: {}", langCode, voiceSource);
            return voiceSource;
        } catch (Exception e) {
            log.error("从redis获取语音源失败", e);
        }
        return null;
    }

    public void setVoiceSourceByLang(String langCode, String voiceSource) {
        try {
            redisUtil.setCacheObject(VOICE_SOURCE_KEY_PREFIX + langCode, voiceSource);
            log.info("保存语音源成功，langCode: {}, voiceSource: {}", langCode, voiceSource);
        } catch (Exception e) {
            log.error("保存语音源失败", e);
        }
    }

    public void deleteVoiceSourceByLang(String langCode) {
        try {
            redisUtil.deleteObject(VOICE_SOURCE_KEY_PREFIX + langCode);
            log.warn("删除语音源成功，langCode: {}", langCode);
        } catch (Exception e) {
            log.error("删除语音源失败", e);
        }
    }

    public void deleteAllVoiceSource() {
        try {
            redisUtil.deleteObject(redisUtil.keys(VOICE_SOURCE_KEY_PREFIX + "*"));
            log.info("删除所有语音源成功");
        } catch (Exception e) {
            log.error("删除所有语音源失败", e);
        }
    }

    public void clearVoiceSourceCache() {
        try {
            redisUtil.deleteObject(redisUtil.keys(VOICE_SOURCE_KEY_PREFIX + "*"));
            log.info("清除语音源缓存成功");
        } catch (Exception e) {
            log.error("清除语音源缓存失败", e);
        }
    }

    public void clearAllCache() {
        try {
            redisUtil.deleteObject(redisUtil.keys("*"));
            log.info("清除所有缓存成功");
        } catch (Exception e) {
            log.error("清除所有缓存失败", e);
        }
    }

    public void getCacheList(String key) {
        try {
            Object data = redisUtil.getCacheList(key);
            log.info("从redis获取缓存成功，key: {}", key, data);
        } catch (Exception e) {
            log.error("从redis获取缓存失败", e);
        }
    }
}






