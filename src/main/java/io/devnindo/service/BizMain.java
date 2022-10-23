/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service;

import io.devnindo.service.configmodels.RuntimeMode;
import io.devnindo.service.deploy.base.BaseComponent;
import io.devnindo.service.deploy.base.BaseModule;
import io.devnindo.service.deploy.base.DaggerBaseComponent;
import io.devnindo.service.deploy.base.PreBoot;
import io.devnindo.service.deploy.components.*;
import io.devnindo.service.deploy.dev.DaggerDevDeployComponent;
import io.devnindo.service.deploy.dev.DevDeployConfigModule;
import io.devnindo.service.deploy.production.DaggerProDeployComponent;
import io.devnindo.service.deploy.production.ProDeployConfigModule;
import io.devnindo.service.deploy.test.ActionTestExecutor;
import io.devnindo.service.util.JsonConfigUtil;
import io.devnindo.datatype.util.ClzUtil;
import io.devnindo.datatype.json.JsonObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 *
 * @author prevy-sage
 */
 


public abstract class BizMain {

    public static final String PARAM_DEV = "dev";
    public static final String PARAM_PRODUCTION = "production";
    public static final String PARAM_SERVICE_CONFIG = "service.config";
    public static final String PARAM_SERVICE_PACKAGE = "service.package";

    public final BaseComponent baseComponent;
    protected final JsonObject identityConfig;
    protected final JsonObject deployConfig;
    protected final JsonObject runtimeConfig;
    protected final String runtimeMode;
    protected final String configDir;

    // exposed through a public static function with no setter
    private static BizMain INSTANCE;

     public BizMain(String runtimeMode$, String configDir$, JsonObject identityConfig$, JsonObject deployConfig$, JsonObject runtimeConfig$)
     {
        runtimeMode = runtimeMode$;
        configDir = configDir$;
        deployConfig = deployConfig$;
        runtimeConfig = runtimeConfig$;
        identityConfig = identityConfig$;

         baseComponent = DaggerBaseComponent
                 .builder()
                 .baseModule(new BaseModule(deployConfig$))
                 .build();
     }


    protected abstract  ActionComponent actionComponent( );

    protected DeployComponent deployComponent( )
    {
        if(RuntimeMode.DEV.equals(runtimeMode))
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




    public static ActionTestExecutor initTestExec(String runtimeMode$)
    {
        try {
            INSTANCE =   initBizMain0( runtimeMode$);
            return INSTANCE.actionComponent().testExecutor();
        } catch (IllegalAccessException | IOException excp) {
            throw new RuntimeException(excp);
        }
    }

    private static final BizMain initBizMain0( String runtimeMode)
            throws IllegalAccessException, IOException
    {
        String _configDir = "config";//System.getProperty(PARAM_SERVICE_CONFIG);
       // Objects.requireNonNull(_configDir, PARAM_SERVICE_CONFIG+" dir must be specified as default JVM args");

       // String _servicePackage = System.getProperty(PARAM_SERVICE_PACKAGE);
       // Objects.requireNonNull(_servicePackage, PARAM_SERVICE_PACKAGE+" clz name must be specified as default JVM args");



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

    private static final String calcRuntimeMode0(String[] systemArgs)
    {
        String _runtimeMode;
        if(systemArgs.length == 0)
            return PARAM_DEV;
        else {
            _runtimeMode = systemArgs[0];
            if(_runtimeMode.equals(PARAM_DEV) || _runtimeMode.equals(PARAM_PRODUCTION))
                return _runtimeMode;
            else
                throw new IllegalArgumentException("Runtime mode must be of {dev, production}");

        }

    }

    private static void initPreBoot0(BizMain main$,   String runtimeMode$)
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

    public static final void deployService (String runtimeMode$){
        try {
            INSTANCE =   initBizMain0( runtimeMode$);
            initPreBoot0(INSTANCE, runtimeMode$);
            INSTANCE.startDeploying0();

        } catch (IllegalAccessException | InvocationTargetException | IOException e) {
            System.out.println("# Service Deployment failed!");
            e.printStackTrace();
        }
    }
    public static void main(String[] args){


        String _runtimeMode = calcRuntimeMode0(args);
        System.out.println("# Provided runtime mode: "+_runtimeMode);
        deployService(_runtimeMode);


       
    }
    
}
