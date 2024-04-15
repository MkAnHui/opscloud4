package com.baiyi.opscloud.leo.handler.deploy.strategy.notification;

import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.handler.deploy.strategy.notification.base.EndNotificationStrategy;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/12/16 14:26
 * @Version 1.0
 */
@Component
public class EndNotificationWithRedeployStrategy extends EndNotificationStrategy {

    private static final String LEO_REDEPLOY_END = "LEO_REDEPLOY_END";

    @Override
    protected Map<String, Object> buildContentMap(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        return Maps.newHashMap();
    }

    @Override
    public String getDeployType() {
        return DeployTypeConstants.REDEPLOY.name();
    }

    @Override
    protected String getMessageKey() {
        return LEO_REDEPLOY_END;
    }

}