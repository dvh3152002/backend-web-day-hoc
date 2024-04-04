package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.LessonsDTO;
import com.example.backendkhoaluan.entities.Lessons;
import com.example.backendkhoaluan.entities.OrderDetail;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.exception.FileException;
import com.example.backendkhoaluan.service.imp.CloudinaryService;
import com.example.backendkhoaluan.service.imp.FilesStorageService;
import com.example.backendkhoaluan.service.imp.LessonService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/file")
public class FileController {
    @Autowired
    private FilesStorageService filesStorageService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private LessonService lessonService;

    @Value("${root.path.image}")
    private String pathImg;

    @Value("${root.path.video}")
    private String pathVideo;

    @GetMapping("/image/{fileName}")
    public ResponseEntity<?> downloadImageFile(@PathVariable String fileName) {
        Resource resource=filesStorageService.loadImg(pathImg,fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping(value = "/video/{id}/{idCourse}", produces = "video/mp4")
    public void downloadVideoFile(@PathVariable int id, HttpServletResponse response) {
        try {
            LessonsDTO lessonsDTO = lessonService.getLessonsById(id);
            InputStream resource = filesStorageService.loadVideo(pathVideo, lessonsDTO.getVideo());

            // Đặt kiểu MIME của video là video/mp4
            response.setContentType("video/mp4");

            // Sao chép dữ liệu từ InputStream vào OutputStream của phản hồi
            StreamUtils.copy(resource, response.getOutputStream());
        } catch (Exception e) {
            throw new FileException(e.getLocalizedMessage());
        }
    }

    private boolean hasAccessToVideo(Integer courseId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Lấy thông tin người dùng từ xác thực
        User user = (User) authentication.getPrincipal();
        if (user == null) {
            return false;
        }

        // Kiểm tra xem người dùng đã mua khóa học này chưa
//        for (OrderDetail orderDetail : user.getListOrders().getOrderDetails()) {
//            if (orderDetail.getCourse().getId().equals(courseId)) {
//                return true; // Người dùng đã mua khóa học này, có quyền truy cập video
//            }
//        }

        return false; // Người dùng chưa mua khóa học này, không có quyền truy cập video
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file) throws IOException {
        String url=cloudinaryService.uploadFile(file);
        return ResponseEntity.ok().body(url);
    }
}
