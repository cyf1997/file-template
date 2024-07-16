package com.tunanc.filetemplate.uitls;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.tunanc.filetemplate.Exception.FileTemplateException;
import com.tunanc.filetemplate.entity.FileTemplateField;
import com.tunanc.filetemplate.enums.FieldTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FileFieldUtil {


    /**
     * 复杂适配转成map
     * @param paramMap 业务参数
     * @param configuredParamMap 字段配置
     * @return
     */
    public static Map<String,Object> paramAdapterToMap(Map<String, Object> paramMap, Map<String, FileTemplateField> configuredParamMap) {
        if (configuredParamMap == null) {
            return null;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        configuredParamMap.forEach((k, v) -> {
            String paramKey = v.getParamKey();
            String paramType = v.getParamType();

            Object o = paramMap.get(paramKey);
            if ("0".equals(v.getFillValidationFlg())){
                Assert.notNull(o, ()->new FileTemplateException(StrFormatter.format("模板填充参数{}不能为空",paramKey)) );

            }
            if (ObjectUtil.isNotNull(o)){
                if (FieldTypeEnum.LIST.getValue().equals(paramType)){
                    if (o instanceof Collection){
                        Iterator<?> iterator = ((Collection<?>) o).iterator();
                        int i=1;
                        while (iterator.hasNext()){
                            Object next = iterator.next();
                            Map<String, Object> map = BeanUtil.beanToMap(next, Boolean.FALSE, Boolean.TRUE);
                            int finalIndex = i;
                            map.forEach((mk, mv)->{
                                String result=paramKey+"_"+mk+ finalIndex;
                                hashMap.put(result,mv);
                            });
                            i++;
                        }
                    }else{
                        log.error("模板字段：{}的配置类型为：{},但是当前参数字段类型并不是Collection类型",v.getParamKey(),FieldTypeEnum.LIST.getValue());
                        throw new FileTemplateException("当前字段类型需与模板配置类型不符");
                    }
                } else if (FieldTypeEnum.TABLE.getValue().equals(paramType)) {
                    log.debug("处理table类型字段");
                    if (o instanceof Collection){
                        String replaceValue = v.getReplaceValue();
                        Iterator<?> iterator = ((Collection<?>) o).iterator();
                        StringBuilder builder = new StringBuilder();
                        while (iterator.hasNext()){
                            Object next = iterator.next();
                            Map<String, Object> map = BeanUtil.beanToMap(next, Boolean.FALSE, Boolean.TRUE);
                            //将replaceValue的html中的占位符替换成map中的值
                            builder.append(StrUtil.format(replaceValue,map));
                        }
                        if ("0".equals(v.getFillValidationFlg())){
                            log.debug("字段内容必填，校验行内数据是否有未替换的");
                            List<String> strings = extractFields(builder.toString());
                            if (CollectionUtil.isNotEmpty(strings)){
                                throw new FileTemplateException(StrFormatter.format("{}的字段{}为空",v.getParamDesc(),strings.toString()));
                            }
                            hashMap.put(k,builder);
                        }else{
                            // 使用replaceAll方法替换匹配的占位符为空字符串
                            hashMap.put(k,builder.toString().replaceAll("\\{.*?}", ""));
                        }
                    }else{
                        log.error("模板字段：{}的配置类型为：{},但是当前参数字段类型并不是Collection类型",v.getParamKey(),FieldTypeEnum.LIST.getValue());
                        throw new FileTemplateException("当前字段类型需为Collection");
                    }
                }else{
                    hashMap.put(k,o);
                }
            }else{
                //如果业务数据为空，考虑将配置中的默认值取出赋值
                String defaultValue = v.getDefaultValue();
                if (StrUtil.isNotEmpty(defaultValue)){
                    hashMap.put(k,defaultValue);
                }
            }
        });
        return hashMap;
    }

    public static List<String> extractFields(String text) {
        // 定义正则表达式，用于匹配花括号内的内容
        Pattern pattern = Pattern.compile("\\{([^{}]*)}");
        // 创建Matcher对象
        Matcher matcher = pattern.matcher(text);

        List<String> placeholders = new ArrayList<>();
        // 查找与模式匹配的部分
        while (matcher.find()) {
            // 添加匹配到的占位符到列表中
            placeholders.add(matcher.group(1));
        }

        return placeholders;
    }


}
