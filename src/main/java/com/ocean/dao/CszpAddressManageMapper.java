package com.ocean.dao;

import com.ocean.pojo.CszpAddressManage;

public interface CszpAddressManageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cszp_address_manage
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cszp_address_manage
     *
     * @mbggenerated
     */
    int insert(CszpAddressManage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cszp_address_manage
     *
     * @mbggenerated
     */
    int insertSelective(CszpAddressManage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cszp_address_manage
     *
     * @mbggenerated
     */
    CszpAddressManage selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cszp_address_manage
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CszpAddressManage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cszp_address_manage
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CszpAddressManage record);
}