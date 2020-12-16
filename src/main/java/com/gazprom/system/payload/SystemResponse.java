package com.gazprom.system.payload;

import com.gazprom.system.model.Privilege;
import com.gazprom.system.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SystemResponse {
    private Long id;
    private String title;
    private List<Privilege> privileges;
    private Long ownerId;
    private Long primaryAdminId;
    private Long backupAdminId;
}
