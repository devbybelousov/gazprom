package com.gazprom.system.payload;

import com.gazprom.system.model.History;
import com.gazprom.system.model.Privilege;
import com.gazprom.system.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFormat {
    private Long idRequest;
    private String status;
    private List<History> history;
    private List<UserProfile> users;
    private List<Privilege> privileges;
    private Date fillingDate;
    private Date expiryDate;
    private Long idSystem;
    private String system;
}
