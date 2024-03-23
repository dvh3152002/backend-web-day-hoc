package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/code")
public class CodeController {
    @Autowired
    private CodeService codeService;

    @PostMapping("/run")
    public BaseResponse runCode(@RequestParam String data){
        return BaseResponse.success(codeService.runCode(data));
    }
}
