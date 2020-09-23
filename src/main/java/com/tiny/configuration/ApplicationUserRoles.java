package com.tiny.configuration;

import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.tiny.configuration.ApplicationUserPermissions.*;

@AllArgsConstructor
public enum ApplicationUserRoles {
    USER( Sets.newHashSet(USER_UPLOAD, USER_DELETE) ),
    ADMIN( Sets.newHashSet(ADMIN_DELETE, ADMIN_UPDATE, ADMIN_ACTUATOR_ENDPOINTS) );

    private Set<ApplicationUserPermissions> permissions;
    
    public Set<ApplicationUserPermissions> getPermissions(){
        return permissions;
    }

    public Set<SimpleGrantedAuthority> grantedAuthorities(){
        Set<SimpleGrantedAuthority> grantedAuthorities = getPermissions().stream()
                                                         .map( permission -> new SimpleGrantedAuthority(permission.name()))
                                                         .collect( Collectors.toSet());
                                                
        grantedAuthorities.add( new SimpleGrantedAuthority("ROLE_" + this.name()) );
        return grantedAuthorities;
    }

}