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

import io.devnindo.datatype.json.JsonObject;

public class DataForTest
{
    public static final JsonObject module_1_data_1()
    {
        return new JsonObject("""
            {
                name : "TestModule 1 DATA 1",
                sleeping_time : 4,
                reply: "Dishting"
           }    
        """);
    }

    public static final JsonObject module_1_data_2()
    {
        return new JsonObject("""
          {
                name : "TestModule 1 DATA 2",
                sleeping_time : 2,
                reply: "Dishting Dash"
           }
        """);
    }
    public static final JsonObject module_2_data_1()
    {
        return new JsonObject("""
            {
                name : "TestModule 2 DATA 1",
                sleeping_time : 3,
                reply: "Dishting"
           }    
        """);
    }

    public static final JsonObject module_2_data_2()
    {
        return new JsonObject("""
          {
                name : "TestModule 2 DATA 2",
                sleeping_time : 1,
                reply: "Dishting Dash"
           }
        """);
    }
}
