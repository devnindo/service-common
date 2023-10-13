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
package io.devnindo.service.testmains;

import io.devnindo.service.deploy.base.PreBoot;

import java.lang.reflect.Method;


class  Base{

}

class Extended extends Base{

    @PreBoot
    public void initTimer(){

    }

    public void noBoot(){}
}

public class SampleMain {


    public static void main(String... args)
    {

        Base base = new Extended();

        for(Method m : base.getClass().getDeclaredMethods()){
            PreBoot preBootAnt = m.getDeclaredAnnotation(PreBoot.class);
            if(preBootAnt != null){
                System.out.println("prebooting: "+m.getName()+" with config: "+preBootAnt.config());
            }

        }
    }
    
     
    
}
