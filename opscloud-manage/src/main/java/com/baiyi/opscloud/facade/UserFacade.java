package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;

/**
 * @Author baiyi
 * @Date 2020/2/20 11:12 上午
 * @Version 1.0
 */
public interface UserFacade {

    DataTable<OcUserVO.User> queryUserPage(UserParam.PageQuery pageQuery);

    DataTable<OcUserVO.User> fuzzyQueryUserPage(UserParam.PageQuery pageQuery);

    String getRandomPassword();

    BusinessWrapper<Boolean> updateBaseUser(OcUserVO.User user);

}
