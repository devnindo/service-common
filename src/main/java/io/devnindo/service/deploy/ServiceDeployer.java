/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
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
