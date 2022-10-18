package io.devnindo.service.exec.action;

import io.devnindo.service.exec.auth.BizAuth;
import io.devnindo.service.exec.action.request.BizRequest;
import io.devnindo.service.exec.auth.BizUser;
import io.devnindo.service.exec.action.response.BizResponse;
import io.devnindo.datatype.schema.BeanValidator;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.reactivex.rxjava3.core.Single;


import java.util.Objects;

public abstract class BizAction<T extends DataBean> {

    public final Identity identity;
    //public final BizStep<BizRequest, ActionRequest<T>> accessStep;
    private final BeanValidator<T> validator;
    private final BizAuth accessAuth;
    protected String step;

    public BizAction(BeanValidator<T> validator$, BizAuth accessAuth$)
    {

        identity = Identity.createFrom(this.getClass()).right();
        validator = validator$;
        accessAuth = accessAuth$;


    }

    protected final void $step(String step$)
    {
        this.step = step$;
    }

    /**
     *  doBiz is a default Rx-complaint biz action that has
     *  reqData and bizUse as argument. BlockingBizAction simply provides an
     *  implementation with another implementable function: doBlockingBiz
     * */

    protected abstract Single<BizResponse> doBiz(T reqData, BizUser bizUser);


    public Single<BizResponse> executeOn(BizRequest request$)
    {


        this.$step(CommonSteps.ACTION_ACCESS);
        Either<Violation, T> eitherT = validator.apply(request$.reqData);
        if(eitherT.isLeft())
            return  Single.just(BizResponse.failed(eitherT.left()));

        Either<Violation, Void> accessEither = accessAuth.checkAccess(request$.bizUser);
        if(accessEither.isLeft())
            return Single.just(BizResponse.failed(accessEither.left()));

        this.$step("BIZ_STEP");
        BizUser bizUser = request$.bizUser;
        T reqData = eitherT.right();
        return this.doBiz(reqData, bizUser)
                    .onErrorResumeNext(this::onErrorResponse0);


    }

    private Single<BizResponse> onErrorResponse0(Throwable throwable$)
    {

        // Even though Blocking BizException directly use BizResponse to reply
        // Single response; following throwable checking is required because
        // BizAction is a default rx action and failing logic uses exception
        // to pass control downstream.
        if(throwable$ instanceof BizException)
        {
            BizException bizException = (BizException)throwable$;
            if(bizException.dueToViolation())
                return Single.just(BizResponse.failed(bizException.violation()));
            else
                return Single.error(bizException);
        }
        else return Single.error(BizException.onException(this.step, throwable$));
    }



    public static class Identity {


        public final String actionID; // [module.actionType.simpleName]
        public final String actionType; // [command, query]
        public final String execType; // [blocking, fibered, rx]
        public final String moduleName;



        public interface ExeTypes
        {
            String UNKNOWN = "unknown";
            String REACTIVE = "reactive";
            String FIBERED = "fibered";
            String BLOCKING = "blocking";
        }

        private  Identity(String actionID$, String type$, String execType$, String moduleName$)
        {
            actionID = actionID$;
            actionType = type$;
            moduleName = moduleName$;
            execType = execType$;

        }

        public boolean isReactive()
        {
            return  ExeTypes.REACTIVE.equals(execType);
        }

        public boolean isBlocking()
        {
            return  ExeTypes.BLOCKING.equals(execType);
        }

        public boolean isFibered()
        {
            return   ExeTypes.FIBERED.equals(execType);
        }

        public static Either<String,   Identity> createFrom(String reqName)
        {
            return createFrom0(reqName,   ExeTypes.UNKNOWN);
        }
        public static Either<String,  Identity> createFrom0(String reqName$, String execType$)
        {
            String moduleName;
            String actionType;
            String actionID;

            String[] splits = reqName$.split("\\.");
            int splitCount = splits.length;
            if(splitCount != 3)
                return Either.left("Action's package resolution format: module.{coomand, query}.constraint");

            moduleName = splits[0];
            actionType = splits[1];
            actionID = reqName$;

            if(! "command".equals(actionType) && ! "query".equals(actionType))
                return Either.left("Allowed types are: {command, query}");

            return Either.right(new Identity(actionID, actionType, execType$, moduleName));
        }

        public static Either<String,  Identity> createFrom(Class<? extends  BizAction> actionClz$){
            //String simpleName = actionClz$.getSimpleName();
            String execType =  ExeTypes.UNKNOWN;
            String clzName = actionClz$.getName();

            String[] splits = clzName.split("\\.");
            String moduleName;
            String actionType;
            String actionName;

            //tring fullName = clzName.substring(actionsPackagePath.length());
            if(BlockingBizAction.class.isAssignableFrom(actionClz$))
            {
                execType =   ExeTypes.BLOCKING;
            }
            else execType =  ExeTypes.REACTIVE;

            int partIdx = splits.length - 1;
            actionName = splits[partIdx];
            partIdx--;
            actionType = splits[partIdx];
            partIdx--;
            moduleName = splits[partIdx];


            String actionId = moduleName + "." + actionType + "." + actionName;
            return Either.right(new  Identity(actionId, actionType, execType, moduleName));
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null) return false;
            if(obj instanceof String)
            {
                return obj.equals(this.actionID);
            }
            if(obj instanceof  Identity == false)
                return false;
            Identity other = (Identity)obj;
            return this.actionID.equals(other.actionID);
        }

        @Override
        public int hashCode() {
            return Objects.hash(actionID);
        }
    }

}
