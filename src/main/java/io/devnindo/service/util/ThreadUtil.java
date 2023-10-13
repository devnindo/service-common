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
package io.devnindo.service.util;

public class ThreadUtil {

    public static final void sleepingBlock(int scnds){

        try {
            Thread.sleep(scnds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static final void logCurrent(String blockName$){
        Thread crt = Thread.currentThread();
        String msg = """
                       # %s runs on [%s::%s]
                     """.formatted(blockName$, crt.getName(), crt.hashCode());
        System.out.print(msg);
    }
}
