package org.example.Client.rpcClient;

import org.example.Common.rpc.RPCReqeust;
import org.example.Common.rpc.RPCResponse;

public interface RpcClient {

    public RPCResponse sendRequest(RPCReqeust rpcReqeust);
}
