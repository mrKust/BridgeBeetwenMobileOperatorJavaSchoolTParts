package com.school.bean.ejb;

import com.school.ServerSideEndpoint;
import com.school.aspects.UpdateSender;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.interceptor.Interceptors;
import javax.ws.rs.core.MediaType;

/**
 * Class store the lastest update of tariffs info, and get new, when catch notification from mq
 */
@Startup
@Singleton
public class TariffsInfo {

    private static final Logger LOG = Logger.getLogger(TariffsInfo.class);

    @Getter
    @Setter
    private String lastestUpdate;

    @PostConstruct
    public void init() {
        updateInfo();
    }

    /**
     * Method download new version of tariff info and store it
     */
    @Interceptors(UpdateSender.class)
    public void updateInfo() {
        try {
            ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

            Client client = Client.create(clientConfig);
            WebResource webResource = client.resource("http://localhost:8081/api/tariffsInfo");

            ClientResponse response = webResource
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .get(ClientResponse.class);

            this.lastestUpdate = response.getEntity(String.class);
            LOG.setLevel(Level.INFO);
            LOG.info("Store new version of tariffs info");
        }   catch (Exception e) {
            LOG.setLevel(Level.DEBUG);
            LOG.debug("Couldn't connect to server. Can not store anything");
            this.lastestUpdate = "";
        }


    }
}
