package com.baiyi.opscloud.ansible.provider;

import com.baiyi.opscloud.common.datasource.AnsibleDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsAnsibleConfig;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.type.CredentialKindEnum;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.base.common.AbstractSetDsInstanceConfigProvider;
import com.baiyi.opscloud.datasource.util.SystemEnvUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import org.springframework.stereotype.Component;

/**
 * Ansible配置文件推送(privateKey)
 * @Author baiyi
 * @Date 2021/8/16 5:24 下午
 * @Version 1.0
 */
@Component
public class AnsibleSetConfigProvider extends AbstractSetDsInstanceConfigProvider<DsAnsibleConfig.Ansible> {

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ANSIBLE.name();
    }

    @Override
    protected DsAnsibleConfig.Ansible buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, AnsibleDsInstanceConfig.class).getAnsible();
    }

    @Override
    protected void doSet(DsInstanceContext dsInstanceContext) {
        DsAnsibleConfig.Ansible ansible = buildConfig(dsInstanceContext.getDsConfig());
        // 取配置文件路径
        ansible.getPrivateKey();
        String privateKeyPath = SystemEnvUtil.renderEnvHome(ansible.getPrivateKey());
        String privateKey = getPrivateKey(dsInstanceContext);
        IOUtil.writeFile(privateKey, privateKeyPath);
    }

    private String getPrivateKey(DsInstanceContext dsInstanceContext) {
        if (dsInstanceContext.getDsConfig().getCredentialId() == null)
            throw new CommonRuntimeException("凭据没有配置！");

        //   CredentialKindEnum.SSH_USERNAME_WITH_KEY_PAIR.getKind();
        Credential credential = getCredential(dsInstanceContext.getDsConfig().getCredentialId());
        if (credential == null) {
            throw new CommonRuntimeException("凭据不存在！");
        }
        if (credential.getKind() == CredentialKindEnum.SSH_USERNAME_WITH_KEY_PAIR.getKind() ||
                credential.getKind() == CredentialKindEnum.SSH_USERNAME_WITH_PRIVATE_KEY.getKind()) {
            return stringEncryptor.decrypt(credential.getCredential());
        } else {
            throw new CommonRuntimeException("凭据类型不符！");
        }
    }


}
