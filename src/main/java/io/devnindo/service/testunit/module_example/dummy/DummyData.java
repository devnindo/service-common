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
package io.devnindo.service.testunit.module_example.dummy;

import io.devnindo.datatype.schema.DataBean;

public class DummyData implements DataBean
{
    String name;
    Integer sleepingTime;

    String reply;

    public String getName() {
        return name;
    }

    public Integer getSleepingTime() {
        return sleepingTime;
    }

    public String getReply() {
        return reply;
    }
}
