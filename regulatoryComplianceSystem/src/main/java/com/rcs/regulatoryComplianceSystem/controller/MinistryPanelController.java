package com.rcs.regulatoryComplianceSystem.controller;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserResponseDTO;
import com.rcs.regulatoryComplianceSystem.service.AuditLogService;
import com.rcs.regulatoryComplianceSystem.service.MinistryPanelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ministry-panel")
public class MinistryPanelController {

    @Autowired
    private AuditLogService auditLogService;
    private static final Logger logger = LoggerFactory.getLogger(MinistryPanelController.class);

    @Autowired
    private MinistryPanelService ministryPanel;

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<UserResponseDTO> users= ministryPanel.getAllUsers();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        auditLogService.logAction(email,"viewed all users");
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/create-user")
    public ResponseEntity<String> createUser (@RequestBody  UserRequestDTO requestDTO){
       String msg= ministryPanel.createUser(requestDTO);
        auditLogService.logAction(requestDTO.getEmail(), "Created a new user account");
        logger.info("New user created: {}");
        return  ResponseEntity.ok(msg);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PutMapping("/update-role/{userId}")
    public ResponseEntity<String>updateRole(@PathVariable Long userId, @RequestBody UserRequestDTO requestDTO ){
        String msg = ministryPanel.updateRole(userId,requestDTO.getRole());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email  =authentication.getName();
        auditLogService.logAction(email,"updated the role of user");
        logger.info("updated the role of user"+userId);
        return  ResponseEntity.ok(msg);
    }



}
