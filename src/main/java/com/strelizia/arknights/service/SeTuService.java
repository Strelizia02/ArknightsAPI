package com.strelizia.arknights.service;

/**
 * @author wangzy
 * @Date 2020/12/23 16:51
 **/
public interface SeTuService {
    String getImageIntoDb(String url, Integer type, String name, Long qq);

    String PrivateGetImageIntoDb(String url, Integer type, Long qq);

    String sendImageByType(Long qq, Long groupId, Integer type, String name, String imageId);

    Integer getAllImageIntoLocal(String dir);

    String deleteSeTuById(Long qq, Long groupId, Integer id);

    String changePictureStat(Long qq, Long groupId, Integer type);
}
