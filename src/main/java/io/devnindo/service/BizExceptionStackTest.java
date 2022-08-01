package io.devnindo.service;

import io.devnindo.service.exec.action.BizException;
import io.devnindo.core.json.JsonObject;

import java.io.IOException;

public class BizExceptionStackTest {
    public static void stackOne(){
        stackTwo();
    }

    public static void stackTwo(){
        try{
            stackThree();
        }catch(Throwable error)
        {
            throw BizException.onException("STACK-TWO", error);
        }
    }

    public static void stackThree(){
        stackFour();
    }

    public static void stackFour(){
            stackFive();
    }

    public static void stackFive(){

        try {
            stackSix();
        } catch (IOException e) {
            throw new RuntimeException("Holla Fucking Exception", e);
        }
    }

    public static void stackSix() throws IOException{
        /*StackTraceElement[] elmArr = Thread.currentThread().getStackTrace();
        for(StackTraceElement elm : elmArr){
            System.out.println(elm.getMethodName()+" line: "+elm.getLineNumber());
        }*/
        throw new IOException("Holla IO Exception");
    }

    public static void printStackTrace(StackTraceElement[] elmArr)
    {
        //elmArr = Thread.currentThread().getStackTrace();
        for(StackTraceElement elm : elmArr){
            String className = elm.getClassName();
            className = className.substring(className.lastIndexOf("."));
            System.out.println("\t at " + className+" method "+elm.getMethodName()+":"+elm.getLineNumber());
        }
    }

    public static void main(String... args){

        JsonObject exception = new JsonObject();
        try{

            stackOne();

        }catch (Throwable error)
        {
            if(error instanceof BizException)
            {
                BizException bizException = BizException.class.cast(error);
                System.out.println(bizException.toJson().encodePrettily());
                //Throwable excp = BizException.class.cast(error).getCause();
                /*do{

                    System.out.println(excp.getMessage());
                    printStackTrace(excp.getStackTrace());
                    excp.printStackTrace();

                }while ( ( excp = excp.getCause()) != null);*/

                //excp.printStackTrace();
               // System.out.println(BizException.class.cast(error).toJson().encodePrettily());
            }
        }
    }
}
