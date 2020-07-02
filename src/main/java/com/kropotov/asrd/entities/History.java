package com.kropotov.asrd.entities;

import com.kropotov.asrd.dto.SimpleUser;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Created by Artem Kropotov on 03.06.2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History {
    private String title;
    private Object value;
    private LocalDateTime changeDate;
    private SimpleUser user;
}
