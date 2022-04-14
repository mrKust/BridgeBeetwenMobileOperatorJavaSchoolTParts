package com.school.aspects;

import com.school.ServerSideEndpoint;
import com.school.bean.ejb.TariffsInfo;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Class send update through web socket method called
 */
public class UpdateSender {

    private static final Logger LOG = Logger.getLogger(UpdateSender.class);

    @Inject
    TariffsInfo tariffsInfo;

    @Inject
    ServerSideEndpoint serverSideEndpoint;

    /**
     * Method which send update
     * @param context context
     * @return result of method works
     * @throws Exception
     */
    @AroundInvoke
    public Object sendUpdate(InvocationContext context) throws Exception {
        Object result = context.proceed();
        serverSideEndpoint.send(tariffsInfo.getLastestUpdate());
        LOG.setLevel(Level.TRACE);
        LOG.trace("Sended update of tariffs");
        return result;
    }
}
