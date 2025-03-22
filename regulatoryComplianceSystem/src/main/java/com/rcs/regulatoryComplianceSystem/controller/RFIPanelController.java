package com.rcs.regulatoryComplianceSystem.controller;


import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.RoleResponseDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserResponseDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserUpdateRequestDTO;
import com.rcs.regulatoryComplianceSystem.service.AuditLogService;
import com.rcs.regulatoryComplianceSystem.service.MinistryPanelService;
import com.rcs.regulatoryComplianceSystem.service.RFIPanelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rfi-panel")
public class RFIPanelController {

    private static final Logger logger = LoggerFactory.getLogger(RFIPanelController.class);

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private RFIPanelService rfiPanelService;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RFI_SUBADMIN')")
    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<UserResponseDTO> users = rfiPanelService.getAllUsers();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        logger.info("User {} accessed all users", email);
        logger.info("User Authorities: {}", authentication.getAuthorities());
        auditLogService.logAction(email,"viewed all users");
        return ResponseEntity.ok(users);
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RFI_SUBADMIN')")
    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO request) {
            String message = rfiPanelService.createUser(request);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            logger.info("User Authorities: {}", authentication.getAuthorities());
            auditLogService.logAction(email,"created a user : "+ message);
            return ResponseEntity.ok(message);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RFI_SUBADMIN')")
    @PutMapping("/update-user-roles-permissions/{userId}")
    public ResponseEntity<String> updateUserRolesAndPermissions(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequestDTO updateRequestDTO) {
            String response = rfiPanelService.updateUserRolesAndPermissions(userId, updateRequestDTO);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            logger.info("User Authorities: {}", authentication.getAuthorities());
            auditLogService.logAction(email,"update user role : "+response );
            return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RFI_SUBADMIN')")
    public ResponseEntity<List<RoleResponseDTO>> getRFIRoles() {
        List<RoleResponseDTO> ministryRoles = rfiPanelService.getRFIRoles();
        return ResponseEntity.ok(ministryRoles);
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RFI_SUBADMIN')")
    public ResponseEntity<List<String>> getAllRFIPermissions() {
        List<String> permissions = rfiPanelService.getRFIPermissions();
        return ResponseEntity.ok(permissions);
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RFI_SUBADMIN')")
    @DeleteMapping("/delete/{toDeleteUserId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long toDeleteUserId){
        String msg = rfiPanelService.deleteUser(toDeleteUserId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        logger.info("User Authorities: {}", authentication.getAuthorities());
        auditLogService.logAction(email,"created a user : "+msg);
        return ResponseEntity.ok(msg);
    }
}
