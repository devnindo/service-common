package io.devnindo.service.exec.auth;

public interface JwtConfigSpec
{
   String getIssuer();
   String getAlgo();
   String getSecret();
   Long getExpireInSec();
}
