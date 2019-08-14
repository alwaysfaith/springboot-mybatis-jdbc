package com.ocean.controller;

import com.ocean.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = {"/user"})
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = {"/findAll"}, produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public List getAllUsers() {
        List list = userService.findAllUser();
        return list;
    }

    @RequestMapping(value = {"/test"}, produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public void getTest() {
        userService.test();
    }


    @RequestMapping(value = "/importExcel", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public void importExcel(@RequestParam("file") MultipartFile file) {
        userService.importExcel(file);
    }
}
