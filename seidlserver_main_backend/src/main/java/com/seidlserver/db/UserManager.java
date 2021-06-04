package com.seidlserver.db;

import com.seidlserver.pojos.user.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

/*
    Created by: Jonas Seidl
    Date: 25.03.2021
    Time: 18:21
*/
public class UserManager {
    private static SessionFactory factory;

    private static UserManager instance;

    public static UserManager getInstance(){
        if(instance == null){
            instance = new UserManager();
        }
        return instance;
    }

    private UserManager(){
        factory = new Configuration().configure().buildSessionFactory();
    }

    public List<User> getUsers() throws HibernateException{
        Session session = factory.openSession();
        Transaction tx = null;
        List<User> users = null;
        try{
            tx = session.beginTransaction();
            users = session.createQuery("SELECT u FROM User u").list();
            tx.commit();
            session.close();
        }catch(HibernateException ex){
            ex.printStackTrace();
            if(tx!=null){
                tx.rollback();
            }
            session.close();
            throw ex;
        }
        return users;
    }

    public Integer addUser(String first_name, String last_name, String email, String password) throws HibernateException{
        Session session = factory.openSession();
        Transaction tx = null;
        Integer id = null;
        try{
            tx = session.beginTransaction();
            User u = new User(first_name, last_name, email, password);
            id = (Integer) session.save(u);
            tx.commit();
            session.close();
        }catch(HibernateException ex){
            ex.printStackTrace();
            if(tx!=null){
                tx.rollback();
            }
            session.close();
            throw ex;
        }
        return id;
    }

    public void removeUser(Integer id) throws HibernateException{
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            User u = session.get(User.class, id);
            session.remove(u);
            tx.commit();
            session.close();
        }catch(HibernateException ex){
            ex.printStackTrace();
            if(tx!=null){
                tx.rollback();
            }
            session.close();
            throw ex;
        }
    }

    public User getUserByEmail(String email) throws HibernateException{
        List<User> users = getUsers();
        for (User u: users) {
            if(u.getEmail().equals(email)){
                return u;
            }
        }
        return null;
    }

    public void changePassword(int userid, String newPassword){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            User u = session.get(User.class, userid);
            u.setPassword(newPassword);
            tx.commit();
            session.close();
        }catch(HibernateException ex){
            ex.printStackTrace();
            if(tx!=null){
                tx.rollback();
            }
            session.close();
            throw ex;
        }
    }
}
