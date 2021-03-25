package com.seidlserver.db;

import com.seidlserver.pojos.Gameserver;
import com.seidlserver.pojos.GameserverType;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

/*
    Created by: Jonas Seidl
    Date: 25.03.2021
    Time: 21:08
*/
public class GameserverManager {
    private static SessionFactory factory;

    public GameserverManager(){
        factory = new Configuration().configure().buildSessionFactory();
    }

    public List<Gameserver> getGameservers(){
        Session session = factory.openSession();
        Transaction tx = null;
        List<Gameserver> servers = null;
        try{
            tx = session.beginTransaction();
            servers = session.createQuery("SELECT g FROM Gameserver g").list();
            tx.commit();

        }catch(HibernateException ex){
            ex.printStackTrace();
            if(tx!=null){
                tx.rollback();
            }
        } finally{
            session.close();
        }
        return servers;
    }

    public Integer addGameserver(String script, String servername, String type){
        Session session = factory.openSession();
        Transaction tx = null;
        Integer id = null;
        try{
            tx = session.beginTransaction();
            GameserverType gt = session.get(GameserverType.class, type);
            Gameserver g = new Gameserver(script, servername, gt);
            id = (Integer)session.save(g);
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

    public void removeGameserver(Integer id){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Gameserver g = session.get(Gameserver.class, id);
            session.remove(g);
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
}
