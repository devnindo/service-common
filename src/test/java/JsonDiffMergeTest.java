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

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.DataDiff;
import io.devnindo.datatype.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;


@Disabled
public class JsonDiffMergeTest
{
   @Test
    public void testDiffMerge()
    {

        JsonObject from = JsonUtil.tickedJsonObj("{`a` : 4, `b`: `Hello`, `c` : `Joker`, `d` : `DreckBook`}" );
        JsonObject to = JsonUtil.tickedJsonObj("{`a` : 4, `b`: `Hello`, `c` : `BAT MAN`}" );


        DataDiff<JsonObject> diff = DataDiff.jsonObjDiff(from, to);


        System.out.println("delta Json "+diff.delta.encodePrettily());
        System.out.println("merge Json "+diff.merged.encodePrettily());


        Assertions.assertEquals(4, diff.merged.fieldNames().size());
        Assertions.assertEquals(2, diff.delta.fieldNames().size());
        Assertions.assertEquals(true, diff.delta.fieldNames().containsAll(Arrays.asList("c", "d")));


    }
}
