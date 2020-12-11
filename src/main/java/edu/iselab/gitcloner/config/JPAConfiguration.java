package edu.iselab.gitcloner.config;

import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.Getter;
import lombok.Setter;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
public class JPAConfiguration {

    public static class EntityAuditorAware implements AuditorAware<String> {

        @Override
        public Optional<String> getCurrentAuditor() {
            return Optional.of("user");
        }
    }
    
    @Getter
    @Setter
    @MappedSuperclass
    @EntityListeners(AuditingEntityListener.class)
    public static abstract class Auditable<T> {

        @CreatedBy
        protected T createdBy;

        @Temporal(TemporalType.TIMESTAMP)
        @CreatedDate
        protected Date createdAt;

        @LastModifiedBy
        protected T updatedBy;

        @Temporal(TemporalType.TIMESTAMP)
        @LastModifiedDate
        protected Date updatedAt;
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new EntityAuditorAware();
    }

}
