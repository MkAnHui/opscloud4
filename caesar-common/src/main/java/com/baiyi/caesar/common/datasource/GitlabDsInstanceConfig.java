package com.baiyi.caesar.common.datasource;

import com.baiyi.caesar.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.DsGitlabConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/6/21 4:57 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GitlabDsInstanceConfig extends BaseDsInstanceConfig {

    private DsGitlabConfig.Gitlab gitlab;
}

