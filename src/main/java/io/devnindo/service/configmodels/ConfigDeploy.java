/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.configmodels;

import io.devnindo.service.exec.auth.JWTConfig;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.AField;
import io.devnindo.datatype.schema.Required;

/**
 *
 * @author prevy-sage
 */
public class ConfigDeploy implements DataBean
{

    @AField @Required
    Integer serverVerticleCount;

    @AField @Required
    Integer execWorkerPoolSize;

    @AField @Required
    ConfigServer serverConfig;

    @AField @Required
    JWTConfig jwtSessionConfig;


    public Integer getServerVerticleCount() {
        return serverVerticleCount;
    }

    public Integer getExecWorkerPoolSize() {
        return execWorkerPoolSize;
    }


    public ConfigServer getServerConfig() {
        return serverConfig;
    }

    public JWTConfig getJwtSessionConfig() {
        return jwtSessionConfig;
    }
}
