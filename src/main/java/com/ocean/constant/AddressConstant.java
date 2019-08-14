package com.ocean.constant;

/**
 * @Author: zhouliang
 * @Date: 2019/6/12 15:13
 */
public class AddressConstant {
    /**
     * 数据库连接
     */
    public static final String URL = "jdbc:mysql://127.0.0.1:3306/cszp?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&serverTimezone=GMT%2B8&rewriteBatchedStatements=true";
    /**
     * 用户名
     */
    public static final String USER_NAME = "root";
    /**
     * 密码
     */
    public static final String PASS_WORD = "root";
    /**
     * 驱动名字
     */
    public static final String CLASS_NAME = "com.mysql.jdbc.Driver";

    public static final String ADDRESS_SQL = "insert into cszp_address_manage(send_company,send_person,send_mobile,send_province,send_city,send_address) values (?,?,?,?,?,?)";

}
