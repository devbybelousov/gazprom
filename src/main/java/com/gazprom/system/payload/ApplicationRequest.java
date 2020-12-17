package com.gazprom.system.payload;

import com.gazprom.system.model.History;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequest {
    private List<Long> usersId;
    private List<Long> privilegesId;
    private Long idSystem;
}
