package com.company.authservice.model;

import com.company.authservice.utils.RoleEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "user_roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;
    private String guid;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role_name",columnDefinition = "CHAR(1)")
    private RoleEnum roleName;
    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "creation_timestamp")
    private Timestamp creationTimestamp;
    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "last_updated_timestamp")
    private Timestamp lastUpdatedTimestamp;

    public Role() {
    }

    public Role(int roleId, String guid, RoleEnum roleName, Timestamp creationTimestamp, Timestamp lastUpdatedTimestamp) {
        this.roleId = roleId;
        this.guid = guid;
        this.roleName = roleName;
        this.creationTimestamp = creationTimestamp;
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public RoleEnum getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleEnum roleName) {
        this.roleName = roleName;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Timestamp getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(Timestamp lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }
}
