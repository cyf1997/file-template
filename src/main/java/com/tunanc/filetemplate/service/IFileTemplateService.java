package com.tunanc.filetemplate.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.tunanc.filetemplate.entity.FileTemplate;
import com.tunanc.filetemplate.vo.FileTemplateDetail;
import com.tunanc.filetemplate.vo.TemplateDetailVO;

import java.util.List;

/**
 * 文件模版表 业务层
 *
 */
public interface IFileTemplateService extends IService<FileTemplate> {



    /**
     * 根据模板代码获取模板详细数据
     *
     * @param templateCode 模板代码
     * @return FileTemplateDetail
     */
    FileTemplateDetail getTemplateInfoDetail(String templateCode);


}
