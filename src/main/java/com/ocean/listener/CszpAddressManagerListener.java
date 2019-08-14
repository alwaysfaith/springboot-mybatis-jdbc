package com.ocean.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ocean.dto.AddressImportDTO;
import com.ocean.service.IimportExcleService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhouliang
 * @Date: 2019/6/12 14:51
 */
@Slf4j
public class CszpAddressManagerListener extends AnalysisEventListener {

    private IimportExcleService importExcleService;

    public CszpAddressManagerListener(IimportExcleService importExcleService) {
        this.importExcleService = importExcleService;
    }

    private List<AddressImportDTO> addressImportDTOS = new ArrayList<>();

    @Override
    public void invoke(Object excelObj, AnalysisContext analysisContext) {
        AddressImportDTO importDTO = (AddressImportDTO) excelObj;
        addressImportDTOS.add(importDTO);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        importExcleService.handleExcel(addressImportDTOS);
    }
}
