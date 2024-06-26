package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.CodesDTO;
import com.example.backendkhoaluan.payload.request.CodeRequest;
import com.example.backendkhoaluan.payload.request.GetCodeRequest;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.CodeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/code")
public class CodeController {
    @Autowired
    private CodeService codeService;

    @PostMapping("/run")
    public ResponseEntity<?> runCode(@RequestParam String data) {

        return BaseResponse.success(codeService.runCode(data));
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@ModelAttribute GetCodeRequest request) {
        List<CodesDTO> list = codeService.getAll(request);
        return BaseResponse.successListData(list, list.size());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return BaseResponse.success(codeService.getById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> createCode(@Valid @RequestBody CodeRequest request) {
        codeService.save(request);
        return BaseResponse.success("Thêm code thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCode(@PathVariable int id, @Valid @RequestBody CodeRequest request) {
        codeService.updateCode(id, request);
        return BaseResponse.success("Cập nhật code thành công");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCode(@PathVariable int id) {
        codeService.deleteCode(id);
        return BaseResponse.success("Xóa code thành công");
    }
}
