package com.gazprom.system.service;

import com.gazprom.system.model.InformationSystem;
import com.gazprom.system.payload.SystemRequest;
import java.util.List;

public interface SystemService {

  short addSystem(SystemRequest systemRequest);

  List<?> getAllSystem();

  InformationSystem getSystemInfoById(Long id);

  short deleteSystem(Long id);
}
