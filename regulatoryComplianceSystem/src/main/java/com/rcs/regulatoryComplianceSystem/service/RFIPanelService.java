package com.rcs.regulatoryComplianceSystem.service;

import com.rcs.regulatoryComplianceSystem.DTO.AuthDTO.AuthRequest;
import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionResponseDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.RoleResponseDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserResponseDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserUpdateRequestDTO;
import com.rcs.regulatoryComplianceSystem.entity.*;
import com.rcs.regulatoryComplianceSystem.repositories.*;
import com.rcs.regulatoryComplianceSystem.service.serviceImp.RFIPanelImp;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RFIPanelService implements RFIPanelImp {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Transactional
    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findByRoles_RoleType(Role.RoleType.RFI);

        return users.stream()
                .map(user -> {
                    List<String> permissions = userPermissionRepository.findByUser(user).stream()
                            .map(userPermission -> userPermission.getPermission().getName().name())
                            .collect(Collectors.toList());

                    InstitutionResponseDTO institutionDTO = user.getInstitution() != null
                            ? new InstitutionResponseDTO(user.getInstitution().getInstitutionId(), user.getInstitution().getName())
                            : null;

                    return new UserResponseDTO(
                            user.getUserId(),
                            user.getName(),
                            user.getEmail(),
                            user.getRoles().stream()
                                    .map(role -> role.getRoleName().name())
                                    .collect(Collectors.toList()),
                            institutionDTO,
                            user.getCreatedAt(),
                            permissions
                    );
                })
                .collect(Collectors.toList());
    }



    public String createUser(UserRequestDTO requestDTO) {
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists.");
        }
        // Create new user
        User user = new User();
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        // Assign roles
        Set<Role> assignedRoles = new HashSet<>();
        if (requestDTO.getRoles() != null && !requestDTO.getRoles().isEmpty()) {
            for (String roleName : requestDTO.getRoles()) {
                Role role = roleRepository.findByRoleName(Role.RoleName.valueOf(roleName.toUpperCase()))
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
                assignedRoles.add(role);
            }
        } else {
            Role defaultRole = roleRepository.findByRoleName(Role.RoleName.MAKER)
                    .orElseThrow(() -> new IllegalArgumentException("Default role not found"));
            assignedRoles.add(defaultRole);
        }
        user.setRoles(assignedRoles);

        // Assign institution (optional)
        if (requestDTO.getInstitutionId() != null) {
            Institution institution = institutionRepository.findById(requestDTO.getInstitutionId())
                    .orElseThrow(() -> new IllegalArgumentException("Institution not found"));
            user.setInstitution(institution);
        } else {
            user.setInstitution(null);
        }

        // Save user first (needed for assigning permissions)
        user = userRepository.save(user);

        // Assign permissions
        List<UserPermission> userPermissions = new ArrayList<>();
        List<String> assignedPermissions = new ArrayList<>();

        List<String> permissionsToAssign = (requestDTO.getPermissions() == null || requestDTO.getPermissions().isEmpty())
                ? List.of("READ")
                : requestDTO.getPermissions();

        for (String permissionName : permissionsToAssign) {
            Permission permission = permissionRepository.findByName(Permission.PermissionType.valueOf(permissionName))
                    .orElseThrow(() -> new RuntimeException("Permission Not Found: " + permissionName));

            UserPermission userPermission = new UserPermission();
            userPermission.setUser(user);
            userPermission.setPermission(permission);
            userPermissions.add(userPermission);
            assignedPermissions.add(permissionName);
        }

        // Save user permissions
        userPermissionRepository.saveAll(userPermissions);

        return "User created successfully with roles: " +
                assignedRoles.stream().map(role -> role.getRoleName().name()).collect(Collectors.joining(", ")) +
                " and permissions: " + String.join(", ", assignedPermissions);
    }

    @Override
    public String updateRole(Long userId, String role) {
        return "";
    }

    @Transactional
    public String updateUserRolesAndPermissions(Long userId, UserUpdateRequestDTO updateRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (updateRequestDTO.getRoles() != null && !updateRequestDTO.getRoles().isEmpty()) {
            user.getRoles().clear();
            userRepository.saveAndFlush(user);

            Set<Role> updatedRoles = new HashSet<>();
            for (String roleName : updateRequestDTO.getRoles()) {
                Role role = roleRepository.findByRoleName(Role.RoleName.valueOf(roleName.toUpperCase()))
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
                updatedRoles.add(role);
            }
            user.setRoles(updatedRoles);
        }

       if (updateRequestDTO.getPermissions() != null && !updateRequestDTO.getPermissions().isEmpty()) {
            userPermissionRepository.deleteByUser(user);
            userRepository.saveAndFlush(user);

            List<UserPermission> newUserPermissions = new ArrayList<>();
            for (String permissionName : updateRequestDTO.getPermissions()) {
                Permission permission = permissionRepository.findByName(Permission.PermissionType.valueOf(permissionName))
                        .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + permissionName));

                UserPermission userPermission = new UserPermission();
                userPermission.setUser(user);
                userPermission.setPermission(permission);
                newUserPermissions.add(userPermission);
            }

            userPermissionRepository.saveAll(newUserPermissions);
        }

        return "User updated successfully with the new roles and/or permissions.";
    }



    public List<RoleResponseDTO> getRFIRoles() {
        // Fetch roles from the database
        List<Role> roles = roleRepository.findByRoleType(Role.RoleType.MINISTRY);


        return roles.stream()
                .map(role -> new RoleResponseDTO(role.getRoleId(), role.getRoleName().name(), role.getRoleType().name()))
                .collect(Collectors.toList());
    }

    public List<String> getRFIPermissions(){
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(permission -> permission.getName().name())  // Assuming the name is an enum
                .collect(Collectors.toList());
    }


    @Transactional
    public String deleteUser(Long toDeleteUserId) {
        // Step 1: Find the user
        User user = userRepository.findById(toDeleteUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Step 2: Delete Audit Logs FIRST
        auditLogRepository.deleteByUserId(toDeleteUserId);

        // Step 3: Check Reports (Cannot Delete User if Reports Exist)
        List<Report> uploadedReports = reportRepository.findByUploadedBy(user);
        if (!uploadedReports.isEmpty()) {
            throw new IllegalStateException("Cannot delete user. The user has uploaded reports.");
        }

        List<Report> approvedReports = reportRepository.findByApprovedBy(user);
        for (Report report : approvedReports) {
            report.setApprovedBy(null);
        }
        reportRepository.saveAll(approvedReports);

        List<Report> rejectedReports = reportRepository.findByRejectedBy(user);
        for (Report report : rejectedReports) {
            report.setRejectedBy(null);
        }
        reportRepository.saveAll(rejectedReports);

        // Step 4: Handle Institution References
        List<Institution> institutionsCreatedByUser = institutionRepository.findByCreatedBy(user);
        for (Institution institution : institutionsCreatedByUser) {
            institution.setCreatedBy(null);
        }

        List<Institution> institutionsApprovedByUser = institutionRepository.findByApprovedBy(user);
        for (Institution institution : institutionsApprovedByUser) {
            institution.setApprovedBy(null);
        }

        List<Institution> institutionsRejectedByUser = institutionRepository.findByRejectedBy(user);
        for (Institution institution : institutionsRejectedByUser) {
            institution.setRejectedBy(null);
        }

        institutionRepository.saveAll(institutionsCreatedByUser);
        institutionRepository.saveAll(institutionsApprovedByUser);
        institutionRepository.saveAll(institutionsRejectedByUser);

        // Step 5: Handle User Permissions
        user.getUserPermissions().clear();
        userRepository.save(user);

        // Step 6: Remove User from Roles
        for (Role role : user.getRoles()) {
            role.getUsers().remove(user);
        }
        user.getRoles().clear();
        userRepository.save(user);

        // Step 7: Finally, Delete the User
        userRepository.delete(user);

        return "User deleted successfully";
    }

}
