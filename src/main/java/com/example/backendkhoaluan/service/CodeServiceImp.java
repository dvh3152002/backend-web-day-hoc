package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.entities.Codes;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.repository.CodesRepository;
import com.example.backendkhoaluan.service.imp.CodeService;
import com.example.backendkhoaluan.utils.JavaSourceFromString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

@Service
public class CodeServiceImp implements CodeService {
    @Autowired
    private CodesRepository codesRepository;

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
        String s = null;
        try {
            // Biên dịch đoạn mã nguồn
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StringWriter writer = new StringWriter();
            PrintWriter out = new PrintWriter(writer);
            out.println(code);
            out.close();
            JavaCompiler.CompilationTask task = compiler.getTask(null, null, null, null, null, List.of(new JavaSourceFromString("Main", writer.toString())));
            boolean result = task.call();

            if (result) {
                System.out.println("Compilation successful");

                // Thực thi class vừa biên dịch
                Process process = Runtime.getRuntime().exec("java Main");

                // Đọc kết quả từ luồng đầu ra của quá trình thực thi
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (s != null) {
                        s += "\n" + line;
                    } else {
                        s = line;
                    }
                }

                // Đọc kết quả từ luồng lỗi của quá trình thực thi
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((line = errorReader.readLine()) != null) {
                    System.out.println("Error: " + line);
                }

                // Chờ quá trình thực thi kết thúc
                int exitCode = process.waitFor();
            } else {
                
                s = "Compilation failed: " + result;
            }
            return s;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
