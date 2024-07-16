package com.tunanc.filetemplate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件模版表
 */
@Data
@TableName("file_template")
public class FileTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  模版ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;


    /**
     *  文件ID
     */
    private String fileId;


    /**
     *  模版码
     */
    private String templateCode;

    /**
     *  模版类型
     */
    private String templateType;

    /**
     *  模版名称
     */
    private String templateName;

    /**
     *  模版格式
     */
    private String templateFormat;

    /**
     *  模版HTML
     */
    private String templateHtml;


    private String templateDesc;



}
