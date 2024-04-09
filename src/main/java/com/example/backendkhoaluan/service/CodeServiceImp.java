package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.CategoriesDTO;
import com.example.backendkhoaluan.dto.CodesDTO;
import com.example.backendkhoaluan.entities.Categories;
import com.example.backendkhoaluan.entities.Codes;
import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.exception.*;
import com.example.backendkhoaluan.payload.request.CodeRequest;
import com.example.backendkhoaluan.payload.request.GetCodeRequest;
import com.example.backendkhoaluan.repository.CodesRepository;
import com.example.backendkhoaluan.repository.CustomCodeQuery;
import com.example.backendkhoaluan.repository.CustomCourseQuery;
import com.example.backendkhoaluan.service.imp.CodeService;
import com.example.backendkhoaluan.utils.JavaSourceFromString;
import org.aspectj.apache.bcel.classfile.Code;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.tools.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CodeServiceImp implements CodeService {
    @Autowired
    private CodesRepository codesRepository;

    private final ModelMapper modelMapper = new ModelMapper();
    @Override
    @Transactional
    public void deleteAll(List<Codes> codesList) {
        try {
            codesRepository.deleteAll(codesList);
        } catch (Exception e) {
            throw new DeleteException("Xóa code thất bại", e.getLocalizedMessage());
        }
    }

    @Override
    public String runCode(String code) {
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StringWriter writer = new StringWriter();
            PrintWriter out = new PrintWriter(writer);
            out.println(code);
            out.close();

            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
            JavaFileObject fileObject = new JavaSourceFromString("Main", writer.toString());
            Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(fileObject);
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);

            boolean result = task.call();

            if (result) {
                System.out.println("Compilation successful");

                // Thực thi class vừa biên dịch
                Process process = Runtime.getRuntime().exec("java Main");

                // Đọc kết quả từ luồng đầu ra của quá trình thực thi
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    StringBuilder output = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                    return output.toString();
                }

            } else {
                StringBuilder output = new StringBuilder();
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    String message = String.format("Line %d: %s", diagnostic.getLineNumber(), diagnostic.getMessage(null));
                    System.out.println(message);
                    output.append(message).append("\n");
                }
                throw new RunCodeException(output.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during code execution: " + e.getMessage());
        }
    }

    @Override
    public List<CodesDTO> getAll(CustomCodeQuery.CodeFilterParam param) {
        Specification<Codes> specification = CustomCodeQuery.getFilterCourse(param);
        List<Codes> list = codesRepository.findAll(specification);
        List<CodesDTO> dtoList = list.stream().map(data -> modelMapper.map(data, CodesDTO.class))
                .collect(Collectors.toList());
        return dtoList;
    }

    @Override
    @Transactional
    public void deleteCode(int id) {
        try {
            Optional<Codes> codes=codesRepository.findById(id);
            if (!codes.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageCodeValidation.NOT_FIND_CODE_BY_ID + id);
            }
            codesRepository.deleteById(id);
        }catch (Exception e){
            throw new DeleteException("Xóa code thất bại",e.getLocalizedMessage());
        }
    }

    @Override
    public CodesDTO getById(int id) {
        Optional<Codes> codes=codesRepository.findById(id);
        if (!codes.isPresent()) {
            throw new DataNotFoundException(Constants.ErrorMessageCodeValidation.NOT_FIND_CODE_BY_ID + id);
        }
        CodesDTO codesDTO=modelMapper.map(codes.get(),CodesDTO.class);
        return codesDTO;
    }

    @Override
    @Transactional
    public void save(CodeRequest request) {
        try {
            Codes codes = modelMapper.map(request, Codes.class);
            codes.setId(null);
            codesRepository.save(codes);
        }catch (Exception e){
            throw new InsertException("Thêm code thất bại",e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public void updateCode(int id, CodeRequest request) {
        try {
            Optional<Codes> codeOtn=codesRepository.findById(id);
            if (!codeOtn.isPresent()) {
                throw new DataNotFoundException(Constants.ErrorMessageCodeValidation.NOT_FIND_CODE_BY_ID + id);
            }
            Codes code=codeOtn.get();
            code=modelMapper.map(request,Codes.class);
            code.setId(id);
            codesRepository.save(code);
        }catch (Exception e){
            throw new UpdateException("Cập nhật code thất bại",e.getLocalizedMessage());
        }
    }
}
