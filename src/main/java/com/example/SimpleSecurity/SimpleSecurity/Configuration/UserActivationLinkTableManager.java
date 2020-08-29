package com.example.SimpleSecurity.SimpleSecurity.Configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@AllArgsConstructor
public class UserActivationLinkTableManager implements ApplicationListener<ContextRefreshedEvent> {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (!LoginConfiguration.REQUIRED_ACTIVATION) {
            em.createNativeQuery("drop table " + LoginConfiguration.ACTIVATION_LINK_TABLE_NAME + ";").executeUpdate();
        }
    }
}
