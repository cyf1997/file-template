package com.tunanc.filetemplate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 模板字段映射表
 */
@Data
@TableName("file_template_field")
public class FileTemplateField implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     *  模版ID
     */
    private String templateId;

    /**
     *  业务参数名
     */
    private String paramKey;

    /**
     *  模板参数名
     */
    private String templateKey;

    /**
     *  参数类型
     */
    private String paramType;

    /**
     *  参数描述
     */
    private String paramDesc;

    /**
     *  填充校验标记：0-校验，1-不校验
     */
    private String fillValidationFlg;

    /**
     * 业务参数为空时的默认值
     */
    private String defaultValue;
    /**
     *  需要替换的值
     *  <p>业务场景为需要列表形式展示的内容，此值存的是html标签内容，在代表中会根据paramType进一步处理</p>
     */
    private String replaceValue;

}