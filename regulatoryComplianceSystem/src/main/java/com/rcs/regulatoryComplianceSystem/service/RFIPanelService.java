package com.rcs.regulatoryComplianceSystem.service;

import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserResponseDTO;
import com.rcs.regulatoryComplianceSystem.entity.Role;
import com.rcs.regulatoryComplianceSystem.entity.User;
import com.rcs.regulatoryComplianceSystem.repositories.RoleRepository;
import com.rcs.regulatoryComplianceSystem.repositories.UserRepository;
import com.rcs.regulatoryComplianceSystem.service.serviceImp.RFIPanelImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

public class RFIPanelService implements RFIPanelImp {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users= userRepository.findByRole_RoleType(Role.RoleType.RFI);
        return users.stream()
                .map(user -> new UserResponseDTO(
                        user.getUserId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getInstitution(),
                        user.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public String createUser(UserRequestDTO requestDTO) {
        System.out.println("Raw Password: " + requestDTO.getPassword());
        User user = new User();
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        Role defaultRole= roleRepository.findByRoleName(Role.RoleName.MAKER)
                .orElseThrow(()-> new RuntimeException("defualt role is not found"));
        user.setRole(defaultRole);
        userRepository.save(user);
        return "user saved successfully";
    }

    @Override
    public String updateRole(Long userId, String role) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Role.RoleName roleName = Role.RoleName.valueOf(role);
        Role newRole = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(newRole);
        userRepository.save(user);
        return "User role updated successfully";
    }

}
