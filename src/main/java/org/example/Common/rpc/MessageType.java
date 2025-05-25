package org.example.Common.rpc;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MessageType {
    
    REQUEST(0),

    RESPONSE(1);


    int code;

    public int getCode() {
        return code;
    }
}
