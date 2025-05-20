package org.example.Common.rpc;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RPCReqeust implements Serializable {
    // 要请求的接口名称
    private String interfaceName;
    // 要请求的方法名称
    private String methodName;
    // 参数列表
    private Object[] params;
    // 参数的类型
    private Class<?>[] paramsType;
}
