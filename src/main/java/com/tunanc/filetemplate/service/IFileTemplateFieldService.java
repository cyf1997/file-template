package com.tunanc.filetemplate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tunanc.filetemplate.entity.FileTemplateField;

import java.util.List;
import java.util.Map;

/**
* 模板字段映射表 业务层
*/
public interface IFileTemplateFieldService extends IService<FileTemplateField> {


    /**
     * TEcsEsignField
     * @param templateId
     * @return
     */
    Map<String, FileTemplateField> queryParamBeanMapping(String templateId);
}