package io.devnindo.service.configmodels;


import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.AField;

public class ServiceIdentity implements DataBean {
     
    String id;

     
    String version;
    /**
     * A shared secret for all the services(biz and merged) alike to identify themselves
     * **/
     
    String secret;

    /*
    *  type {biz_service, data_service}
    *  biz_service act as an api_gateway composing one or more data_service
    * */
     
    String type;

    public String getId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }

    public String getType() {
        return type;
    }

    public String getVersion(){
        return version;
    }
}
