package io.devnindo.service.exec.action;

import io.devnindo.datatype.json.JsonArray;
import io.devnindo.datatype.json.Jsonable;;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.json.JsonObject;

import java.time.Instant;

public abstract class BizException  extends RuntimeException
        implements Jsonable

{
    protected String stepName;
    protected Instant exceptionDatime;
    protected Violation violation;
    protected   Throwable throwableCause;

    BizException(String stepName$, Violation violation$ )
    {
        super(null, null, false, false);
        throwableCause = null;
        violation = violation$;
        stepName = stepName$;
        exceptionDatime = Instant.now();
    }

    BizException(String stepName$, Throwable cause$)
    {
        super(null, null, false, false);
        violation = null;
        throwableCause = cause$;
        stepName = stepName$;
        exceptionDatime = Instant.now();
    }

    @Override
    public synchronized Throwable getCause() {
        return throwableCause;
    }

    public String stepName(){ return this.stepName;}
    public Instant exceptionDatime(){return  this.exceptionDatime; }
    public abstract boolean dueToViolation();
    public abstract boolean dueToException();
    public abstract Violation violation();
    public abstract Throwable throwableCause();

    public static final BizException onViolation(String stepName, Violation violation)
    {
        return new ViolationCause(stepName, violation);
    }

    public static final BizException onException(String stepName$, Throwable cause$)
    {
        return new ThrowableCause(stepName$, cause$);
    }



    static final class  ViolationCause extends BizException
    {


        ViolationCause(String stepName$, Violation violation$)
        {
            super(stepName$, violation$);
        }

        @Override
        public boolean dueToViolation() {
            return true;
        }

        @Override
        public boolean dueToException() {
            return false;
        }

        @Override
        public Violation violation() {
            return violation;
        }

        @Override
        public Throwable throwableCause() {
            throw new UnsupportedOperationException("Not a throwable cause");
        }

        /*@Override
        public Throwable throwable() {
            throw new UnsupportedOperationException("Not a violation cause");
        }*/

        @Override
        public JsonObject toJson() {
            return new JsonObject().put("step", this.stepName)
                    .put("violation", violation.toJson());
        }
    }

    public static class ThrowableCause extends BizException
    {

        ThrowableCause(String stepName$, Throwable throwable$)
        {

           super(stepName$, throwable$);
           //super(stepName$, throwable$);
        }


        @Override
        public boolean dueToViolation() {
            return false;
        }

        @Override
        public boolean dueToException() {
            return true;
        }

        @Override
        public Violation violation() {
            throw new UnsupportedOperationException("Not a throwableCause cause");
        }

        @Override
        public Throwable throwableCause() {
            return throwableCause;
        }

        @Override
        public JsonObject toJson() {


            Throwable excp = this.throwableCause;
            JsonArray causeList = new JsonArray();
            do{
              JsonObject aStack = new JsonObject();
              aStack.put("message", excp.getMessage());
              StackTraceElement traceElm = excp.getStackTrace()[0];
                String className = traceElm.getClassName();
                className = className.substring(className.lastIndexOf(".")+1);
              aStack.put("ref", className+":"+traceElm.getMethodName()+"::"+traceElm.getLineNumber());
              causeList.add(aStack);
            }while ( ( excp = excp.getCause()) != null);

            return new JsonObject()
                    .put("action_step", this.stepName)
                    .put("error_datime", this.exceptionDatime)
                    .put("error_cause", causeList);
        }
    }
}
