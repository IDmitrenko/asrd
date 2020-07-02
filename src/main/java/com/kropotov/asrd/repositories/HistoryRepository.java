package com.kropotov.asrd.repositories;

import com.kropotov.asrd.entities.History;

import java.util.List;

/**
 * Created by Artem Kropotov on 03.06.2020
 */
public interface HistoryRepository {
    List<History> getHistoryById(Long id);
}
