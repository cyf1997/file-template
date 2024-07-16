package com.tunanc.filetemplate.uitls;

import cn.hutool.core.exceptions.ExceptionUtil;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GroovyUtil {


    private static final Logger log = LoggerFactory.getLogger(GroovyUtil.class);
    private static final ConcurrentMap<String, ScriptCache> NAME_AND_SCRIPT_CACHE = new ConcurrentHashMap<>();

    /**
     * 执行脚本
     * @param scriptText 脚本
     * @param name 脚本名称；唯一
     * @param args 方法参数
     */
    public static void eval(String scriptText, String name, Object[] args) {
        // 脚本为空直接返回
        if (StringUtils.isBlank(scriptText)) {
            return;
        }

        try {
            Script script;
            String newMd5 = fingerKey(scriptText);
            ScriptCache scriptCache = NAME_AND_SCRIPT_CACHE.computeIfAbsent(name, n -> new ScriptCache());
            if (newMd5.equals(scriptCache.getMd5())) {
                script = scriptCache.getScript();
            }else{
                synchronized (scriptCache) {
                    if (newMd5.equals(scriptCache.getMd5())) {
                        script = scriptCache.getScript();
                    } else {
                        GroovyClassLoader classLoader = new GroovyClassLoader();
                        Class<?> aClass = classLoader.parseClass(scriptText, classLoader.generateScriptName());
                        script = InvokerHelper.createScript(aClass, new Binding());
                        scriptCache.setScript(script, newMd5);
                    }
                }
            }


            script.invokeMethod("eval", args);
        } catch (Exception e) {
            log.error("执行groovy脚本命令发生错误：{}", ExceptionUtil.stacktraceToString(e));
            throw new RuntimeException("执行groovy脚本命令发生错误");
        }
    }

    private static class ScriptCache {
        private Script script;
        private String md5;

        public Script getScript() {
            return script;
        }

        public void setScript(Script script, String md5) {
            this.script = script;
            this.md5 = md5;
        }

        public String getMd5() {
            return md5;
        }

    }


    // 为脚本代码生成md5指纹
    public static String fingerKey(String scriptText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(scriptText.getBytes(StandardCharsets.UTF_8));

            final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
            StringBuilder ret = new StringBuilder(bytes.length * 2);
            for (int i=0; i<bytes.length; i++) {
                ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
                ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
            }
            return ret.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
