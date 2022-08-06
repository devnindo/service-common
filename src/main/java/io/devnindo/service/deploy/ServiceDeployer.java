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
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.devnindo.datatype.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import java.util.function.Supplier;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Set;
import javax.inject.Inject;

/**
 *
 * @author prevy-sage
 */
public class ServiceDeployer {

   // final ServiceModule module;
    final ServiceIdentity serviceIdentity;
    final ConfigDeploy deployConfig;
    final BizExecutor executor;
    final Vertx vertx;
    final BizLogalyzerApi logalyzerApi;
    final BizManagerApi managerApi;
    
    @Inject
    public ServiceDeployer(Vertx vertx$,
                           BizExecutor executor$,
                           ServiceIdentity identity$,
                           ConfigDeploy deployConfig$,
                           BizLogalyzerApi logalyzerApi$,
                           BizManagerApi managerApi$
        ) 
    {
        vertx = vertx$;
        executor = executor$;
        serviceIdentity = identity$;
        deployConfig = deployConfig$;
        this.logalyzerApi = logalyzerApi$;
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
                                            
        /*DeploymentOptions blockingVerDepOps = new DeploymentOptions()
                                        .setInstances(deployConfig.getBlockingExecCount())
                                        .setWorkerPoolSize(deployConfig.getExecWorkerPoolSize());
        */
        /*DeploymentOptions asyncVerDepOps = new DeploymentOptions()
                                        .setInstances(deployConfig.getExecVerticleCount()) ;*/
        
         
//        DeploymentOptions fiberedVerDepOps = new DeploymentOptions()
//                .setInstances(deployConfig.getFiberedExecCount());
         
        DeploymentOptions careTakerDepOps = new DeploymentOptions().setInstances(1);
         
         final Boolean enableSSL = false; //ServerParams.SSL_ENABLED.of(config$);
       
        Supplier<Verticle> serverSupplier = ()-> new ServerVerticle(executor, deployConfig.getServerConfig());
        Supplier<Verticle> careTakerSupplier = () -> new CareTakerVerticle(managerApi, logalyzerApi);

        // Supplier<Verticle> execSupplier = () -> new BlockingExecVerticle(executor);
       // Supplier<Verticle> execSupplier = () -> new ExecVerticle(executor);
       // Supplier<Verticle> fiberedExecSupplier = () -> new FiberedExecVerticle(executor);
        //$currentStep("SERVER_VERTICLE_DEPLOY");
        
        JsonObject discoveryData = serviceDiscoveryData(serviceIdentity, deployConfig.toJson());

        Single<String> serverDeploy = vertx.rxDeployVerticle(serverSupplier, serverDepOps);
        Single<String> careTakerDeploy = vertx.rxDeployVerticle(careTakerSupplier, careTakerDepOps);

        //Single<String> blockingExecDeploy = vertx.rxDeployVerticle(execSupplier, blockingVerDepOps);
        //Single<String> execDeploy = vertx.rxDeployVerticle(execSupplier, asyncVerDepOps);
        // Single<String> fiberedExecDeploy = vertx.rxDeployVerticle(fiberedExecSupplier, fiberedVerDepOps);
        //Single<JsonObject> managerRegister = managerApi.registerService(discoveryData);
        
        Disposable disposable = Single.zip(serverDeploy,  careTakerDeploy,
                (srvDeployID, careTakerDeployID)->{

            return discoveryData;
        })
            .subscribe( managerApi::registerService
                //deployInfo -> System.out.println(deployInfo.encodePrettily()),
                //error -> {error.printStackTrace(System.err);}
                
        );
  
    }
     
    
     
    
    
     
    
}
