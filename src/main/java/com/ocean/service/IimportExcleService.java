package com.ocean.service;

import com.ocean.dto.AddressImportDTO;

import java.util.List;

/**
 * @Author: zhouliang
 * @Date: 2019/6/12 14:59
 */
public interface IimportExcleService {

    void handleExcel(List<AddressImportDTO> addressImportDTOS);
}
