package com.baiyi.opscloud.datasource.business.server.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.business.server.impl.base.AbstractZabbixHostServerHandler;
import com.baiyi.opscloud.datasource.business.server.util.HostParamUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/8/19 11:40 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixHostServerHandler extends AbstractZabbixHostServerHandler {

    @Override
    protected void doCreate(Server server) {
        ServerProperty.Server property = getBusinessProperty(server);
        doCreate(server, property);
    }

    @Override
    protected void doUpdate(Server server) {
        ServerProperty.Server property = getBusinessProperty(server);
        if (!property.enabledZabbix()) return;
        String manageIp = HostParamUtil.getManageIp(server, property);
        ZabbixHost.Host host = zabbixV5HostDriver.getByIp(configContext.get(), manageIp);
        if (host == null) {
            doCreate(server, property);
        } else {
            updateHost(server, property, host,manageIp );
        }
    }

    @Override
    protected void doDelete(Server server) {
        ServerProperty.Server property = getBusinessProperty(server);
        String manageIp = HostParamUtil.getManageIp(server, property);
        try {
            com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost.Host host = zabbixV5HostDriver.getByIp(configContext.get(), manageIp);
            if (host == null) {
                return;
            }
            zabbixV5HostDriver.deleteById(configContext.get(), host.getHostid());
            zabbixV5HostDriver.evictHostById(configContext.get(), host.getHostid());
        } catch (Exception ignored) {
        } finally {
            zabbixV5HostDriver.evictHostByIp(configContext.get(), manageIp);
        }
    }

    @Override
    protected int getBusinessResourceType() {
        return BusinessTypeEnum.SERVERGROUP.getType();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.getName();
    }

}