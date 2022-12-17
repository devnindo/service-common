/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service;

import io.devnindo.service.deploy.RuntimeMode;
import io.devnindo.service.deploy.ServiceComponentProvider;
import io.devnindo.service.deploy.base.BaseComponent;
import io.devnindo.service.deploy.base.BaseModule;
import io.devnindo.service.deploy.base.DaggerBaseComponent;
import io.devnindo.service.deploy.base.PreBoot;
import io.devnindo.service.deploy.components.*;
import io.devnindo.service.util.ServiceConfigUtil;
import io.devnindo.datatype.util.ClzUtil;
import io.devnindo.datatype.json.JsonObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author prevy-sage
 */
 
/**
 *  1. BizMain should not be abstract
 *  2. A ServiceModule interface should be introduced
 *      a. provider function for BaseComponent, DeployComponent, ActionComponent
 *      b. @PreBoot annotated function for external module
 *
 * */

public final class BizMain {


    public final BaseComponent baseComponent;

    protected final JsonObject identityConfig;
    protected final JsonObject deployConfig;
    protected final JsonObject runtimeConfig;
    protected final RuntimeMode runtimeMode;

    // exposed through a public static function with no setter
    private static BizMain INSTANCE;

     public BizMain(RuntimeMode runtimeMode$, JsonObject identityConfig$, JsonObject deployConfig$, JsonObject runtimeConfig$)
     {
        runtimeMode = runtimeMode$;
        deployConfig = deployConfig$;
        runtimeConfig = runtimeConfig$;
        identityConfig = identityConfig$;

         baseComponent = DaggerBaseComponent
                 .builder()
                 .baseModule(new BaseModule(deployConfig$))
                 .build();
     }






    public static final RuntimeMode runtimeMode(){
        return INSTANCE.runtimeMode;
    }

    public static final BaseComponent baseComponent(){
        return INSTANCE.baseComponent;
    }

    private static final BizMain init(RuntimeMode runtimeMode)
            throws  IOException
    {

        JsonObject identityConfig;
        JsonObject deployConfig ;
        JsonObject runtimeConfig;

        identityConfig = ServiceConfigUtil.readIdentityConfig();
        deployConfig = ServiceConfigUtil.readConfig(runtimeMode, "deploy");
        runtimeConfig = ServiceConfigUtil.readConfig( runtimeMode, "runtime");


        return new BizMain(runtimeMode, identityConfig, deployConfig, runtimeConfig);


    }

    private  void preBootAndDeploy0( ) throws IllegalAccessException, IOException, InvocationTargetException {

        ServiceComponentProvider componentProvider = ClzUtil.findClzAndReflect(ServiceComponentProvider.class);

        preBoot0(componentProvider);

        BizComponent bizComponent = DaggerBizComponent.builder()
                .identityConfigModule(new IdentityConfigModule(identityConfig))
                .deployComponent(componentProvider.deployComponent(deployConfig))
                .actionComponent(componentProvider.actionComponent(runtimeConfig))
                .build();

        bizComponent.serverDeployer().deploy();

    }

    private void preBoot0(ServiceComponentProvider componentProvider)
            throws InvocationTargetException, IllegalAccessException, IOException {


        for(Method m : componentProvider.getClass().getDeclaredMethods()){
            PreBoot preBootAnt = m.getDeclaredAnnotation(PreBoot.class);
            if(preBootAnt != null){
                System.out.println("pre-booting: "+m.getName());
                String configName = preBootAnt.config();
                if(configName.isEmpty())
                    m.invoke(componentProvider);
                else {
                    JsonObject configData = ServiceConfigUtil.readConfig( runtimeMode, configName);
                    m.invoke(componentProvider, configData);
                }
            }

        }
    }



    private static final RuntimeMode calcRuntimeMode0(String[] systemArgs)
    {

        if(systemArgs.length == 0)
            return RuntimeMode.dev;
        else {
            try{

               return RuntimeMode.valueOf(systemArgs[0]);

            }catch (Exception exception){
                throw new IllegalArgumentException("Runtime mode must be of {dev, production}");
            }


        }

    }





    public static void main(String[] args){


        RuntimeMode _runtimeMode = calcRuntimeMode0(args);
        System.out.println("# Provided runtime mode: "+_runtimeMode);
        try {
            INSTANCE = init( _runtimeMode);
            INSTANCE.preBootAndDeploy0();

        } catch (IllegalAccessException | InvocationTargetException | IOException e) {
            System.out.println("# Service Deployment failed!");
            e.printStackTrace();
        }


       
    }
    
}
