package edu.iselab.gitcloner.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.iselab.gitcloner.util.AlertUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(Exception.class)
    public String handleError(HttpServletRequest req, Exception ex, RedirectAttributes ra) {
        
        log.error("Request: " + req.getRequestURL() + " raised " + ex);

        AlertUtils.error(ra, "alert.exception", ex.getMessage());

        return "redirect:/";
    }
}
