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
package io.devnindo.service.configmodels;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.AField;
import io.devnindo.datatype.schema.Required;

public class ConfigServer implements DataBean{
    
    @Required
    Integer port;
    
    @Required
    Boolean sslEnabled;
    
    @Required
    String sslCertLocation;
    
    @Required
    String sslKeyLocation;

    public Integer getPort() {
        return port;
    }

    public Boolean getSslEnabled() {
        return sslEnabled;
    }

    public String getSslCertLocation() {
        return sslCertLocation;
    }

    public String getSslKeyLocation() {
        return sslKeyLocation;
    }

    public ConfigServer setPort(Integer port) {
        this.port = port;
        return this;
    }

    public ConfigServer setSslEnabled(Boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
        return this;
    }

    public ConfigServer setSslCertLocation(String sslCertLocation) {
        this.sslCertLocation = sslCertLocation;
        return this;
    }

    public ConfigServer setSslKeyLocation(String sslKeyLocation) {
        this.sslKeyLocation = sslKeyLocation;
        return this;
    }
}
