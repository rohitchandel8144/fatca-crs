package com.rcs.regulatoryComplianceSystem.service;

import com.rcs.regulatoryComplianceSystem.entity.User;
import com.rcs.regulatoryComplianceSystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class RoleService {
    @Autowired
    private UserRepository userRepository;

    public Boolean hasRole(String roleName){
        String userName= SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userName).orElseThrow(()->new UsernameNotFoundException("email not found"));
        return user.getRoles().stream()
                .anyMatch(role -> role.getRoleName().toString().equals(roleName));

    }

    public Boolean hasRoleType(String roleType){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userName).orElseThrow(()-> new UsernameNotFoundException("email not found"));
        return user.getRoles().stream()
                .anyMatch(role -> role.getRoleName().toString().equals(roleType));

    }


}
