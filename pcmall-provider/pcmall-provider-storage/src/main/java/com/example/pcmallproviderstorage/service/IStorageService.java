package com.example.pcmallproviderstorage.service;

import com.example.pcmallcommon.model.Storage;

import java.util.List;

public interface IStorageService {
    List<Storage> getStorageList(Integer gid, Integer currentPage, Integer pageSize);

    Long getTotalCount(Integer gid);

    void inboundDelivery(Storage storage);

    void outboundDelivery(Storage storage);
}
