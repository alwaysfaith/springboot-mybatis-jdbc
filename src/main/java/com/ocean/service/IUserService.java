package com.ocean.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {

    List findAllUser();

    void test();

    void importExcel(MultipartFile file);
}
