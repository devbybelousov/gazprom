package com.gazprom.system.service;

import com.gazprom.system.exceprion.AppException;
import com.gazprom.system.model.InformationSystem;
import com.gazprom.system.model.Privilege;
import com.gazprom.system.model.User;
import com.gazprom.system.payload.SystemRequest;
import com.gazprom.system.repository.DepartmentRepository;
import com.gazprom.system.repository.PrivilegeRepository;
import com.gazprom.system.repository.RoleRepository;
import com.gazprom.system.repository.SystemRepository;
import com.gazprom.system.repository.UnitRepository;
import com.gazprom.system.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SystemServiceImpl implements SystemService {

  private final PrivilegeRepository privilegeRepository;
  private final SystemRepository systemRepository;
  private final UserRepository userRepository;

  @Override
  public short addSystem(SystemRequest systemRequest) {
    User owner = userRepository.findById(systemRequest.getOwnerId())
        .orElseThrow(() -> new AppException("User not found."));
    User primaryAdmin = userRepository.findById(systemRequest.getPrimaryAdminId())
        .orElseThrow(() -> new AppException("User not found."));
    User backupAdmin = userRepository.findById(systemRequest.getBackupAdminId())
        .orElseThrow(() -> new AppException("User not found."));
    List<Privilege> privileges = new ArrayList<>();
    for (Long id : systemRequest.getPrivilegesId()) {
      Privilege privilege = privilegeRepository.findById(id)
          .orElseThrow(() -> new AppException("Privilege not found."));
      privileges.add(privilege);
    }
    InformationSystem system = new InformationSystem(systemRequest.getTitle(), owner, primaryAdmin,
        backupAdmin, privileges);
    systemRepository.save(system);
    return 1;
  }


  @Override
  public List<?> getAllSystem() {
    return systemRepository.findAll();
  }

  @Override
  public InformationSystem getSystemInfoById(Long id) {
    return systemRepository.findById(id).orElseThrow(() -> new AppException("System not found."));
  }

  @Override
  public short deleteSystem(Long id) {
    InformationSystem system = systemRepository.findById(id)
        .orElseThrow(() -> new AppException("System not found."));
    systemRepository.delete(system);
    return 1;
  }
}
