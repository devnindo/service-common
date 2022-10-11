/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.configmodels;

import io.devnindo.datatype.schema.AField;

/**
 *
 * @author prevy-sage
 */
public class ProductionDeployConfig extends ConfigDeploy {
    
     
    ConfigApiClient managerApi;
    


    public ConfigApiClient getManagerApi() {
        return managerApi;
    }

    
    
}
