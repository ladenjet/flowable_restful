package com.genpact.flowable.config.security.interceptor;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;


//@Component
public class MyInvocationSecurityMetadataSource  implements FilterInvocationSecurityMetadataSource {

//    	此方法是为了判定用户请求的url 是否在权限表中， 如果在权限表中，则返回给 decide 方法， 用来判定用户是否有此权限。如果不在权限表中则放行。
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
    	   Collection<ConfigAttribute> co=new ArrayList<>();
           co.add(new SecurityConfig("null"));
           return co;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
