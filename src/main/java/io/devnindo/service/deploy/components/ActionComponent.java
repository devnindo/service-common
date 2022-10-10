/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.deploy.components;

import io.devnindo.service.deploy.test.ActionTestExecutor;
import io.devnindo.service.exec.action.BizAction;

import javax.inject.Provider;
import java.util.Map;

/**
 *
 * @author prevy-sage
 */
public interface ActionComponent {
     //Set<BizAction> actionSet();
     public Map<Class<? extends BizAction>, Provider<BizAction>> actionMap();

     public ActionTestExecutor testExecutor();

}
