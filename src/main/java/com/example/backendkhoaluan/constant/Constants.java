package com.example.backendkhoaluan.constant;

public interface Constants {
    interface ErrorMessageUserValidation {
        String INVALID_EMAIL="Email không hợp lệ";
        String EMAIL_NOT_BLANK="Email không được để trống";
        String PASSWORD_SIZE="Mật khẩu phải từ 6 đến 15 ký tự";
        String PASSWORD_NOT_BLANK="Mật khẩu không được để trống";
        String FULLNAME_NOT_SPECIAL_CHARATERS = "Họ tên không được có ký tự đặc biệt";
        String FULLNAME_NOT_BLANK="Họ tên không được để trống";
        String ADDRESS_NOT_BLANK="Địa chỉ không được để trống";
        String START_SIZE="Số trang phải bắt đầu từ 0";
        String LIMIT_SIZE = "Số lượng người dùng trong một trang là từ 5 đến 30 người";
    }

    interface ErrorMessageCourseValidation {
        String NAME_NOT_BLANK="Tên khóa học không được để trống";
        String PRICE_NOT_BLANK="Giá khóa học không được để trống";
        String NAME_NOT_SPECIAL_CHARATERS="Tên khóa học không được có kí tự đặc biệt";
        String DESCRIPTION_NOT_BLANK="Mô tả không được để trống";
        //        String IMAGE_NOT_BLANK="Ảnh khóa học không được để trống";
        String USER_ID_NOT_BLANK="Thiếu người dạy khóa học";
        String START_SIZE="Số trang phải bắt đầu từ 0";
        String LIMIT_SIZE = "Số lượng khóa học trong một trang là từ 5 đến 30 khóa học";

    }

    interface ErrorMessagePostValidation {
        String TITLE_NOT_BLANK="Tiêu đề bài viết không được để trống";
        String DESCRIPTION_NOT_BLANK="Mô tả bài viết không được để trống";
        String START_SIZE="Số trang phải bắt đầu từ 0";
        String LIMIT_SIZE = "Số lượng bài viết trong một trang là từ 5 đến 50 bài viết";

    }

    interface SortType {
        String DESC = "DESC";
        String ASC = "ASC";
    }
}