package com.kropotov.asrd.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Artem Kropotov on 03.06.2020
 */
@Getter
@Setter
@NoArgsConstructor
public class History {
    private String title;
    private Object value;

    public History(String title, Object value) {
        this.title = title;
        this.value = value;
    }
}
