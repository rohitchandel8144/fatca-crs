package com.rcs.regulatoryComplianceSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAndNotificationDTO {
    private Long userId;
    private String name;
    private String email;
}
