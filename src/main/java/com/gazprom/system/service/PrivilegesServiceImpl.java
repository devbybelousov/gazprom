package com.gazprom.system.service;

import com.gazprom.system.exceprion.AppException;
import com.gazprom.system.model.Privilege;
import com.gazprom.system.payload.PrivilegeRequest;
import com.gazprom.system.repository.PrivilegeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PrivilegesServiceImpl implements PrivilegeService {

  private final PrivilegeRepository privilegeRepository;

  @Override
  public short addPrivilege(PrivilegeRequest privilegeRequest) {
    Privilege privilege = new Privilege(privilegeRequest.getTitle(),
        privilegeRequest.getDescription());
    privilegeRepository.save(privilege);
    return 1;
  }

  @Override
  public short deletePrivilege(Long id) {
    Privilege privilege = privilegeRepository.findById(id)
        .orElseThrow(() -> new AppException("Privilege not found."));
    privilegeRepository.delete(privilege);
    return 1;
  }
}
