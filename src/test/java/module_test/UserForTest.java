package module_test;

import io.devnindo.service.exec.auth.BizUser;
import io.devnindo.service.exec.auth.BizUserBuilder;

public class UserForTest
{

    public static final BizUser seniorRashed(){
        return BizUserBuilder.init()
                .userId(15L)
                .prefName("Rashed")
                .role("senior_editor")
                .domain("elearning")
                .channel("lojens")
                .build();
    }

    public static final BizUser juniorFuad(){
        return BizUserBuilder.init()
                .userId(14L)
                .prefName("Kawsar")
                .role("editor")
                .domain("elearning")
                .channel("lojens")
                .build();
    }
}
