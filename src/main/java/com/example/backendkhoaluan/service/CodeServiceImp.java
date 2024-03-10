package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.entities.Code;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.repository.CodesRepository;
import com.example.backendkhoaluan.service.imp.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CodeServiceImp implements CodeService {
    @Autowired
    private CodesRepository codesRepository;

    @Override
    @Transactional
    public void deleteAll(List<Code> codeList) {
        try {
            codesRepository.deleteAll(codeList);
        }catch (Exception e){
            throw new DeleteException("Xóa code thất bại",e.getLocalizedMessage());
        }
    }
}
