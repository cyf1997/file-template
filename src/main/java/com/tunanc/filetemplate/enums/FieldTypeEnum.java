package com.tunanc.filetemplate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件类型枚举类
 */
@AllArgsConstructor
public enum FieldTypeEnum {

    /**
     *
     * 字符串
     */
    STRING("string","字符串"),
    /**
     *
     * 列表
     */
    LIST("list","列表"),
    /**
     *
     * 表格
     */
    TABLE("table","表格");






    @Getter
    private String value;

    @Getter
    private String name;
}
