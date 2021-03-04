package com.gazprom.system.service;

import com.gazprom.system.exceprion.AppException;
import com.gazprom.system.model.Unit;
import com.gazprom.system.payload.UnitRequest;
import com.gazprom.system.repository.UnitRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UnitServiceImpl implements UnitService {

  private final UnitRepository unitRepository;

  @Override
  public short addUnit(UnitRequest unitRequest) {
    Unit unit = new Unit(unitRequest.getTitle());
    unitRepository.save(unit);
    return 1;
  }

  @Override
  public List<?> getAllUnit() {
    return unitRepository.findAll();
  }

  @Override
  public short deleteUnit(Long id) {
    Unit unit = unitRepository.findById(id).orElseThrow(() -> new AppException("Unit not found."));
    unitRepository.delete(unit);
    return 1;
  }
}
