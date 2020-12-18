package com.gazprom.system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemRequest {
    private String title;
    private Long ownerId;
    private Long primaryAdminId;
    private Long backupAdminId;
    List<Long> privilegesId;
}
