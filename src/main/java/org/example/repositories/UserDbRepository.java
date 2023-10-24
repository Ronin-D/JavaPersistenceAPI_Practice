package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.Constants;
import org.example.models.User;

import java.util.List;

public class UserDbRepository {

    private final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory(Constants.PERSISTENCE_UNIT_NAME);
    public User getUser(String login){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User>userRoot = query.from(User.class);
        query.select(userRoot).where(criteriaBuilder.equal(userRoot.get("login"),login));
        List<User> resultList =  entityManager.createQuery(query).getResultList();
        User user;
        if (!resultList.isEmpty()){
           user =  resultList.get(0);
        }
        else {
           user = null;
        }
        entityManager.close();
        return user;
    }
    public List<User>getUsers(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        query.select(query.from(User.class));
        List<User> userList =  entityManager.createQuery(query).getResultList();
        return userList;
    }
    public void addUser(User user){
        new Thread(()->{
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            entityManager.close();
        }).start();

    }

    public void deleteUser(String login){
        new Thread(()->{
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
            Root<User>userRoot = query.from(User.class);
            query.select(userRoot).where(criteriaBuilder.equal(userRoot.get("login"),login));
            List<User> resultList =  entityManager.createQuery(query).getResultList();
            if (!resultList.isEmpty()){
                entityManager.getTransaction().begin();
                User user = resultList.get(0);
                entityManager.remove(user);
                System.out.println("User successfully deleted\n");
                entityManager.getTransaction().commit();
                entityManager.close();
            }
            else{
                System.out.println("User not found\n");
            }
        }).start();

    }
    public void deposit(int money, String login){
        new Thread(()->{
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            int id = getUser(login).id;
            User user = entityManager.find(User.class,id);
            entityManager.getTransaction().begin();
            user.setBalance(user.getBalance()+money);
            entityManager.getTransaction().commit();
        }).start();
    }
    public void close(){
        entityManagerFactory.close();
    }

}
