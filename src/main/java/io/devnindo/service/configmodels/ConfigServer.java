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
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
public class ConfigServer implements DataBean{
    
    @Required
    Integer port;
    
    @Required
    Boolean sslEnabled;
    
    @Required
    String sslCertLocation;
    
    @Required
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

    public ConfigServer setPort(Integer port) {
        this.port = port;
        return this;
    }

    public ConfigServer setSslEnabled(Boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
        return this;
    }

    public ConfigServer setSslCertLocation(String sslCertLocation) {
        this.sslCertLocation = sslCertLocation;
        return this;
    }

    public ConfigServer setSslKeyLocation(String sslKeyLocation) {
        this.sslKeyLocation = sslKeyLocation;
        return this;
    }
}
