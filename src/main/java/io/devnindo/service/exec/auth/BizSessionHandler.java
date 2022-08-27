package io.devnindo.service.exec.auth;

import io.devnindo.service.exec.action.BizStep;
import io.devnindo.service.exec.action.CommonSteps;
import io.devnindo.service.exec.action.request.BizAccessInfo;

public abstract class BizSessionHandler extends BizStep<BizAccessInfo, BizUser>
{
    @Override
    public final String  name()
    {
        return CommonSteps.SESSION_HANDLING;
    };
}
