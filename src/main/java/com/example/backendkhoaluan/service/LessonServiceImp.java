package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.dto.LessonsDTO;
import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.entities.Lessons;
import com.example.backendkhoaluan.exception.DataNotFoundException;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.exception.InsertException;
import com.example.backendkhoaluan.payload.request.LessonRequest;
import com.example.backendkhoaluan.repository.CustomeLessonQuery;
import com.example.backendkhoaluan.repository.LessonsRepository;
import com.example.backendkhoaluan.service.imp.FileUploadCloudinaryService;
import com.example.backendkhoaluan.service.imp.FilesStorageService;
import com.example.backendkhoaluan.service.imp.LessonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImp implements LessonService {
    @Autowired
    private FileUploadCloudinaryService cloudinaryService;

    @Autowired
    private LessonsRepository lessonsRepository;

    private ModelMapper modelMapper=new ModelMapper();

    @Override
    public Page<Lessons> getListLesson(CustomeLessonQuery.LessonFilterParam param, PageRequest pageRequest) {
        Specification<Lessons> specification=CustomeLessonQuery.getFilterLesson(param);
        return lessonsRepository.findAll(specification,pageRequest);
    }

    @Override
    public LessonsDTO getLessonsById(int id) {
        Optional<Lessons> lessonsOptional=lessonsRepository.findById(id);
        if(!lessonsOptional.isPresent()){
            throw new DataNotFoundException(Constants.ErrorMessageLessonValidation.NOT_FIND_LESSON_BY_ID+id);
        }
        Lessons lessons=lessonsOptional.get();
        LessonsDTO lessonsDTO=new LessonsDTO();
        lessonsDTO.setId(lessons.getId());
        lessonsDTO.setTitle(lessons.getTitle());
        lessonsDTO.setVideo("http://localhost:8081/api/file/video/" +lessons.getVideo());

        return lessonsDTO;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,InsertException.class})
    public void save(LessonRequest request, MultipartFile video) {
        try {
            Lessons lessons=new Lessons();
            lessons.setTitle(request.getTitle());

            String videoName=cloudinaryService.uploadFile(video);
            lessons.setVideo(videoName);

            Courses courses=new Courses();
            courses.setId(request.getIdCourse());
            lessons.setCourse(courses);

            lessonsRepository.save(lessons);
        } catch (Exception e) {
            throw new InsertException("Thêm khóa học thất bại: ", e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public void updateLesson(int id, LessonRequest request, MultipartFile video) {
        try {
            Optional<Lessons> lessonsOptional=lessonsRepository.findById(id);
            if(!lessonsOptional.isPresent()){
                throw new DataNotFoundException(Constants.ErrorMessageLessonValidation.NOT_FIND_LESSON_BY_ID+id);
            }
            Lessons lessons=lessonsOptional.get();
            lessons.setTitle(request.getTitle());

            cloudinaryService.deleteVideo(lessons.getVideo());
            String videoName=cloudinaryService.uploadFile(video);
            lessons.setVideo(videoName);

            Courses courses=new Courses();
            courses.setId(request.getIdCourse());
            lessons.setCourse(courses);

            lessonsRepository.save(lessons);
        } catch (Exception e) {
            throw new InsertException("Cập nhật khóa học thất bại: ", e.getLocalizedMessage());
        }
    }

    @Override
    public void deleteLesson(int id) {
        try {
            Optional<Lessons> lessonsOptional=lessonsRepository.findById(id);
            if(!lessonsOptional.isPresent()){
                throw new DataNotFoundException(Constants.ErrorMessageLessonValidation.NOT_FIND_LESSON_BY_ID+id);
            }
            Lessons lessons=lessonsOptional.get();
            lessonsRepository.delete(lessons);
            cloudinaryService.deleteVideo(lessons.getVideo());
        }catch (Exception e){
            throw new DeleteException("Xóa bài học thất bại",e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public void deleteAll(List<Lessons> list) {
        try {
            for(Lessons lessons:list){
                cloudinaryService.deleteVideo(lessons.getVideo());
            }
            lessonsRepository.deleteAll(list);
        }catch (Exception e){
            throw new DeleteException("Xóa danh sách bài học thất bại",e.getLocalizedMessage());
        }
    }
}
