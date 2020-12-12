package com.gazprom.system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Date {
    private int day;
    private int month;
    private int year;
}
