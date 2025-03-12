package com.rcs.regulatoryComplianceSystem.service;

import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserResponseDTO;
import com.rcs.regulatoryComplianceSystem.entity.Institution;
import com.rcs.regulatoryComplianceSystem.entity.Role;
import com.rcs.regulatoryComplianceSystem.entity.User;
import com.rcs.regulatoryComplianceSystem.repositories.InstitutionRepository;
import com.rcs.regulatoryComplianceSystem.repositories.RoleRepository;
import com.rcs.regulatoryComplianceSystem.repositories.UserRepository;
import com.rcs.regulatoryComplianceSystem.service.serviceImp.MinistryPanelImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class MinistryPanelService implements MinistryPanelImp {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users= userRepository.findByRole_RoleType(Role.RoleType.MINISTRY);
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

    public String createUser(UserRequestDTO requestDTO) {
        System.out.println("Raw Password: " + requestDTO.getPassword());

        User user = new User();
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        // Fetch the default role
        Role defaultRole = roleRepository.findByRoleName(Role.RoleName.MAKER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRole(defaultRole);

        // Fetch the institution from the database
        Institution institution = institutionRepository.findById(requestDTO.getInstitutionId())
                .orElseThrow(() -> new RuntimeException("Institution not found"));
        user.setInstitution(institution);

        userRepository.save(user);
        return "User saved successfully";
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
