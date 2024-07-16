package com.tunanc.filetemplate.vo;


import com.tunanc.filetemplate.entity.FileTemplate;
import com.tunanc.filetemplate.entity.FileTemplateField;
import com.tunanc.filetemplate.entity.FileTemplateScript;
import lombok.Data;

import java.util.Map;


@Data
public class FileTemplateDetail {

    /**
     * 合同模板
     */
    private FileTemplate fileTemplate;

    /**
     * 模板脚本TEcsEsignTemplateScript
     */
    private FileTemplateScript templateScript;

    /**
     * 模板参数map TEcsEsignField
     */
    private Map<String, FileTemplateField> paramMap;
}
