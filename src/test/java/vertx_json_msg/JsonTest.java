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
package vertx_json_msg;

import io.devnindo.datatype.json.*;
import io.devnindo.service.util.JsonArrayMessageCodec;
import io.devnindo.service.util.JsonObjectMessageCodec;
import io.vertx.core.Vertx;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;


@Disabled
class JsonTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    @Test
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
