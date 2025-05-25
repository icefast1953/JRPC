package org.example.Common.rpc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
