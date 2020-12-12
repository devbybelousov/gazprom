package com.gazprom.system.payload;

import com.gazprom.system.model.History;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApplicationRequest {
    private List<Long> usersId;
    private Date fillingDate;
    private Date expiryDate;
    private Long idSystem;
}
