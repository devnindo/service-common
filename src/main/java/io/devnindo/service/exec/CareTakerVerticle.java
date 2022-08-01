/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.service.exec;
   
import io.devnindo.service.configmodels.ParamService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.devnindo.core.json.JsonObject;

/**
 *
 * @author prevy-sage
 */
public class CareTakerVerticle extends AbstractVerticle{
 
    BizManagerApi managerClient;
    BizLogalyzerApi loggerClient;
    
    
    
  //  @Inject
    public CareTakerVerticle(BizManagerApi managerClient$, BizLogalyzerApi loggerClient$)
    {
         managerClient = managerClient$;
         loggerClient = loggerClient$;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
         vertx.eventBus().consumer(ParamService.BIZLOG_QUEUE, this::handleBizLogMessage);
        startPromise.complete();
    }
    
   /*
    public void handleExceptionMessage(Message<JsonObject> exceptionMsg)
    {

        managerClient.logError(exceptionMsg.body())
                .subscribe(js -> exceptionMsg.reply(js));



    }*/
    
    
    public void handleBizLogMessage(Message<JsonObject> bizLogMsg)
    {
        
        loggerClient.logActionData(bizLogMsg.body())
                .subscribe( );
       
    }
    
    public void collectAndLogHealth(){
        throw new UnsupportedOperationException("Pending implementation");
    }
}
