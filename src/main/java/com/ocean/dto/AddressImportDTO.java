package com.ocean.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zhouliang
 * @Date: 2019/6/12 14:52
 */
@Data
public class AddressImportDTO extends BaseRowModel implements Serializable {
    /**
     * 收寄件公司
     */
    @ExcelProperty(index = 0, value = "收寄件公司")
    private String sendCompany;
    /**
     * 收寄件人
     */
    @ExcelProperty(index = 1, value = "收寄件人")
    private String sendPerson;
    /**
     * 手机号码
     */
    @ExcelProperty(index = 2, value = "收寄件人电话")
    private String sendMobile;
    /**
     * 收寄件省
     */
    @ExcelProperty(index = 3, value = "收寄件省")
    private String sendProvince;
    /**
     * 收寄件市
     */
    @ExcelProperty(index = 4, value = "收寄件市")
    private String sendCity;
    /**
     * 收寄件地址
     */
    @ExcelProperty(index = 5, value = "收寄件地址")
    private String sendAddress;
}
