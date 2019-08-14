package com.ocean.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhouliang
 * @Date: 2019/6/12 18:29
 */
@Data
public class AddressImportErrorDTO implements Serializable {

    private static final long serialVersionUID = -5739097127838251695L;
    /**
     * 是否正常
     */
    private Boolean flag;
    /**
     * 不正常的list
     */
    private List<AddressImportDTO> list;
}
