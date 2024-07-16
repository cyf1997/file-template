package com.tunanc.filetemplate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 模板脚本处理
 *
 */
@Data
@TableName("file_template_script")
public class FileTemplateScript implements Serializable {

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
     *  脚本
     */
    private String script;

}