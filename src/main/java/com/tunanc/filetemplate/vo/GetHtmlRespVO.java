package com.tunanc.filetemplate.vo;

import lombok.Data;

@Data
public class GetHtmlRespVO {


    /**
     * html内容
     */
    private String htmlContent;

    /**
     * 已处理的原始模板ID
     * 经过模板填充字段处理后生成的模板ID
     */
    private String originalProcessedTemplateId;

    /**
     * 模板格式
     */
    private String templateFormat;

    /**
     * 模板名称
     */
    private String templateName;

}
