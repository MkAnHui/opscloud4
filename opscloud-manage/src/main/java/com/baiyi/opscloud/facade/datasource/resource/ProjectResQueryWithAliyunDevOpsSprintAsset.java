package com.baiyi.opscloud.facade.datasource.resource;

import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.annotation.ProjectResType;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.ProjectResTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2023/5/19 3:09 PM
 * @Since 1.0
 */

@ProjectResType(ProjectResTypeEnum.ALIYUN_DEVOPS_SPRINT)
@BusinessType(BusinessTypeEnum.ASSET)
@Component
public class ProjectResQueryWithAliyunDevOpsSprintAsset extends AbstractProjectResQueryWithAsset{
}
