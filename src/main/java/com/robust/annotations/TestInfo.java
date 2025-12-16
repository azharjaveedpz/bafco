package com.robust.annotations;


import com.robust.enums.TestPriority;
import com.robust.enums.TestSeverity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestInfo {
    TestPriority priority();
    TestSeverity severity() ;
}
