package db;

import java.util.logging.Logger;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
/**
 * 
 * Manage the solutions' DB
 */
public class DbManager {

	private static class DbManagerHolder{
		public static final DbManager instance = new DbManager();
	}
	
	public static DbManager getInstance() {
		return DbManagerHolder.instance;
	}

	private SessionFactory factory;

	private DbManager() {
		// to show the severe msg
		Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.SEVERE);

		// reading the xml so he can connect to the Db
		Configuration configuration = new Configuration();
		configuration.configure();
		factory = configuration.buildSessionFactory();
	}
	
	/**
	 * Checking if Solution exists in the DB by the levelId
	 * @param levelId
	 * @return The solution if exists or null
	 */
	public LevelSolutionData getLevelSolution(String levelId)
	{
		Session session = null;
		LevelSolutionData levelSolData=null;
		Query query=null;
		try 
		{
			session = factory.openSession();
			query=session.createQuery("FROM LevelSolutionData as sol WHERE sol.levelId= :levelId");
			query.setParameter("levelId", levelId);
			levelSolData =(LevelSolutionData) query.getSingleResult();
							
			System.out.println("LevelSol From DB: "+ levelSolData.toString());
			return levelSolData;
		} 
		catch (HibernateException ex) 
		{
			System.out.println(ex.getMessage());
		} 
		catch (NoResultException e){
			return null;
		}
		finally 
		{
			if (session != null)
				session.close();
		}
		return null;
	}
	
	
	/**
	 * Adding solution to the DB
	 * @param obj- the solution 
	 */
	public void add(Object obj) {
		Session session = null;
		Transaction tx = null;

		try {
			session = factory.openSession();
			tx = session.beginTransaction();

			session.save(obj);
			tx.commit();
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			System.out.println(ex.getMessage());
		} finally {
			if (session != null)
				session.close();
		}
	}

}
