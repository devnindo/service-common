package io.devnindo.service.exec.auth;


/**
 *
 * @author <a href="https://github.com/skull-sage">Rashed Alam</a>
 */
public interface JwtConfigSpec
{
   String getIssuer();
   String getAlgo();
   String getSecret();
   Long getExpireInSec();
}
