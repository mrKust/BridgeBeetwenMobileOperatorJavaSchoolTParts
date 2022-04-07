package com.school.aspects;

import com.school.ServerSideEndpoint;
import com.school.bean.ejb.TariffsInfo;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class UpdateSender {

    @Inject
    TariffsInfo tariffsInfo;

    @Inject
    ServerSideEndpoint serverSideEndpoint;

    @AroundInvoke
    public Object sendUpdate(InvocationContext context) throws Exception {
        Object result = context.proceed();
        serverSideEndpoint.send(tariffsInfo.getLastestUpdate());
        return result;
    }
}
