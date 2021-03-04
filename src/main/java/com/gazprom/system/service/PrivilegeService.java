package com.gazprom.system.service;

import com.gazprom.system.payload.PrivilegeRequest;

public interface PrivilegeService {
  short addPrivilege(PrivilegeRequest privilegeRequest);

  short deletePrivilege(Long id);
}
