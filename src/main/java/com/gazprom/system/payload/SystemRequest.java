package com.gazprom.system.payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemRequest {
  String title;
  Long ownerId;
  Long primaryAdminId;
  Long backupAdminId;
  List<Long> privilegesId;
}
