package com.rcs.regulatoryComplianceSystem.service.serviceImp;

import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserResponseDTO;
import com.rcs.regulatoryComplianceSystem.entity.Role;
import com.rcs.regulatoryComplianceSystem.entity.User;

import java.util.List;

public interface MinistryPanelImp {
    public List<UserResponseDTO> getAllUsers();
    public String createUser(UserRequestDTO userRequestDTO);
    public String updateRole(Long userId,String role);
}
