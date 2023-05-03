package com.example.back_end_mobile_project.utils.config.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorInfo {

    private String url;

    private String message;

    public ErrorInfo(HttpServletRequest request, Exception e){
        this.url = request.getRequestURL().toString();
        this.message = e.getMessage();
    }

    final class ErrorMessage{

    }

}
