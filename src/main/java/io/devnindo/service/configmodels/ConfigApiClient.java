/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.configmodels;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.AField;

/**
 *
 * @author prevy-sage
 */
public class ConfigApiClient implements DataBean{
     
    String host;
    
     
    Integer port;
    
     
    Boolean sslEnabled;
    
     
    String apiRoot;

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public Boolean getSslEnabled() {
        return sslEnabled;
    }

    public String getApiRoot() {
        return apiRoot;
    }
    
    
}
