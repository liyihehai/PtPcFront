package com.nnte.pf_basic.entertity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
public class MethodObject {
    private Method method;
    private Object object;
}
