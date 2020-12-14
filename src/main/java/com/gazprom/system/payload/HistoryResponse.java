package com.gazprom.system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistoryResponse {
    private Long userId;
    private String reason;
    private Date date;
    private String status;
}
