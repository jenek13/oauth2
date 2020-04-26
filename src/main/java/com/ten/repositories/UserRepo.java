package com.ten.repositories;

import com.ten.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByLogin(String login);

}
