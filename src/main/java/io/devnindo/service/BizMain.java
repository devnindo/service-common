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

     public BizMain(String runtimeMode$, JsonObject identityConfig$, JsonObject deployConfig$, JsonObject runtimeConfig$)
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


    public  abstract  ActionComponent actionComponent( );

    public DeployComponent deployComponent( )
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

    private  void startDeploying( ){

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


    private static final BizMain initBizMain0(String servicePackage, String configDir, String runtimeMode)
    {
        //String identityConfigPath = configDir + "/identity.json";
        //String deployConfigPath = configDir + "/" + runtimeMode+"/" + "deploy.json";
       // String runtimeConfigPath = configDir + "/" + runtimeMode+"/" + "runtime.json";

        JsonObject identityConfig;
        JsonObject deployConfig ;
        JsonObject runtimeConfig;

        try {
            identityConfig = JsonConfigUtil.readIdentityConfig(configDir);
            deployConfig = JsonConfigUtil.readConfig(configDir, runtimeMode, "deploy");
            runtimeConfig = JsonConfigUtil.readConfig(configDir, runtimeMode, "runtime");
        }catch(IOException ioExcp)
        {
            throw new RuntimeException("Could not read configuration from: "+configDir, ioExcp);
        }

        try{

            return ClzUtil.findClzAndReflect(BizMain.class.getName(), servicePackage, runtimeMode, identityConfig, deployConfig, runtimeConfig);


        }catch (IllegalAccessException illegalExcp)
        {
            throw new RuntimeException("Could not initialize BizMain", illegalExcp);
        }

    }

    public static final String calcRuntimeMode(String[] systemArgs)
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

    private static void initPreBoot0(BizMain main$, String configDir$, String runtimeMode$) throws InvocationTargetException, IllegalAccessException, IOException {

        for(Method m : main$.getClass().getDeclaredMethods()){
            PreBoot preBootAnt = m.getDeclaredAnnotation(PreBoot.class);
            if(preBootAnt != null){
                System.out.println("pre-booting: "+m.getName());
                String configName = preBootAnt.config();
                if(configName.isEmpty())
                    m.invoke(main$);
                else {
                    JsonObject configData = JsonConfigUtil.readConfig(configDir$, runtimeMode$, configName);
                    m.invoke(main$, configData);
                }
            }

        }
    }

    public static void main(String[] args) throws IOException, IllegalAccessException {

       // Json.mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        String _configDir = System.getProperty(PARAM_SERVICE_CONFIG);
        Objects.requireNonNull(_configDir, "Configuration directory must be specified");
        
        String _servicePackage = System.getProperty(PARAM_SERVICE_PACKAGE);


        String _runtimeMode = calcRuntimeMode(args);
        System.out.println("Provided runtime mode: "+_runtimeMode);



        BizMain main =   initBizMain0(_servicePackage, _configDir, _runtimeMode);
        try {
            initPreBoot0(main, _configDir, _runtimeMode);
            main.startDeploying();

        } catch (InvocationTargetException | IOException e) {
            System.out.println("### Service Deployment failed!");
            e.printStackTrace();
        }

       // main.startDeploying(deployComponent, runtimeConfig);
       // BizComponent com
       
    }
    
}
