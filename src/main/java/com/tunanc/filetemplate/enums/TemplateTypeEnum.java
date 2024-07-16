package com.tunanc.filetemplate.enums;

import com.tunanc.filetemplate.constant.TemplateConstant;
import lombok.AllArgsConstructor;

/**
 * 文件类型枚举类
 */
@AllArgsConstructor
public enum TemplateTypeEnum {

    /**
     *
     * 字符串
     */
    WORD(TemplateConstant.TEMPLATE_TYPE_WORD,"docx"),
    /**
     *
     * 列表
     */
    HTML(TemplateConstant.TEMPLATE_TYPE_HTML,"html"),


    /**
     *
     * PDF模板
     */
    PDF(TemplateConstant.TEMPLATE_TYPE_HTML,"pdf");

    ;







    private String key;
    private String suffix;



    public String key(){
        return this.key;
    }
    public String suffix(){
        return this.suffix;
    }


    /**
     * 根据key查询val
     *
     * @return
     */
    public static String getSuffixByKey(String key) {
        for (TemplateTypeEnum id : TemplateTypeEnum.values()) {
            if (id.key.equals(key)) {
                return id.suffix();
            }
        }
        return null;
    }

}
