package com.tunanc.filetemplate.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class HandleParamsReqVO {

    @NotEmpty(message = "模板编号不能为空")
    private String templateCode;

    @NotNull(message = "参数不能为空")
    private Map<String, Object> bizParamsMap;
}
