package hiber.dao;

import hiber.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jakarta.persistence.*;
import java.util.List;
import org.hibernate.query.Query;

@Repository
public class UserDaoImp implements UserDao {
   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   public User findUserByCar(String model, int series) {
      Session session = sessionFactory.openSession();
      Query<User> query = session.createQuery(
              "FROM User u WHERE u.car.model = :model AND u.car.series = :series", User.class);
      query.setParameter("model", model);
      query.setParameter("series", series);

      try {
         if (query.getResultList().isEmpty()) {
            throw new EntityNotFoundException("Пользователь не найден");
         }
         return query.getSingleResult();
      } finally {
         session.close();
      }
   }


}
