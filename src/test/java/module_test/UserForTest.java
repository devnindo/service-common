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
package module_test;

import io.devnindo.service.exec.auth.BizUser;
import io.devnindo.service.exec.auth.BizUserBuilder;

public class UserForTest
{

    public static final BizUser seniorRashed(){
        return BizUserBuilder.init()
                .userId(15L)
                .prefName("Rashed")
                .role("senior_editor")
                .domain("elearning")
                .channel("lojens")
                .build();
    }

    public static final BizUser juniorFuad(){
        return BizUserBuilder.init()
                .userId(14L)
                .prefName("Kawsar")
                .role("editor")
                .domain("elearning")
                .channel("lojens")
                .build();
    }
}
