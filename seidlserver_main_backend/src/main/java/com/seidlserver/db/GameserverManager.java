package com.seidlserver.db;

import com.seidlserver.pojos.gameserver.Gameserver;
import com.seidlserver.pojos.gameserver.GameserverType;
import com.seidlserver.pojos.user.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

/*
    Created by: Jonas Seidl
    Date: 25.03.2021
    Time: 21:08
*/
public class GameserverManager {
    private static SessionFactory factory;

    private static GameserverManager instance;

    public static GameserverManager getInstance(){
        if(instance == null){
            instance = new GameserverManager();
        }
        return instance;
    }

    private GameserverManager(){
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

    public List<Gameserver> getGameserversForUser(Integer userid){
        Session session = factory.openSession();
        Transaction tx = null;
        List<Gameserver> servers = null;
        try{
            tx = session.beginTransaction();
            User u = session.get(User.class, userid);
            Query q = session.createQuery("SELECT g FROM Gameserver g WHERE g.user = :u");
            q.setParameter("u", u);
            servers = q.list();
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

    public Integer addGameserver(String script, String servername, String type, Integer userid){
        Session session = factory.openSession();
        Transaction tx = null;
        Integer id = null;
        try{
            tx = session.beginTransaction();
            GameserverType gt = session.get(GameserverType.class, type);
            User u = session.get(User.class, userid);
            Gameserver g = new Gameserver(script, servername, gt, u);
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

    public void shareGameserver(Integer gameserverid, Integer userid){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();

            Gameserver g = session.get(Gameserver.class, gameserverid);
            User u = session.get(User.class, userid);

            g.getSharedUsers().add(u);

            session.save(g);
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

    public void unshareGameserver(Integer gameserverid, Integer userid){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();

            Gameserver g = session.get(Gameserver.class, gameserverid);
            User u = session.get(User.class, userid);

            g.getSharedUsers().remove(u);
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
