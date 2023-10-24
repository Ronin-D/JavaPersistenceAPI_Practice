package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.Constants;
import org.example.models.Product;
import org.example.models.User;

import java.util.List;

public class ProductDbRepository {
    EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory(Constants.PERSISTENCE_UNIT_NAME);

    public void addProduct(Product product){
        new Thread(()-> {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(product);
            entityManager.getTransaction().commit();
            entityManager.close();
        }).start();

    }

    public void removeProduct(Product product){
        new Thread(()->{
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.contains(product)? product:entityManager.merge(product));
            entityManager.getTransaction().commit();
            entityManager.close();
        }).start();
    }


    public List<Product> getProducts(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product>query = criteriaBuilder.createQuery(Product.class);
        query.select(query.from(Product.class));
        List<Product> products = entityManager.createQuery(query).getResultList();
        entityManager.close();
        return products;
    }

    public void buyProduct(Product productSource, User userSource){
        new Thread(()->{
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            User user = entityManager.find(User.class,userSource.id);
            Product product = entityManager.find(Product.class,productSource.id);
            int cnt = product.getCount()-1;
            int price = product.getPrice();
            int balance = user.getBalance();
            if (balance<product.getPrice()){
                entityManager.getTransaction().rollback();
                System.out.println("Not enough money");
            }
            else{
                if (cnt==0){

                    removeProduct(product);
                }else{
                    product.setCount(cnt);
                }
                user.setBalance(balance-price);
                userSource.setBalance(balance-price);
                System.out.println("The purchase is successful");
                entityManager.getTransaction().commit();
                entityManager.close();
            }
        }).start();


    }

    public Product getProduct(String name, String manufacturer){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = criteriaBuilder.createQuery(Product.class);
        Root<Product> productRoot = query.from(Product.class);
        query.select(productRoot)
                .where(criteriaBuilder.equal(productRoot.get("name"),name))
                .where(criteriaBuilder.equal(productRoot.get("manufacturer"),manufacturer));
        List<Product>productList = entityManager.createQuery(query).getResultList();
        entityManager.close();
        if (!productList.isEmpty()){
           return productList.get(0);
        }
        else{
            return null;
        }
    }

    public void close(){
        entityManagerFactory.close();
    }

}
