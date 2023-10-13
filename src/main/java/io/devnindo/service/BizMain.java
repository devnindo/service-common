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
package io.devnindo.service;

import io.devnindo.service.deploy.RuntimeMode;
import io.devnindo.service.deploy.BizComponentProvider;
import io.devnindo.service.deploy.base.BaseComponent;
import io.devnindo.service.deploy.base.BaseModule;
import io.devnindo.service.deploy.base.DaggerBaseComponent;
import io.devnindo.service.deploy.base.PreBoot;
import io.devnindo.service.deploy.components.*;
import io.devnindo.service.exec.RxScheduler;
import io.devnindo.service.util.ServiceConfigUtil;
import io.devnindo.datatype.util.ClzUtil;
import io.devnindo.datatype.json.JsonObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

 

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


    private  void preBootAndDeploy0( ) throws IllegalAccessException, IOException, InvocationTargetException {

        RxScheduler.init(baseComponent.blockingScheduler(), baseComponent.asyncScheduler());
        BizComponentProvider componentProvider = ClzUtil.findClzAndReflect(BizComponentProvider.class);

        // Boot worker or other background watcher mechanism
        preBoot0(componentProvider);

        ActionComponent actionComponent = componentProvider.actionComponent(runtimeConfig, baseComponent);
        DeployComponent deployComponent;
        if(RuntimeMode.dev.equals(runtimeMode))
            deployComponent = componentProvider.devDeployComponent(deployConfig, baseComponent);
        else
            deployComponent = componentProvider.proDeployComponent(deployConfig, baseComponent);

        BizComponent bizComponent = DaggerBizComponent.builder()
                .identityConfigModule(new IdentityConfigModule(identityConfig))
                .deployComponent(deployComponent)
                .actionComponent(actionComponent)
                .build();

        bizComponent.serverDeployer().deploy();

    }

    private void preBoot0(BizComponentProvider componentProvider)
            throws InvocationTargetException, IllegalAccessException, IOException {


        for(Method m : componentProvider.getClass().getDeclaredMethods()){
            PreBoot preBootAnn = m.getDeclaredAnnotation(PreBoot.class);
            if(preBootAnn != null){
                System.out.println("pre-booting: "+m.getName());
                String configName = preBootAnn.config();
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
            JsonObject identityConfig;
            JsonObject deployConfig ;
            JsonObject runtimeConfig;

            identityConfig = ServiceConfigUtil.readIdentityConfig();
            deployConfig = ServiceConfigUtil.readConfig(_runtimeMode, "deploy");
            runtimeConfig = ServiceConfigUtil.readConfig( _runtimeMode, "runtime");
            INSTANCE = new BizMain(_runtimeMode, identityConfig, deployConfig, runtimeConfig);
            INSTANCE.preBootAndDeploy0();

        } catch (IllegalAccessException | InvocationTargetException | IOException e) {
            System.out.println("# Service Deployment failed!");
            e.printStackTrace();
        }


       
    }
    
}
