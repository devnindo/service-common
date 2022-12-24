package module_test;

import io.devnindo.datatype.json.JsonObject;

public class DataForTest
{
    public static final JsonObject dummyData_1()
    {
        return new JsonObject("""
            {
                name : "Dummy BLocker 1",
                sleeping_time : 3,
                reply: "Dishting"
           }    
        """);
    }

    public static final JsonObject dummyData_2()
    {
        return new JsonObject("""
          {
                name : "Dummy BLocker 2",
                sleeping_time : 2,
                reply: "Dishting Dash"
           }
        """);
    }

}
