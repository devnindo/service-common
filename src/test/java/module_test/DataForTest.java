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
