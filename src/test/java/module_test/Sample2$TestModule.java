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

import io.devnindo.service.testunit.BizTestModule;
import io.devnindo.service.testunit.module_example.dummy.action.DummyBlockingAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class Sample2$TestModule extends BizTestModule {



    @Test
    public void test_1(){
        forAction(DummyBlockingAction.class)
                .withUser(UserForTest.seniorRashed())
                .withData(DataForTest.module_2_data_1())
                .applyRule((bizResponse, bizException) -> {
                    assertEquals(Boolean.TRUE, bizResponse.isSuccess());
                });
    }

    @Test
    public void test_2(){
        forAction(DummyBlockingAction.class)
                .withUser(UserForTest.seniorRashed())
                .withData(DataForTest.module_2_data_2())
                .applyRule((bizResponse, bizException) -> {
                    assertEquals(Boolean.TRUE, bizResponse.isSuccess());
                });
    }
}
