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

    public List<Gameserver> getGameservers() throws HibernateException{
        Session session = factory.openSession();
        Transaction tx = null;
        List<Gameserver> servers = null;
        try{
            tx = session.beginTransaction();
            servers = session.createQuery("SELECT g FROM Gameserver g").list();
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
        return servers;
    }

    public List<Gameserver> getGameserversForUser(Integer userid) throws HibernateException{
        Session session = factory.openSession();
        Transaction tx = null;
        List<Gameserver> servers = null;
        try{
            tx = session.beginTransaction();
            User u = session.get(User.class, userid);
            Query q = session.createQuery("SELECT g FROM Gameserver g WHERE g.owner = :u");
            q.setParameter("u", u);
            servers = q.list();
            servers.addAll(u.getSharedGameservers());
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
        return servers;
    }

    public Gameserver getGameserversForId(Integer id) throws HibernateException{
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Gameserver g = session.get(Gameserver.class, id);
            tx.commit();
            session.close();
            return g;
        }catch(HibernateException ex){
            ex.printStackTrace();
            if(tx!=null){
                tx.rollback();
            }
            session.close();
            throw ex;
        }

    }

    public List<GameserverType> getGameserverTypes() throws HibernateException{
        Session session = factory.openSession();
        Transaction tx = null;
        List<GameserverType> types = null;
        try{
            tx = session.beginTransaction();
            Query q = session.createQuery("SELECT g FROM GameserverType g");
            types = q.list();
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
        return types;
    }

    public Integer addGameserver(String script, String servername, String type, Integer userid) throws HibernateException{
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

    public void shareGameserver(Integer gameserverid, Integer serverownerID, Integer recipientID) throws HibernateException{
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();

            Gameserver g = session.get(Gameserver.class, gameserverid);
            User recipient = session.get(User.class, recipientID);

            if(g.getOwner().getId() == serverownerID){
                g.getSharedUsers().add(recipient);
            }else{
                throw new HibernateException("Attempting to share gameserver from different user");
            }
            session.save(g);
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

    public void unshareGameserver(Integer gameserverid, Integer userid) throws HibernateException{
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();

            Gameserver g = session.get(Gameserver.class, gameserverid);
            User u = session.get(User.class, userid);

            g.getSharedUsers().remove(u);
            session.remove(g);
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

    public void removeGameserver(Integer id) throws HibernateException{
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Gameserver g = session.get(Gameserver.class, id);
            session.remove(g);
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

    public void removeGameserverFromUser(Integer id, Integer userid) throws HibernateException{
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Gameserver g = session.get(Gameserver.class, id);
            System.out.println("=============================================gameserver"+g);
            System.out.println("=============================================id"+id);
            System.out.println("=============================================userid"+userid);
            if(g.getOwner().getId() == userid){
                session.remove(g);
                tx.commit();
            }
            else{
                throw new HibernateException("Attempting to remove gameserver from different user");
            }
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
