package com.gazprom.system.service;

import com.gazprom.system.exceprion.AppException;
import com.gazprom.system.model.Department;
import com.gazprom.system.model.Unit;
import com.gazprom.system.payload.DepartmentRequest;
import com.gazprom.system.repository.DepartmentRepository;
import com.gazprom.system.repository.UnitRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

  private final UnitRepository unitRepository;
  private final DepartmentRepository departmentRepository;

  @Override
  public short addDepartment(DepartmentRequest departmentRequest) {
    Unit unit = unitRepository.findById(departmentRequest.getUnitId())
        .orElseThrow(() -> new AppException("Unit not found."));
    Department department = new Department(departmentRequest.getTitle(), unit);
    departmentRepository.save(department);
    return 1;
  }

  @Override
  public List<?> getAllDepartmentByUnit(Long id) {
    return departmentRepository.findAllByUnitId(id);
  }

  @Override
  public short deleteDepartment(Long id) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new AppException("Department not found."));
    departmentRepository.delete(department);
    return 1;
  }
}
