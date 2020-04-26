package com.ten.service;

import com.ten.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {

    void addRole(Role role);

    Role getRoleByName(String roleName);

    Role getRoleById(Long id);

    List<Role> getAllRoles();

    void updateRole(Role role);

    void deleteRoleById(Long id);

    Set<Role> getRolesbyID(Long id);
}
