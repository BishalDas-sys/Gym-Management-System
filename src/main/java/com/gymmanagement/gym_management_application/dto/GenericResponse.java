package com.gymmanagement.gym_management_application.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class GenericResponse {
    private String status;
    private String message;
    private String errorCode;
    private Object data;

    public GenericResponse success(String msg, Object dt) {
        this.status = "success";
        this.message = msg;
        this.data = dt;
        return this;
    }

    public GenericResponse failure(String msg, String errCode) {
        this.status = "failure";
        this.message = msg;
        this.errorCode = errCode;
        return this;
    }


}
