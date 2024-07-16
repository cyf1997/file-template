package com.tunanc.filetemplate.service;


import com.tunanc.filetemplate.Exception.FileTemplateException;
import com.tunanc.filetemplate.constant.TemplateConstant;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 获取模板类型服务策略
 */
@Component
public class TemplateTypeStrategy {


    @Resource
    private Map<String,TemplateTypeService> templateTypeServiceMap;





    public TemplateTypeService getService(String type){
        TemplateTypeService templateTypeService = templateTypeServiceMap.get(type + TemplateConstant.TEMPLATE_TYPE_SERVICE_SUFFIX);
        if (templateTypeService == null){
            throw new FileTemplateException("模板类型未匹配上服务类");
        }
        return templateTypeService;
    }
}
