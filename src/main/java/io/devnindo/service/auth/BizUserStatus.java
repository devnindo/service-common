package io.devnindo.service.auth;

public enum BizUserStatus {

    /**
     * Requesting user is signed in devnindo from a valid client
     */
    reg_user,

    /**
     * referenced anonymous:  requesting user has not signed in devnindo
     * but was referenced by a valid client
     */
    r_anonymous,

    /**
     * global anonymous: the requesting user has neither signed
     * in nor was referenced by a valid client
     */
    g_anonymous
}
