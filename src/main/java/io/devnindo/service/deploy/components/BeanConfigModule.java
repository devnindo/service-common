/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.deploy.components;
 
import io.devnindo.datatype.schema.BeanSchema;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.json.JsonObject;
/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
public abstract class BeanConfigModule<T extends DataBean> {
 
    public final T config;
    
    public BeanConfigModule(JsonObject jsonObj$, Class<T> dataBean$)
    {
        Either<Violation, T> either = BeanSchema.of(dataBean$).apply(jsonObj$);
        if(either.isLeft())
        {
            String msg = "provided json violates schema of dataBean: "+dataBean$+"\n";
            msg += "violations: " +either.left().toJson().encodePrettily();

            throw new UnsupportedOperationException(msg);
        }
        config  = either.right();
    }
    
    
}