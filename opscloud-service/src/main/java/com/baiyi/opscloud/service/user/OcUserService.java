package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.OcUser;
import com.baiyi.opscloud.domain.param.user.UserParam;

/**
 * @Author baiyi
 * @Date 2020/1/3 6:51 下午
 * @Version 1.0
 */
public interface OcUserService {

    void addOcUser(OcUser ocUser);

    void updateOcUser(OcUser ocUser);

    void updateBaseOcUser(OcUser ocUser);

    OcUser queryOcUserById(Integer id);

    OcUser queryOcUserByUsername(String username);

    void delOcUserByUsername(String username);

    DataTable<OcUser> queryOcUserByParam(UserParam.PageQuery pageQuery);

    DataTable<OcUser> fuzzyQueryUserByParam(UserParam.PageQuery pageQuery);

}
