package com.ocean.service;

import com.ocean.dto.AddressImportDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: zhouliang
 * @Date: 2019/6/12 18:13
 */
public class A {
    //按每3个一组分割
    private static final Integer MAX_NUMBER = 10;

    public static void main(String[] args) {

        List<AddressImportDTO> addressImportDTOS = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            AddressImportDTO importDTO = new AddressImportDTO();
            importDTO.setSendAddress("华南路:" + i);
            addressImportDTOS.add(importDTO);
        }
        int limit = countStep(addressImportDTOS.size());
        List<List<AddressImportDTO>> collect = Stream.iterate(0, n -> n + 1)
                .limit(limit).parallel()
                .map(a -> addressImportDTOS.stream().skip(a * MAX_NUMBER).limit(MAX_NUMBER).parallel().collect(Collectors.toList()))
                .collect(Collectors.toList());
        System.out.println(collect);


//        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
//        int limit = countStep(list.size());
//        //方法一：使用流遍历操作
//        List<List<Integer>> mglist = new ArrayList<>();
//        Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> {
//            mglist.add(list.stream().skip(i * MAX_NUMBER).limit(MAX_NUMBER).collect(Collectors.toList()));
//        });
//
//        System.out.println(mglist);
//
//        //方法二：获取分割后的集合
//        List<List<Integer>> splitList = Stream.iterate(0, n -> n + 1)
//                .limit(limit).parallel()
//                .map(a -> list.stream().skip(a * MAX_NUMBER).limit(MAX_NUMBER).parallel().collect(Collectors.toList()))
//                .collect(Collectors.toList());
//
//        System.out.println(splitList);
    }

    /**
     * 计算切分次数
     */
    private static Integer countStep(Integer size) {
        return (size + MAX_NUMBER - 1) / MAX_NUMBER;
    }

}
