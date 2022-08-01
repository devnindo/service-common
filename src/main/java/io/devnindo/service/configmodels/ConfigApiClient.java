/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.configmodels;

import io.devnindo.core.schema.DataBean;
import io.devnindo.core.schema.AField;

/**
 *
 * @author prevy-sage
 */
public class ConfigApiClient implements DataBean{
    @AField
    String host;
    
    @AField
    Integer port;
    
    @AField
    Boolean sslEnabled;
    
    @AField
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
