package com.tunanc.filetemplate.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Data
public class GetHtmlReqVO {


    @NotEmpty(message = "模板代码不能为空")
    private String templateCode;

    /**
     *  合同数据
     */
    private Map<String, Object> params;

}
