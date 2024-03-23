package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.entities.Codes;

import java.util.List;

public interface CodeService {
    public void deleteAll(List<Codes> codesList);

    String runCode(String code);
}
