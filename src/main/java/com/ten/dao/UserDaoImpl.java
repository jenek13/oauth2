package com.ten.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.ten.model.User;

import javax.persistence.*;
import java.util.List;

@Repository
@Transactional
public class UserDaoImpl implements UserDAO {

    public UserDaoImpl() {
    }

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> selectAllUsers() {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    @Override
    public User getUserByLogin(String login) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class);
        query.setParameter("login", login);
        User user = new User();
        try {
            user = query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println(e);
        }
        return user;
    }

    @Override
    public void addUser(User user) {
        em.persist(user);
    }

    @Override
    public void editUser(User user) {
        em.merge(user);
    }

    @Override
    public void removeUser(long id) {
        Query query = em.createQuery("DELETE from User u WHERE u.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public User getUserById(long id) {
        return em.find(User.class, id);
    }

}
