package com.tunanc.filetemplate.vo;

import lombok.Data;

import java.util.Map;

@Data
public class HandleParamsRespVO {


    /**
     * 已处理的map
     */
    private Map<String, Object> handledMap;
}
