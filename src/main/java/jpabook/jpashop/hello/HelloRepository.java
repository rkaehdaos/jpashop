package jpabook.jpashop.hello;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class HelloRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Hello hello) {
        em.persist(hello);
        return hello.getId();
    }

    public Hello findById(Long id) {
        return em.find(Hello.class, id);
    }

}