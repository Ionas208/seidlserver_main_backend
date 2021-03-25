package com.seidlserver.db;

import com.seidlserver.pojos.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

/*
    Created by: Jonas Seidl
    Date: 25.03.2021
    Time: 18:21
*/
public class UserManager {
    private static SessionFactory factory;

    public UserManager(){
        factory = new Configuration().configure().buildSessionFactory();
    }

    public List<User> getUsers(){
        Session session = factory.openSession();
        Transaction tx = null;
        List<User> users = null;
        try{
            tx = session.beginTransaction();
            users = session.createQuery("SELECT u FROM User u").list();
            tx.commit();

        }catch(HibernateException ex){
            ex.printStackTrace();
            if(tx!=null){
                tx.rollback();
            }
        } finally{
            session.close();
        }
        return users;
    }

    public Integer addUser(String first_name, String last_name, String email, String password){
        Session session = factory.openSession();
        Transaction tx = null;
        Integer id = null;
        try{
            tx = session.beginTransaction();
            User u = new User(first_name, last_name, email, password);
            id = (Integer) session.save(u);
            tx.commit();

        }catch(HibernateException ex){
            ex.printStackTrace();
            if(tx!=null){
                tx.rollback();
            }
        } finally{
            session.close();
        }
        return id;
    }

    public void removeUser(Integer id){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            User u = session.get(User.class, id);
            session.remove(u);
            tx.commit();

        }catch(HibernateException ex){
            ex.printStackTrace();
            if(tx!=null){
                tx.rollback();
            }
        } finally{
            session.close();
        }
    }

    public static void main(String[] args) {
        UserManager m = new UserManager();
        Integer id = m.addUser("Hans", "Franz", "lmao", "lol");
        System.out.println(id);
    }
}
