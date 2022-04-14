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

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.interceptor.Interceptors;
import javax.ws.rs.core.MediaType;

@Startup
@Singleton
public class TariffsInfo {

    @Getter
    @Setter
    private String lastestUpdate;

    @PostConstruct
    public void init() {
        updateInfo();
    }

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
        }   catch (Exception e) {
            this.lastestUpdate = "";
        }


    }
}
