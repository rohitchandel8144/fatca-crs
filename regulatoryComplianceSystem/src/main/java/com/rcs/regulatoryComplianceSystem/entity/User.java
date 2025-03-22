package com.rcs.regulatoryComplianceSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rcs.regulatoryComplianceSystem.entity.fatcaEntity.FatcaReport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institution_id")
    @JsonIgnore
    private Institution institution;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UserPermission> userPermissions = new ArrayList<>();

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private Date resetTokenExpiry;

    @OneToMany(mappedBy = "uploadedBy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "approvedBy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Report> approvedReports = new ArrayList<>();

    @OneToMany(mappedBy = "rejectedBy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Report> rejectedReports = new ArrayList<>();



    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<FatcaReport> createdFatcaReports; // ✅ User can create multiple FATCA reports

    @OneToMany(mappedBy = "updatedBy", cascade = CascadeType.ALL)
    private List<FatcaReport> updatedFatcaReports; // ✅ User can update multiple FATCA reports

    @OneToMany(mappedBy = "approvedBy", cascade = CascadeType.ALL)
    private List<FatcaReport> approvedFatcaReports; // ✅ User can approve multiple FATCA reports

    @OneToMany(mappedBy = "rejectedBy", cascade = CascadeType.ALL)
    private List<FatcaReport> rejectedFatcaReports;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.concat(
                roles.stream().flatMap(role -> role.getRolePermissions().stream()
                        .map(rp -> (GrantedAuthority) () -> "PERMISSION_" + rp.getPermission().getName().name())),
                userPermissions.stream()
                        .map(up -> (GrantedAuthority) () -> "PERMISSION_" + up.getPermission().getName().name())
        ).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}



