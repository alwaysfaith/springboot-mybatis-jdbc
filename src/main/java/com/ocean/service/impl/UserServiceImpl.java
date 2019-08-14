package com.ocean.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.ocean.dao.UserInfoMapper;
import com.ocean.dto.AddressImportDTO;
import com.ocean.listener.CszpAddressManagerListener;
import com.ocean.service.IUserService;
import com.ocean.service.IimportExcleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Service(value = "userService")
@Slf4j
public class UserServiceImpl implements IUserService {

    // 请求总数
    public static int clientTotal = 5000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;

    @Autowired
    private UserInfoMapper userDao;//这里可能会报错，但是并不会影响

    @Autowired
    private IimportExcleService importExcleService;

    @Override
    public List findAllUser() {
        return userDao.findAllUser();
    }


    @Override
    public void test() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    listTask();
                    semaphore.release();
                } catch (Exception e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        executorService.shutdown();
    }

    private void listTask() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        bulidList(list1, list2);
        log.info("list1======》》》》{}", list1.size());
        log.info("list2======》》》{}", list2.size());
        if (list1.size() != list2.size()) {
            System.out.println("错误！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
        }
    }

    private void bulidList(List<Integer> list1, List<Integer> list2) {
        for (int i = 0; i < 1000; i++) {
            list1.add(i);
            list2.add(i);
        }
    }

    @Override
    public void importExcel(MultipartFile file) {
        long start = System.currentTimeMillis();
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            CszpAddressManagerListener listener = new CszpAddressManagerListener(importExcleService);
            EasyExcelFactory.readBySax(inputStream, new Sheet(1, 2, AddressImportDTO.class), listener);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        long end = System.currentTimeMillis();
        System.err.println("导入耗时:" + (end - start) + " 毫秒");

    }
}
