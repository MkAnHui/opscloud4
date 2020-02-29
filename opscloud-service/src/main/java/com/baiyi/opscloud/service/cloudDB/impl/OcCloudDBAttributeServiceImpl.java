package com.baiyi.opscloud.service.cloudDB.impl;

import com.baiyi.opscloud.domain.generator.OcCloudDbAttribute;
import com.baiyi.opscloud.mapper.OcCloudDbAttributeMapper;
import com.baiyi.opscloud.service.cloudDB.OcCloudDBAttributeService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/29 8:20 下午
 * @Version 1.0
 */
@Service
public class OcCloudDBAttributeServiceImpl implements OcCloudDBAttributeService {

    @Resource
    private OcCloudDbAttributeMapper ocCloudDbAttributeMapper;

    @Override
    public OcCloudDbAttribute queryOcCloudDbAttributeByUniqueKey(int cloudDbId, String attributeName) {
        Example example = new Example(OcCloudDbAttribute.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cloudDbId", cloudDbId);
        criteria.andEqualTo("attributeName", attributeName);
        return ocCloudDbAttributeMapper.selectOneByExample(example);
    }

    @Override
    public void addOcCloudDbAttribute(OcCloudDbAttribute ocCloudDbAttribute) {
        ocCloudDbAttributeMapper.insert(ocCloudDbAttribute);
    }

    @Override
    public void updateOcCloudDbAttribute(OcCloudDbAttribute ocCloudDbAttribute) {
        ocCloudDbAttributeMapper.updateByPrimaryKey(ocCloudDbAttribute);
    }

}