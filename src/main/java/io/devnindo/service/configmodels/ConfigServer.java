/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.configmodels;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.AField;
import io.devnindo.datatype.schema.Required;

/**
 *
 * @author prevy-sage
 */
public class ConfigServer implements DataBean{
    
    @AField @Required
    Integer port;
    
    @AField @Required
    Boolean sslEnabled;
    
    @AField @Required
    String sslCertLocation;
    
    @AField @Required
    String sslKeyLocation;

    public Integer getPort() {
        return port;
    }

    public Boolean getSslEnabled() {
        return sslEnabled;
    }

    public String getSslCertLocation() {
        return sslCertLocation;
    }

    public String getSslKeyLocation() {
        return sslKeyLocation;
    }
    
    
}
