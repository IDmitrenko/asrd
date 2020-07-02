package com.kropotov.asrd.repositories;

import com.kropotov.asrd.entities.History;
import com.kropotov.asrd.entities.items.ControlSystem;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Artem Kropotov on 03.06.2020
 */
@Component
@RequiredArgsConstructor
public class ControlSystemHibernateEnversHistoryRepository implements HistoryRepository {

    private final EntityManager entityManager;

    @Override
    public List<History> getHistoryById(Long id) {

        List<History> historyList = new ArrayList<>();
        Class<?> entityType = ControlSystem.class;
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List results = auditReader.createQuery().forRevisionsOfEntityWithChanges(entityType, false)
                .add(AuditEntity.id().eq(id))
                .getResultList();

        Object previousEntity = null;
        for (Object row : results) {
            Object[] rowArray = (Object[]) row;
            final ControlSystem entity = (ControlSystem) rowArray[0];
            final RevisionType revisionType = (RevisionType) rowArray[2]; //todo  или Спринг??
            final Set<String> propertiesChanged = (Set<String>) rowArray[3];
            for (String propertyName : propertiesChanged) {
                try {
                    Field f = entityType.getDeclaredField(propertyName);
                    f.setAccessible(true);
                    System.out.println(propertyName + " : " + f.get(entity));
                    historyList.add(new History(propertyName, f.get(entity)));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        return historyList;
    }
}
