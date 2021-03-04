package com.gazprom.system.service;

import com.gazprom.system.payload.DepartmentRequest;
import java.util.List;

public interface DepartmentService {

  short addDepartment(DepartmentRequest departmentRequest);

  List<?> getAllDepartmentByUnit(Long id);

  short deleteDepartment(Long id);
}
