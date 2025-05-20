package org.example.Common.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {//实现serializable接口，表示可以序列化
    private Integer id;
    private String userName;
    private Boolean sex;  //true表示男，false表示女
}
