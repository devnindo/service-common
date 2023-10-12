/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.configmodels;

import io.devnindo.service.exec.auth.JwtConfig;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.Required;

/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
public class ConfigDeploy implements DataBean
{

      @Required
    Integer serverVerticleCount;

      @Required
    Integer execWorkerPoolSize;

      @Required
    ConfigServer serverConfig;

      @Required
      JwtConfig jwtSessionConfig;


    public Integer getServerVerticleCount() {
        return serverVerticleCount;
    }

    public Integer getExecWorkerPoolSize() {
        return execWorkerPoolSize;
    }


    public ConfigServer getServerConfig() {
        return serverConfig;
    }

    public JwtConfig getJwtSessionConfig() {
        return jwtSessionConfig;
    }
}
