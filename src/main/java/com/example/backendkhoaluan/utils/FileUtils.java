package com.example.backendkhoaluan.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {
    public static String setFileName(String input){
        int lastDotIndex = input.lastIndexOf(".");
        String extension = input.substring(lastDotIndex + 1);
        String name=SlugUtils.toSlug(input.substring(0,lastDotIndex))+DateUtils.getTimeLabel()+"."+extension;
        return name;
    }

//    public static String extractFileExtension(MultipartFile part) {
//        String contentDisp = part.getContentType();
////        int indexOfDot = contentDisp.lastIndexOf(".");
////        return contentDisp.substring(indexOfDot, contentDisp.length() - 1);
//        return contentDisp;
//    }
}
