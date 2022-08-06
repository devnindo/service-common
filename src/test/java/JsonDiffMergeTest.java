
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
