package com.ten.service;

import com.ten.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ten.model.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public void addRole(Role role) {
        roleDao.save(role);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleDao.getRoleByName(roleName);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleDao.getById(id);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDao.getAll();
    }

    @Override
    public void updateRole(Role role) {
        roleDao.update(role);
    }

    @Override
    public void deleteRoleById(Long id) {
        roleDao.deleteById(id);
    }

    @Override
    public Set<Role> getRolesbyID(Long id) {
        Set<Role> roles = new HashSet<>();

        if (id == 1) {
            roles.add(getRoleById(1L));
        } else if (id == 2) {
            roles.add(getRoleById(2L));
        } else {
            roles.add(getRoleById(2L));
        }

        return roles;
    }
}
