package db;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 */
public class HibernateUtil {
    private static StandardServiceRegistry standardServiceRegistry;
    private static SessionFactory sessionFactory;
    
    static{
        if (sessionFactory == null) {
          try {
            Configuration cfg   = new Configuration().configure();              
            standardServiceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(cfg.getProperties()).build();
                    
            sessionFactory = cfg.buildSessionFactory(standardServiceRegistry);
          } catch (HibernateException e) {
            if (standardServiceRegistry != null) {
              StandardServiceRegistryBuilder.destroy(standardServiceRegistry);
            }
          }
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
