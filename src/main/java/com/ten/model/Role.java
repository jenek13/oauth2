package com.ten.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {



    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = User.class)
//    @JoinTable(name = "user_roles",
//            joinColumns = {@JoinColumn(name = "role_id")}
//            //inverseJoinColumns = {@JoinColumn(name = "user_id")}
//            )

    @JsonIgnore
    private Set<User> users = new HashSet<>();
    //private List<User> users = new ArrayList<>();


    public Role() {
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Role(String name) {
        this.name = name;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }



    public String getName() {
        return name;
    }



    @Override
    public String toString() {
        if (name.contains("ADMIN") && name.contains("USER")) {
            return "admin, user";
        } else if (name.contains("ADMIN")) {
            return "admin";
        }
        return "user";
    }

    @JsonIgnore
    public String getAuthority() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return name.equals(role.name);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + name.hashCode();
        return result;
    }

}
