package com.example.backendkhoaluan.constant;

public class ErrorCodeDefs {
    public static final int ERR_OK = 0;
    public static final int ERR_SYSTEM = ERR_OK + 1;
    public static final int ERR_VALIDATION = ERR_OK + 2;
    public static final int ERR_OBJECT_NOT_FOUND = ERR_OK + 3;
    public static final int ERR_ACCOUNT_LOCKED = ERR_OK + 4;
    public static final int ERR_UNAUTHORIZED = ERR_OK + 5;
    public static final int ERR_TOKEN_INVALID = ERR_OK + 6;
    public static final int ERR_INTERNAL_SERVICE = ERR_OK + 7;
    public static final int ERR_OTHER = ERR_OK + 8;
    public static final int ERR_PARTNERID_INVALID = ERR_OK + 9;
    public static final int ERR_PARTNER_APIKEY_INVALID = ERR_OK + 10;

    public static final int ERR_FILE = ERR_OK + 11;
    public static final int ERR_RUN_CODE = ERR_OK + 12;
    public static final int ERR_PAYMENT_FAILED = ERR_OK + 13;

    public static final int ERR_EMAIL_FAILED = ERR_OK + 14;

    public static final int ERR_BAD_REQUEST = 400;
    public static final int ERR_PERMISSION_INVALID = 401;
    public static final int ERR_ACCESS_FORBIDDEN = 403;
    public static final int ERR_REQUEST_NOT_FOUND = 404;
    public static final int ERR_METHOD_NOT_ALLOWED = 405;
    public static final int ERR_METHOD_FORMAT_NOT_VALID = 406;
    public static final int ERR_SERVER_ERROR = 500;
    public static final int ERR_GATEWAY_ERROR = 502;
    public static final int ERR_SERVER_UNAVAILABLE = 503;
    public static final int ERR_GATEWAY_TIMEOUT = 504;

    public static final int ERR_RESOUCES_DELETED = 410;
    public static final int ERR_VALIDATE_ERROR_OCCURED = 422;
    public static final int ERR_REFRESH_TOKEN_INVALID = 99;
    public static final int ERR_HEADER_TOKEN_REQUIRED = 98;

    public static String getMessage(int errorCode) {
        switch (errorCode) {
            case ERR_OK:
                return "Thành công";
            case ERR_SYSTEM:
            case ERR_SERVER_ERROR:
                return "Lỗi không xác định";
            case ERR_VALIDATION:
                return "Dữ liệu/tham số không hợp lệ";
            case ERR_OBJECT_NOT_FOUND:
                return "Không tìm thấy dữ liệu";
            case ERR_ACCOUNT_LOCKED:
                return "Tài khoản đã bị khóa, không thể truy cập hệ thống";
            case ERR_UNAUTHORIZED:
                return "Không được truy cập hệ thống do chưa xác thực, hoặc xác thực không thành công";
            case ERR_TOKEN_INVALID:
                return "Token không đúng hoặc đã hết hạn";
            case ERR_REFRESH_TOKEN_INVALID:
                return "Refresh token không đúng hoặc đã hết hạn";
            case ERR_HEADER_TOKEN_REQUIRED:
                return "Header token is required";
            case ERR_INTERNAL_SERVICE:
                return "Lỗi hệ thống nội bộ";
            case ERR_PARTNERID_INVALID:
                return "Mã đối tác không hợp lệ hoặc đã inactive";
            case ERR_PARTNER_APIKEY_INVALID:
                return "Mã api key không hợp lệ hoặc đã inactive";
            case ERR_PERMISSION_INVALID:
                return "Bạn không có quyền thao tác chức năng này";
            case ERR_ACCESS_FORBIDDEN:
                return "Truy cập không hợp lệ";
            case ERR_REQUEST_NOT_FOUND:
                return "End point url không hợp lệ !";
            case ERR_METHOD_NOT_ALLOWED:
                return "Phương thức không hợp lệ";
            case ERR_METHOD_FORMAT_NOT_VALID:
                return "Định dạng dữ liệu không hợp lệ";
            case ERR_GATEWAY_ERROR:
                return "Gateway error";
            case ERR_SERVER_UNAVAILABLE:
                return "Hệ thống không khả dụng";
            case ERR_GATEWAY_TIMEOUT:
                return "The gateway timed out";
            case ERR_FILE:
                return "Lỗi file";
            case ERR_RUN_CODE:
                return "Lỗi chạy code";
            case ERR_PAYMENT_FAILED:
                return "Lỗi thanh toán";
            case ERR_EMAIL_FAILED:
                return "Lỗi gửi mail";
            case ERR_OTHER:
                return "Các lỗi khác";
            case ERR_BAD_REQUEST:
                return "Tham số không hợp lệ";
            case ERR_RESOUCES_DELETED:
                return "The requested resource is permanently deleted and will no longer be available";
            case ERR_VALIDATE_ERROR_OCCURED:
                return "When creating an object, a validation error occurred";
            default:
                return "Lỗi không xác định";

        }
    }
}
