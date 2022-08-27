/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.deploy.components;

import io.devnindo.service.auth.BizSessionHandler;
import io.devnindo.service.configmodels.ConfigDeploy;
import io.devnindo.service.exec.BizLogalyzerApi;
import io.devnindo.service.exec.BizManagerApi;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.client.WebClient;

/**
 *
 * @author prevy-sage
 */

public interface DeployComponent 
{
    BizSessionHandler sessionHandler();
    BizManagerApi managerClient();
    BizLogalyzerApi logalyzerClient();

    Vertx vertx();
    WebClient webClient();
    ConfigDeploy deployConfig();
    
}
