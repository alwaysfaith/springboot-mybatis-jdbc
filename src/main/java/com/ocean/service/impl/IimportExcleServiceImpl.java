package com.ocean.service.impl;

import com.ocean.constant.AddressConstant;
import com.ocean.dto.AddressImportDTO;
import com.ocean.dto.AddressImportErrorDTO;
import com.ocean.service.IimportExcleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @Author: zhouliang
 * @Date: 2019/6/12 14:59
 */
@Service
@Slf4j
public class IimportExcleServiceImpl implements IimportExcleService {

    @Resource(name = "importPool")
    private ThreadPoolExecutor poolExecutor;

    /**
     * 1000条为一个分组
     */
    private static final Integer MAX_NUMBER = 1000;

    /**
     * 处理excel
     *
     * @param addressImportDTOS
     */
    @Override
    public void handleExcel(List<AddressImportDTO> addressImportDTOS) {
        long start = System.currentTimeMillis();
        //java8 并发流对list进行切割
        int limit = countStep(addressImportDTOS.size());
        List<List<AddressImportDTO>> lists = Stream.iterate(0, n -> n + 1)
                .limit(limit).parallel()
                .map(a -> addressImportDTOS.stream().skip(a * MAX_NUMBER).limit(MAX_NUMBER).parallel().collect(Collectors.toList()))
                .collect(Collectors.toList());
        long end = System.currentTimeMillis();
        log.info("对list进行切割花费的时间为:{} 毫秒", end - start);

        //多线程入库，并返回入库失败的list
        long startDb = System.currentTimeMillis();
        List<CompletableFuture<AddressImportErrorDTO>> completableFutures = lists.stream().map(list -> CompletableFuture.supplyAsync(() -> {
            AddressImportErrorDTO errorDTO = new AddressImportErrorDTO();
            errorDTO.setFlag(Boolean.TRUE);
            insertDb(list);
            return errorDTO;
        }, poolExecutor).exceptionally(throwable -> {
            //如果出现异常
            AddressImportErrorDTO errorDTO = new AddressImportErrorDTO();
            errorDTO.setFlag(Boolean.FALSE);
            errorDTO.setList(list);
            return errorDTO;
        })).collect(Collectors.toList());

        List<AddressImportErrorDTO> errorDTOS = sequence(completableFutures).join();
        List<AddressImportErrorDTO> errorList = errorDTOS.stream().filter(e -> e.getFlag().equals(false)).collect(Collectors.toList());
        System.out.println(errorList.size());
        long endDb = System.currentTimeMillis();
        log.info("入库耗时:{} 毫秒", endDb - startDb);
    }

    /**
     * 构造结果集
     *
     * @param futuresList
     * @param <T>
     * @return
     */
    private <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futuresList) {
        return CompletableFuture.allOf(futuresList.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> futuresList.stream()
                        .map(CompletableFuture::join)
                        .collect(toList())
                );

    }

    /**
     * 计算切分次数
     */
    private static Integer countStep(Integer size) {
        return (size + MAX_NUMBER - 1) / MAX_NUMBER;
    }


    /**
     * 原生jdbc入库操作
     *
     * @param list
     */
    private void insertDb(List<AddressImportDTO> list) {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            Class.forName(AddressConstant.CLASS_NAME);
            conn = DriverManager.getConnection(AddressConstant.URL, AddressConstant.USER_NAME, AddressConstant.PASS_WORD);
            String sql = AddressConstant.ADDRESS_SQL;
            pstm = conn.prepareStatement(sql);
            conn.setAutoCommit(false);
            for (AddressImportDTO importDTO : list) {
                pstm.setString(1, importDTO.getSendCompany());
                pstm.setString(2, importDTO.getSendPerson());
                pstm.setString(3, importDTO.getSendMobile());
                pstm.setString(4, importDTO.getSendProvince());
                pstm.setString(5, importDTO.getSendCity());
                pstm.setString(6, importDTO.getSendAddress());
                pstm.addBatch();
            }

            //模拟异常
//            boolean b = new Random().nextInt() % 2 >= 0;
//            if (b) {
//                System.out.println(1 / 0);
//            }

            pstm.executeBatch();
            conn.commit();
        } catch (Exception e) {
            log.error("异常=================>>>>:{}", e.getMessage());
            try {
                if (null != conn) {
                    conn.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
