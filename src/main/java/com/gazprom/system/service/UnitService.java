package com.gazprom.system.service;

import com.gazprom.system.payload.UnitRequest;
import java.util.List;

public interface UnitService {

  short addUnit(UnitRequest unitRequest);

  List<?> getAllUnit();

  short deleteUnit(Long id);
}
