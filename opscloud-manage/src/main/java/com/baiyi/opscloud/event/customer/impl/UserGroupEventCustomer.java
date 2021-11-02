package com.baiyi.opscloud.event.customer.impl;

import com.baiyi.opscloud.datasource.manager.DsAccountGroupManager;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.event.NoticeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/11/1 9:52 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserGroupEventCustomer extends AbstractEventConsumer<UserGroup> {

    private final DsAccountGroupManager dsAccountGroupManager;

    @Override
    public String getEventType() {
        return BusinessTypeEnum.USERGROUP.name();
    }

    @Override
    protected void onCreateMessage(NoticeEvent noticeEvent) {
        UserGroup eventData = toEventData(noticeEvent.getMessage());
        dsAccountGroupManager.create(eventData);
    }

    @Override
    protected void onUpdateMessage(NoticeEvent noticeEvent) {
        UserGroup eventData = toEventData(noticeEvent.getMessage());
        dsAccountGroupManager.update(eventData);
    }

    @Override
    protected void onDeleteMessage(NoticeEvent noticeEvent) {
        UserGroup eventData = toEventData(noticeEvent.getMessage());
        dsAccountGroupManager.delete(eventData);
    }

}
