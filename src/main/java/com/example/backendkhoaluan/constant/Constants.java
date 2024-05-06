package com.example.backendkhoaluan.constant;

public interface Constants {
    interface ErrorMessageUserValidation {
        String INVALID_EMAIL="Email không hợp lệ";
        String EMAIL_NOT_BLANK="Email không được để trống";
        String PASSWORD_SIZE="Mật khẩu phải từ 6 đến 15 ký tự";
        String FULLNAME_NOT_SPECIAL_CHARATERS = "Họ tên không được có ký tự đặc biệt";
        String FULLNAME_NOT_BLANK="Họ tên không được để trống";
        String ADDRESS_NOT_BLANK="Địa chỉ không được để trống";
        String START_SIZE="Số trang phải bắt đầu từ 0";
        String LIMIT_SIZE = "Số lượng người dùng trong một trang là từ 5 đến 30 người";
        String NOT_FIND_USER_BY_ID = "Không tìm thấy người dùng có ID là: ";
        String EMAIL_IS_EXIST = "Email đã tồn tại";
        String OTP_NOT_BLANK = "OTP không được để trống";
        String NOT_FIND_USER_BY_EMAIL= "Không tìm thấy người dùng có email là: ";
    }

    interface ErrorMessageCourseValidation {
        String NAME_NOT_BLANK="Tên khóa học không được để trống";
        String PRICE_NOT_BLANK="Giá khóa học không được để trống";
        String NAME_NOT_SPECIAL_CHARATERS="Tên khóa học không được có kí tự đặc biệt";
        String DESCRIPTION_NOT_BLANK="Mô tả không được để trống";
        //        String IMAGE_NOT_BLANK="Ảnh khóa học không được để trống";
        String USER_ID_NOT_BLANK="Thiếu người dạy khóa học";
        String CATEGORY_ID_NOT_BLANK="Thiếu danh mục khóa học";
        String START_SIZE="Số trang phải bắt đầu từ 0";
        String LIMIT_SIZE = "Số lượng khóa học trong một trang là từ 3 đến 30 khóa học";
        String NOT_FIND_COURSE_BY_ID = "Không tìm thấy khóa học có ID là: ";
    }

    interface ErrorMessagePostValidation {
        String TITLE_NOT_BLANK="Tiêu đề bài viết không được để trống";
        String DESCRIPTION_NOT_BLANK="Mô tả bài viết không được để trống";
        String START_SIZE="Số trang phải bắt đầu từ 0";
        String LIMIT_SIZE = "Số lượng bài viết trong một trang là từ 5 đến 50 bài viết";
        String NOT_FIND_POST_BY_ID = "Không tìm thấy bài viết có ID là: ";
    }

    interface ErrorMessageNewValidation {
        String TITLE_NOT_BLANK="Tiêu đề tin tức không được để trống";
        String DESCRIPTION_NOT_BLANK="Mô tả tin tức không được để trống";
        String START_SIZE="Số trang phải bắt đầu từ 0";
        String LIMIT_SIZE = "Số lượng tin tức trong một trang là từ 5 đến 50 bài viết";
        String NOT_FIND_NEW_BY_ID = "Không tìm thấy tin tức có ID là: ";
    }

    interface ErrorMessageCodeValidation {
        String CODE_NOT_BLANK="Code không được để trống";
        String LANGUAGE_NOT_BLANK="Ngôn ngữ lập trình không được để trống";

        String ID_POST_NOT_BLANK="ID bài viết không được để trống";

        String NOT_FIND_CODE_BY_ID = "Không tìm thấy code có ID là: ";
    }

    interface ErrorMessageRoleValidation {
        String NOT_FIND_ROLE_BY_ID = "Không tìm thấy role có ID là: ";
    }

    interface ErrorMessageVerifyValidation {
        String OTP_NOT_BLANK = "OTP không được để trống";

        String EMAIL_NOT_BLANK = "Email không được để trống";
    }

    interface ErrorMessageRatingValidation {
        String CONTENT_NOT_BLANK="Nhận xét khóa học không được để trống";
        String COURSE_NOT_BLANK="Không có ID khóa học";
        String USER_NOT_BLANK="Không có ID người dùng";
        String POINT_NOT_BLANK="Không có điểm đánh giá";
        String START_SIZE="Số trang phải bắt đầu từ 0";
        String LIMIT_SIZE = "Số lượng bình luận trong một trang là từ 5 đến 50 bình luận";
        String NOT_FIND_RATING_BY_ID = "Không tìm thấy bình luận có ID là: ";
    }

    interface ErrorMessageCategoryValidation {
        String TITLE_NOT_BLANK="Tiêu đề bài viết không được để trống";
        String DESCRIPTION_NOT_BLANK="Mô tả bài viết không được để trống";
        String START_SIZE="Số trang phải bắt đầu từ 0";
        String LIMIT_SIZE = "Số lượng bài viết trong một trang là từ 5 đến 50 bài viết";
        String NOT_FIND_CATEGORY_BY_ID = "Không tìm thấy danh mục có ID là: ";
    }

    interface ErrorMessageLessonValidation {
        String TITLE_NOT_BLANK="Tiêu đề bài học không được để trống";
        String START_SIZE="Số trang phải bắt đầu từ 0";
        String COURSE_ID_NOT_BLANK="Thiếu khóa học";
        String LIMIT_SIZE = "Số lượng bài học trong một trang là từ 5 đến 50 bài học";
        String NOT_FIND_LESSON_BY_ID = "Không tìm thấy bài học có ID là: ";
    }

    interface ErrorMessageOrderValidation {
        String NOT_FIND_ORDER_BY_ID = "Không tìm thấy đơn hàng có ID là: ";
        String START_SIZE="Số trang phải bắt đầu từ 0";
        String LIMIT_SIZE = "Số lượng bài viết trong một trang là từ 5 đến 50 bài viết";
    }

    interface SortType {
        String DESC = "DESC";
        String ASC = "ASC";
    }
}
