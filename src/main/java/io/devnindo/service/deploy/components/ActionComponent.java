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
package io.devnindo.service.deploy.components;

import io.devnindo.service.exec.action.BizAction;

import javax.inject.Provider;
import java.util.Map;

public interface ActionComponent {
     //Set<BizAction> actionSet();
     public Map<Class<? extends BizAction>, Provider<BizAction>> actionMap();


}
