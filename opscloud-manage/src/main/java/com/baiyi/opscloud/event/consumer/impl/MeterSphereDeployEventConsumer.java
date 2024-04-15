package com.baiyi.opscloud.event.consumer.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.MeterSphereConfig;
import com.baiyi.opscloud.common.event.NoticeEvent;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.metersphere.driver.MeterSphereDeployHookDriver;
import com.baiyi.opscloud.datasource.metersphere.entity.LeoDeployHook;
import com.baiyi.opscloud.datasource.metersphere.entity.LeoHookResult;
import com.baiyi.opscloud.datasource.metersphere.provider.MeterSphereDeployHookProvider;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.hook.leo.LeoHook;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/5/15 17:34
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MeterSphereDeployEventConsumer extends AbstractEventConsumer<LeoHook.DeployHook> {

    private final MeterSphereDeployHookDriver meterSphereDeployHookDriver;

    private final MeterSphereDeployHookProvider meterSphereDeployHookProvider;

    private final InstanceHelper instanceHelper;

    private final DsConfigManager dsConfigManager;

    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.METER_SPHERE};

    protected DsTypeEnum[] getFilterInstanceTypes() {
        return FILTER_INSTANCE_TYPES;
    }

    protected String getTag() {
        return TagConstants.NOTICE.getTag();
    }

    protected List<DatasourceInstance> listInstance() {
        return instanceHelper.listInstance(getFilterInstanceTypes(), getTag());
    }

    @Override
    public String getEventType() {
        return BusinessTypeEnum.METER_SPHERE_DEPLOY_HOOK.name();
    }

    private MeterSphereConfig.MeterSphere buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, MeterSphereConfig.class).getMeterSphere();
    }

    @Override
    protected void onCreatedMessage(NoticeEvent<LeoHook.DeployHook> noticeEvent) {
        LeoHook.DeployHook deployHook = toEventData(noticeEvent.getMessage());
        List<DatasourceInstance> instances = listInstance();
        for (DatasourceInstance instance : instances) {
            DatasourceConfig dsConfig = dsConfigManager.getConfigById(instance.getConfigId());
            LeoHookResult.Result result;
            try {
                result = meterSphereDeployHookDriver.sendHook(buildConfig(dsConfig), deployHook);
            } catch (MalformedURLException | FeignException e) {
                result = LeoHookResult.Result.ERROR;
                result.setBody(e.getMessage());
            }
            LeoDeployHook leoDeployHook = LeoDeployHook.builder()
                    .deployId(deployHook.getId())
                    .buildId(deployHook.getBuildId())
                    .projectId(deployHook.getProjectId())
                    .name(deployHook.getName())
                    .hook(deployHook)
                    .success(result.isSuccess())
                    .code(result.getCode())
                    .body(result.getBody())
                    .build();
            meterSphereDeployHookProvider.pullAsset(instance.getId(), leoDeployHook);
        }
    }

    @Override
    protected void onUpdatedMessage(NoticeEvent<LeoHook.DeployHook> noticeEvent) {
        this.onCreatedMessage(noticeEvent);
    }

}