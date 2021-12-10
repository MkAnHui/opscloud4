package com.baiyi.opscloud.datasource.aliyun.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.aliyun.convert.RamAssetConvert;
import com.baiyi.opscloud.datasource.aliyun.ram.drive.AliyunRamPolicyDrive;
import com.baiyi.opscloud.datasource.aliyun.ram.drive.AliyunRamUserDrive;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamPolicy;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamUser;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_RAM_POLICY;

/**
 * @Author 修远
 * @Date 2021/7/2 8:15 下午
 * @Since 1.0
 */

@Component
public class AliyunRamPolicyProvider extends AbstractAssetRelationProvider<RamPolicy.Policy, RamUser.User> {

    @Resource
    private AliyunRamPolicyDrive aliyunRamPolicyDrive;

    @Resource
    private AliyunRamUserDrive aliyunRamUserDrive;

    @Resource
    private AliyunRamPolicyProvider aliyunRamPolicyProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_RAM_POLICY, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, RamPolicy.Policy entity) {
        return RamAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getAssetId(), asset.getAssetId()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey(), asset.getAssetKey()))
            return false;
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        return true;
    }

    @Override
    protected List<RamPolicy.Policy> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<RamPolicy.Policy> entities = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> entities.addAll(aliyunRamPolicyDrive.listPolicies(regionId, aliyun)));
        return entities;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.RAM_POLICY.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunRamPolicyProvider);
    }

    @Override
    protected List<RamPolicy.Policy> listEntities(DsInstanceContext dsInstanceContext, RamUser.User target) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        return aliyunRamUserDrive.listPoliciesForUser(aliyun.getRegionId(), aliyun, target.getUserName());
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.RAM_USER.name();
    }
}
