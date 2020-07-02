package com.kropotov.asrd.repositories;

import com.kropotov.asrd.dto.SimpleUser;
import com.kropotov.asrd.entities.History;
import com.kropotov.asrd.entities.items.ControlSystem;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Artem Kropotov on 03.06.2020
 */
@Component
@RequiredArgsConstructor
public class ControlSystemHibernateEnversHistoryRepository implements HistoryRepository {

    private final EntityManager entityManager;
    private Map<String, String> dictionary = new HashMap<>();

    @PostConstruct
    public void init() {
        dictionary.put("number", "Номер");
        dictionary.put("location", "Местоположение");
        dictionary.put("purpose", "Названачение");
        dictionary.put("purposePassport", "Назначение по паспорту");
        dictionary.put("vintage", "Дата изготовления");
        dictionary.put("vpNumber", "Номер ВП");
        dictionary.put("otkDate", "Дата приемки ОТК");
        dictionary.put("vpDate", "Дата приемки ВП");
    }

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
            final ControlSystem entity = (ControlSystem) rowArray[0]; // TODO не распознает пользователя по id, почему?
            final RevisionType revisionType = (RevisionType) rowArray[2];
            final Set<String> propertiesChanged = (Set<String>) rowArray[3]; // TODO не достает проперти если название поля сущности не совпадает с названием поля в таблице
            final History history = new History();
            if (revisionType.equals(RevisionType.ADD)) {
                history.setTitle("Система добавлена");
                history.setChangeDate(entity.getCreatedAt());
            }
            if (revisionType.equals(RevisionType.DEL)) {
                history.setTitle("Система удалена");
                history.setChangeDate(entity.getUpdatedAt());
            }
            for (String propertyName : propertiesChanged) {
                try {
                    Field f = entityType.getDeclaredField(propertyName);
                    f.setAccessible(true);
                    System.out.println(propertyName + " : " + f.get(entity));
                    if (propertyName.equals("updatedAt")) {
                        history.setChangeDate((LocalDateTime) f.get(entity));
                    } else if (propertyName.equals("user")) {

                    } else {
                        history.setTitle(dictionary.get(propertyName));
                        history.setValue(f.get(entity));
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (history.getUser() == null) {
                history.setUser(SimpleUser.builder()
                        .firstName("Иван")
                        .lastName("Иванов")
                        .build());
            }
            historyList.add(history);
        }
        return historyList;
    }
}
