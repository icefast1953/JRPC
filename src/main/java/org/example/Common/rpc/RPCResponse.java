package org.example.Common.rpc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RPCResponse implements Serializable {
    // 响应的状态码
    private Integer code;
    // 响应的返回信息
    private String message;
    // 响应的具体数据
    private Object data;
    // 响应的数据类型
    private Class<?> dataType;
    // 成功时返回的响应
    public static RPCResponse success(Object data) {
        return RPCResponse.builder().code(200).message("success").data(data).dataType(data.getClass()).build();
    }
    // 失败时返回的响应
    public static RPCResponse failure() {
        return RPCResponse.builder().code(500).message("failure").build();
    }
}
