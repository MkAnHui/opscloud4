package com.baiyi.opscloud.service.template.impl;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTemplate;
import com.baiyi.opscloud.domain.param.template.BusinessTemplateParam;
import com.baiyi.opscloud.mapper.opscloud.BusinessTemplateMapper;
import com.baiyi.opscloud.service.template.BusinessTemplateService;
import com.baiyi.opscloud.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/6 11:03 AM
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class BusinessTemplateServiceImpl implements BusinessTemplateService {

    private final BusinessTemplateMapper businessTemplateMapper;

    @Override
    public DataTable<BusinessTemplate> queryPageByParam(BusinessTemplateParam.BusinessTemplatePageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(BusinessTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getQueryName()))
            criteria.andLike("name", SQLUtil.toLike(pageQuery.getQueryName()));
        if (IdUtil.isNotEmpty(pageQuery.getEnvType())) criteria.andEqualTo("envType", pageQuery.getEnvType());
        List<BusinessTemplate> data = businessTemplateMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public void add(BusinessTemplate businessTemplate) {
        businessTemplateMapper.insert(businessTemplate);
    }

    @Override
    public void update(BusinessTemplate businessTemplate) {
        businessTemplateMapper.updateByPrimaryKey(businessTemplate);
    }

    @Override
    public BusinessTemplate getById(Integer id) {
        return businessTemplateMapper.selectByPrimaryKey(id);
    }

}