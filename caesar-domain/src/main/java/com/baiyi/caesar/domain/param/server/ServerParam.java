package com.baiyi.caesar.domain.param.server;

import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.param.PageParam;
import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/21 4:48 下午
 * @Version 1.0
 */
public class ServerParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class ServerPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "服务器名")
        private String name;

        @ApiModelProperty(value = "查询ip")
        private String queryIp;

        @ApiModelProperty(value = "服务器组id")
        private Integer serverGroupId;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "状态")
        private Integer serverStatus;

        @ApiModelProperty(value = "标签id")
        private Integer tagId;

        private final int businessType = BusinessTypeEnum.SERVER.getType();

        private Boolean extend;

    }

}