/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.testmains;

import io.devnindo.service.deploy.base.PreBoot;

import java.lang.reflect.Method;

/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */

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
