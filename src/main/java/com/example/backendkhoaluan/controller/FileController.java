package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.CoursesDTO;
import com.example.backendkhoaluan.dto.LessonsDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.Lessons;
import com.example.backendkhoaluan.entities.OrderDetail;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.exception.FileException;
import com.example.backendkhoaluan.service.imp.CloudinaryService;
import com.example.backendkhoaluan.service.imp.CourseService;
import com.example.backendkhoaluan.service.imp.FilesStorageService;
import com.example.backendkhoaluan.service.imp.LessonService;
import com.example.backendkhoaluan.utils.HelperUtils;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.google.gson.Gson;
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
import java.util.Set;

@RestController
@RequestMapping("/api/file")
public class FileController {
    @Autowired
    private FilesStorageService filesStorageService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    private final Gson gson = new Gson();

    @Value("${root.path.image}")
    private String pathImg;

    @Value("${root.path.video}")
    private String pathVideo;

    @GetMapping("/image/{fileName}")
    public ResponseEntity<?> downloadImageFile(@PathVariable String fileName) {
        Resource resource = filesStorageService.loadImg(pathImg, fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping(value = "/video/{id}", produces = "video/mp4")
    public void downloadVideoFile(@PathVariable int id,
                                  HttpServletResponse response,
                                  @RequestHeader("Authorization") String header) {
        Set<String> roles = HelperUtils.getAuthorities();
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);
        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        LessonsDTO lessonsDTO = lessonService.getLessonsById(id);
        if (roles.contains("ROLE_ADMIN")) {
            getVideo(lessonsDTO, response);
        } else {
            CoursesDTO coursesDTO = courseService.getCourseById(lessonsDTO.getIdCourse());
            if (coursesDTO.isFree()) {
                getVideo(lessonsDTO, response);
            } else {
                if (roles.contains("ROLE_TEACHER")) {

                    if (coursesDTO.getTeacher().getId() == user.getId()) {
                        getVideo(lessonsDTO, response);
                    }
                } else if (roles.contains("ROLE_USER")) {
                    if (courseService.isCoursePurchased(user.getId(), lessonsDTO.getIdCourse())) {
                        getVideo(lessonsDTO, response);
                    }
                }
            }

        }
    }

    private void getVideo(LessonsDTO lessonsDTO, HttpServletResponse response) {
        try {
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
        String url = cloudinaryService.uploadFile(file);
        return ResponseEntity.ok().body(cloudinaryService.getImageUrl(url));
    }
}
