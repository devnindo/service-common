package json;

import io.devnindo.datatype.json.*;
import io.vertx.core.Vertx;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

class JsonTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    @Test @Disabled
    void vertx_bus_transport(){
        Vertx vertx = Vertx.vertx();
        JsonObject aSample = new JsonObject();
        aSample.put("greeting", "Bonjur Bash");
        vertx.eventBus().registerDefaultCodec(JsonObject.class, new JsonObjectMessageCodec());
        vertx.eventBus().registerDefaultCodec(JsonArray.class, new JsonArrayMessageCodec());

        String QUEUE = "test.queue";

        CountDownLatch latch = new CountDownLatch(2);

        vertx.eventBus().consumer(QUEUE, h->{
            latch.countDown();
            Json body = (Json)h.body();
            System.out.println("Received " +body.encode());
        });

        vertx.eventBus().send(QUEUE, aSample);
        vertx.setTimer(3000, h->{
            JsonArray sampleArr = new JsonArray("['Disting', 420]");
            vertx.eventBus().send(QUEUE, sampleArr);
        });

        try{
            latch.await();
            vertx.close();
        }catch (InterruptedException exception){
        }
    }

}