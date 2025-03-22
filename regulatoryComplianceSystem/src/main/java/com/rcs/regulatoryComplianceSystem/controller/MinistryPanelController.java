package com.rcs.regulatoryComplianceSystem.controller;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.RoleResponseDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserResponseDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserUpdateRequestDTO;
import com.rcs.regulatoryComplianceSystem.entity.Permission;
import com.rcs.regulatoryComplianceSystem.entity.Role;
import com.rcs.regulatoryComplianceSystem.service.AuditLogService;
import com.rcs.regulatoryComplianceSystem.service.MinistryPanelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/ministry-panel")
public class MinistryPanelController {

    @Autowired
    private AuditLogService auditLogService;
    private static final Logger logger = LoggerFactory.getLogger(MinistryPanelController.class);

    @Autowired
    private MinistryPanelService ministryPanel;

    @PreAuthorize("hasRole('SUPERADMIN') || hasRole('SUBADMIN')")
    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<UserResponseDTO> users= ministryPanel.getAllUsers();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        logger.info("User {} accessed all users", email);
        logger.info("User Authorities: {}", authentication.getAuthorities());
        auditLogService.logAction(email,"viewed all users");
        return ResponseEntity.ok(users);
    }


    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO request) {
            String message = ministryPanel.createUser(request);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            logger.info("User Authorities: {}", authentication.getAuthorities());
            auditLogService.logAction(email,"created a user : "+ message);
            return ResponseEntity.ok(message);
    }

    @PreAuthorize("hasRole('SUPERADMIN') || hasRole('SUBADMIN')")
    @PutMapping("/update-user-roles-permissions/{userId}")
    public ResponseEntity<String> updateUserRolesAndPermissions(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequestDTO updateRequestDTO) {
            String response = ministryPanel.updateUserRolesAndPermissions(userId, updateRequestDTO);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            logger.info("User Authorities: {}", authentication.getAuthorities());
            auditLogService.logAction(email,"update user role : "+response );
            return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasRole('SUPERADMIN') || hasRole('SUBADMIN')")
    @GetMapping("/get-ministry-roles")
    public ResponseEntity<List<RoleResponseDTO>> getMinistryRoles() {
        List<RoleResponseDTO> ministryRoles = ministryPanel.getMinistryRoles();
        return ResponseEntity.ok(ministryRoles);
    }

    @PreAuthorize("hasRole('SUPERADMIN') || hasRole('SUBADMIN')")
    @GetMapping("/get-ministry-permissions")
    public ResponseEntity<List<String>> getAllPermissions() {
        List<String> permissions = ministryPanel.getMinistryPermissions();
        return ResponseEntity.ok(permissions);
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @DeleteMapping("/delete/{toDeleteUserId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long toDeleteUserId){
        String msg = ministryPanel.deleteUser(toDeleteUserId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        logger.info("User Authorities: {}", authentication.getAuthorities());
        auditLogService.logAction(email,"created a user : "+msg);
        return ResponseEntity.ok(msg);
    }

}
