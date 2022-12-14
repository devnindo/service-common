/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service;

import io.devnindo.service.deploy.RuntimeMode;
import io.devnindo.service.deploy.base.BaseComponent;
import io.devnindo.service.deploy.base.BaseModule;
import io.devnindo.service.deploy.base.DaggerBaseComponent;
import io.devnindo.service.deploy.base.PreBoot;
import io.devnindo.service.deploy.components.*;
import io.devnindo.service.deploy.dev.DaggerDevDeployComponent;
import io.devnindo.service.deploy.dev.DevDeployConfigModule;
import io.devnindo.service.deploy.production.DaggerProDeployComponent;
import io.devnindo.service.deploy.production.ProDeployConfigModule;
import io.devnindo.service.util.JsonConfigUtil;
import io.devnindo.datatype.util.ClzUtil;
import io.devnindo.datatype.json.JsonObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author prevy-sage
 */
 


public abstract class BizMain {


    public final BaseComponent baseComponent;

    protected final JsonObject identityConfig;
    protected final JsonObject deployConfig;
    protected final JsonObject runtimeConfig;
    protected final RuntimeMode runtimeMode;

    // exposed through a public static function with no setter
    private static BizMain INSTANCE;

     public BizMain(RuntimeMode runtimeMode$,   JsonObject identityConfig$, JsonObject deployConfig$, JsonObject runtimeConfig$)
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


    public abstract  ActionComponent actionComponent( );

    protected DeployComponent deployComponent( )
    {
        if(RuntimeMode.dev.equals(runtimeMode))
            return DaggerDevDeployComponent
                    .builder()
                    .devDeployConfigModule(new DevDeployConfigModule(deployConfig))
                    .baseDependency(baseComponent)
                    .build();
        else
            return DaggerProDeployComponent
                    .builder()
                    .proDeployConfigModule(new ProDeployConfigModule(deployConfig))
                    .baseDependency(baseComponent)
                    .build();
    }



    public static final RuntimeMode runtimeMode(){
        return INSTANCE.runtimeMode;
    }

    public static final BaseComponent baseComponent(){
        return INSTANCE.baseComponent;
    }


    public static final BizMain init(RuntimeMode runtimeMode)
            throws IllegalAccessException, IOException
    {
        String _configDir = "config";

        JsonObject identityConfig;
        JsonObject deployConfig ;
        JsonObject runtimeConfig;

        identityConfig = JsonConfigUtil.readIdentityConfig(_configDir);
        deployConfig = JsonConfigUtil.readConfig(_configDir, runtimeMode, "deploy");
        runtimeConfig = JsonConfigUtil.readConfig(_configDir, runtimeMode, "runtime");


        return ClzUtil.findClzAndReflect(BizMain.class.getName(), runtimeMode, _configDir, identityConfig, deployConfig, runtimeConfig);


    }

    private  void startDeploying0( ){

        ActionComponent _actionComponent;
        _actionComponent = actionComponent();

        DeployComponent _deployComponent = deployComponent();

        BizComponent bizComponent = DaggerBizComponent.builder()
                .identityConfigModule(new IdentityConfigModule(identityConfig))
                .deployComponent(_deployComponent)
                .actionComponent(_actionComponent)
                .build();

        bizComponent.serverDeployer().deploy();

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

    private static void initPreBoot0(BizMain main$,   RuntimeMode runtimeMode$)
            throws InvocationTargetException, IllegalAccessException, IOException {

        // once done in BizMain0. Repeating is not a good practice
        String _configDir = "config";//System.getProperty(PARAM_SERVICE_CONFIG);
        //Objects.requireNonNull(_configDir, PARAM_SERVICE_CONFIG+" dir must be specified as default JVM args");

        for(Method m : main$.getClass().getDeclaredMethods()){
            PreBoot preBootAnt = m.getDeclaredAnnotation(PreBoot.class);
            if(preBootAnt != null){
                System.out.println("pre-booting: "+m.getName());
                String configName = preBootAnt.config();
                if(configName.isEmpty())
                    m.invoke(main$);
                else {
                    JsonObject configData = JsonConfigUtil.readConfig(_configDir, runtimeMode$, configName);
                    m.invoke(main$, configData);
                }
            }

        }
    }

    public static BizMain instance()
    {
        return INSTANCE;
    }

    private static final void deployService (RuntimeMode runtimeMode$){
        try {
            INSTANCE =   init( runtimeMode$);
            initPreBoot0(INSTANCE, runtimeMode$);
            INSTANCE.startDeploying0();

        } catch (IllegalAccessException | InvocationTargetException | IOException e) {
            System.out.println("# Service Deployment failed!");
            e.printStackTrace();
        }
    }
    public static void main(String[] args){


        RuntimeMode _runtimeMode = calcRuntimeMode0(args);
        System.out.println("# Provided runtime mode: "+_runtimeMode);
        deployService(_runtimeMode);


       
    }
    
}
