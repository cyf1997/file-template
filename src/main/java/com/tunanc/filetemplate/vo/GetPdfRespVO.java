package com.tunanc.filetemplate.vo;

import lombok.Data;

@Data
public class GetPdfRespVO {

    /**
     * PDF文件ID
     */
    private String pdfFileId;

    /**
     * PDF字节数组
     * 请求参数needPdfContent为true时返回
     */
    private byte[] pdfContent;

}
