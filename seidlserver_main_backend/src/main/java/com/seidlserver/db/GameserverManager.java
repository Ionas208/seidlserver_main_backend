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

import java.util.ArrayList;
import java.util.List;

/*
    Created by: Jonas Seidl
    Date: 25.03.2021
    Time: 21:08
*/

/***
 * Singleton Class for DB actions with the Gameserver
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

    /***
     * Fetches all Gameservers
     * @return List of all Gameservers
     * @throws HibernateException
     */
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

    /***
     * Fetches all Gameservers for a particular user, shared or owned
     * @param userid ID of the user
     * @return List of all Gameservers for the user
     * @throws HibernateException
     */
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

    /***
     * Fetches all shared Gameservers for a particular user
     * @param userid ID of the user
     * @return List of all shared Gameservers for the user
     * @throws HibernateException
     */
    public List<Gameserver> getSharedGameservers(Integer userid) throws HibernateException{
        Session session = factory.openSession();
        Transaction tx = null;
        List<Gameserver> servers = new ArrayList<>();
        try{
            tx = session.beginTransaction();
            User u = session.get(User.class, userid);
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

    /***
     * Fetches a single Gameserver identified by its ID
     * @param id The ID of the Gameserver
     * @return Gameserver
     * @throws HibernateException
     */
    public Gameserver getGameserverForId(Integer id) throws HibernateException{
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

    /***
     * Fetches all the different types of gameserver
     * @return List of all the GameserverTypes
     * @throws HibernateException
     */
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

    /***
     * Adds a gameserver to a user
     * @param script The script of the Gameserver
     * @param servername The name of the Gameserver
     * @param type The type of the Gameserver
     * @param userid The ID of the user
     * @return The ID of the new Gameserver
     * @throws HibernateException
     */
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

    /***
     * Shares a gameserver with a particular user
     * @param gameserverid The ID of the Gameserver to be shared
     * @param serverownerID The ID of the User of the Owner,
     *                      to check if the server belongs to the user
     * @param recipientID The ID of the User to be shared to
     * @throws HibernateException
     */
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

    /***
     * Unshares a gameserver from a particular user
     * @param gameserverid The ID of the gameserver to be unshared from
     * @param userid The ID fo the user to be unshared from
     * @throws HibernateException
     */
    public void unshareGameserver(Integer gameserverid, Integer userid) throws HibernateException{
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();

            Gameserver g = session.get(Gameserver.class, gameserverid);
            User u = session.get(User.class, userid);
            if(!u.getSharedGameservers().contains(g)){
                throw new HibernateException("Server does not belong to user.");
            }
            g.getSharedUsers().remove(u);
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

    /***
     * Removes a particular Gameserver
     * @param id The ID of the Gameserver to remove
     * @throws HibernateException
     */
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

    /***
     * Removes a Gameserver from a User
     * @param id The ID of the Gameserver
     * @param userid The ID of the User of the Owner,
     *               to check if the server belongs to the user
     * @throws HibernateException
     */
    public void removeGameserverFromUser(Integer id, Integer userid) throws HibernateException{
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Gameserver g = session.get(Gameserver.class, id);
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
