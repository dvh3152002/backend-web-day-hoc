package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.RatingCourseDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.RatingCourse;
import com.example.backendkhoaluan.payload.request.CreateRatingRequest;
import com.example.backendkhoaluan.payload.request.GetRatingCourseRequest;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.CloudinaryService;
import com.example.backendkhoaluan.service.imp.RatingService;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/rating")
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    @Autowired
    private CloudinaryService cloudinaryService;

    private ModelMapper modelMapper=new ModelMapper();

    private final Gson gson = new Gson();

    @GetMapping("")
    public BaseResponse getListRating(GetRatingCourseRequest request){
        log.info("request: {}",request);
        Page<RatingCourse> page=ratingService.getAllRating(request, PageRequest.of(request.getStart(),request.getLimit()));
        return BaseResponse.successListData(page.stream().map(e->{
            RatingCourseDTO rating=modelMapper.map(e,RatingCourseDTO.class);
            UsersDTO usersDTO=new UsersDTO();
            usersDTO.setFullname(e.getUser().getFullname());
            if(e.getUser().getAvatar()!=null){
                usersDTO.setAvatar(cloudinaryService.getImageUrl(e.getUser().getAvatar()));
            }
            rating.setUser(usersDTO);
            return rating;
        }).toList(),(int) page.getTotalElements());
    }

    @PostMapping("")
    public BaseResponse createRating(@RequestBody CreateRatingRequest request,
                                     @RequestHeader("Authorization") String header){
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        request.setIdUser(user.getId());
        ratingService.createRating(request);
        return BaseResponse.success("Đánh giá thành công");
    }

    @PutMapping("/{id}")
    public BaseResponse deleteRating(@PathVariable int id,@RequestBody CreateRatingRequest request){
        log.info("id: {}",id);
        ratingService.updateRating(id,request);
        return BaseResponse.success("Cập nhật đánh giá thành công");
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteRating(@PathVariable int id){
        log.info("id: {}",id);
        return BaseResponse.success(ratingService.deleteRating(id));
    }

    @GetMapping("/{idCourse}")
    public BaseResponse getRating(@PathVariable int idCourse,
                                  @RequestHeader("Authorization") String header){
        log.info("id: {}",idCourse);
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);

        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        return BaseResponse.success(ratingService.getRating(idCourse,user.getId()));
    }
}
