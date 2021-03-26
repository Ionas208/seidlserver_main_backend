package com.seidlserver.user;

import com.seidlserver.pojos.User;
import org.springframework.data.repository.CrudRepository;

/*
    Created by: Jonas Seidl
    Date: 26.03.2021
    Time: 10:38
*/
public interface UserRepository extends CrudRepository<User, Integer> {}