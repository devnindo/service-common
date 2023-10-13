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
package io.devnindo.service.exec.auth;

import io.devnindo.service.exec.action.BizStep;
import io.devnindo.service.exec.action.CommonSteps;
import io.devnindo.service.exec.action.request.BizAccessInfo;

public abstract class BizSessionHandler extends BizStep<BizAccessInfo, BizUser>
{
    @Override
    public final String  name()
    {
        return CommonSteps.SESSION_HANDLING;
    };
}
