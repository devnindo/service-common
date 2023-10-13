/*
 * Copyright 2023 devnindo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.devnindo.service.deploy;

import io.devnindo.service.exec.*;
import io.devnindo.service.exec.action.BizAction;
import io.devnindo.service.configmodels.ConfigDeploy;
import io.devnindo.service.configmodels.ServiceIdentity;
import io.devnindo.datatype.json.JsonArray;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.DeploymentOptions;
import io.devnindo.datatype.json.JsonObject;
import io.vertx.core.Verticle;
import io.vertx.rxjava3.core.Vertx;

import java.util.function.Supplier;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Set;
import javax.inject.Inject;

public class ServiceDeployer {

   // final ServiceModule module;
    final ServiceIdentity serviceIdentity;
    final ConfigDeploy deployConfig;
    final BizExecutor executor;
    final Vertx vertx;
    final BizManagerApi managerApi;
    
    @Inject
    public ServiceDeployer(Vertx vertx$,
                           BizExecutor executor$,
                           ServiceIdentity identity$,
                           ConfigDeploy deployConfig$,
                           BizManagerApi managerApi$
        ) 
    {
        vertx = vertx$;
        executor = executor$;
        serviceIdentity = identity$;
        deployConfig = deployConfig$;
        managerApi = managerApi$;
        
    }
    
  
 
    private  JsonObject serviceDiscoveryData(ServiceIdentity serviceIdentity$, JsonObject deployConfig$)
    {
        JsonObject discoveryData = new JsonObject();
        
        String ipAddress = "N/A" ;
        
        try(final DatagramSocket socket = new DatagramSocket())
        {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ipAddress = socket.getLocalAddress().getHostAddress();
        }catch(Exception exception)
        {
            exception.printStackTrace();
        }
        
        deployConfig$.put("ip", ipAddress);
        Set<BizAction.Identity> actionSet = executor.identitySet();
        JsonArray actionData = new JsonArray();

        actionSet.forEach(identity -> actionData.add(identity.actionID));
        
        //JsonObject managerConfig = ServiceMain.CONFIG.discoveryConfig();
        
        discoveryData.put("identity", serviceIdentity$.toJson())
                     .put("server_config", deployConfig$)
                     .put("actions", actionData);
                
               
        
        return discoveryData;
        
    }
 
    public void deploy() {
        

        DeploymentOptions serverDepOps = new DeploymentOptions()
                                            .setInstances(deployConfig.getServerVerticleCount());

        Supplier<Verticle> serverSupplier = ()-> new ServerVerticle(executor, deployConfig.getServerConfig());

        
        JsonObject discoveryData = serviceDiscoveryData(serviceIdentity, deployConfig.toJson());

        Single<String> serverDeploy = vertx.rxDeployVerticle(serverSupplier, serverDepOps);

        serverDeploy
            .doOnSuccess((str)-> managerApi.registerService(discoveryData))
            .doOnError(error->{
                System.out.println("# SERVICE DEPLOY ERROR");
                error.printStackTrace();
            })
            .subscribe();
  
    }
     
    
     
    
    
     
    
}
