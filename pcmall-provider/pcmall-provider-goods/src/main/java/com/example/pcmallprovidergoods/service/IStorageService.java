package com.example.pcmallprovidergoods.service;

import com.example.pcmallcommon.model.Storage;

import java.util.List;

public interface IStorageService {
    public List<Storage> getStorageList(Integer gid, Integer currentPage, Integer pageSize);

    public Long getTotalCount(Integer gid);

    public void inboundDelivery(Storage storage);

    public void outboundDelivery(Storage storage);
}
