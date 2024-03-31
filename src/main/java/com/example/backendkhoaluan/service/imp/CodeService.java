package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.CategoriesDTO;
import com.example.backendkhoaluan.dto.CodesDTO;
import com.example.backendkhoaluan.entities.Codes;
import com.example.backendkhoaluan.payload.request.CodeRequest;
import com.example.backendkhoaluan.payload.request.GetCodeRequest;
import com.example.backendkhoaluan.repository.CustomCodeQuery;
import com.example.backendkhoaluan.repository.CustomeLessonQuery;

import java.util.List;

public interface CodeService {
    public void deleteAll(List<Codes> codesList);

    String runCode(String code);

    List<CodesDTO> getAll(CustomCodeQuery.CodeFilterParam param);
    void deleteCode(int id);
    CodesDTO getById(int id);
    void save(CodeRequest request);
    void updateCode(int id,CodeRequest request);
}
