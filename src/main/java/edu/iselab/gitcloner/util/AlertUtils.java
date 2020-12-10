package edu.iselab.gitcloner.util;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class AlertUtils {
    
    public static void message(RedirectAttributes ra, String type, String title, String message, Object... parameters) {
        ra.addFlashAttribute("alertType", "alert alert-dismissible " + type);
        ra.addFlashAttribute("alertTitle", title);
        ra.addFlashAttribute("alertMessage", message);
        ra.addFlashAttribute("alertMessageParameters", parameters);
    }

    public static void error(RedirectAttributes ra, String message, Object... parameters) {
        message(ra, "alert-danger", "Ooops!", message, parameters);
    }
    
    public static void success(RedirectAttributes ra, String message, Object... parameters) {
        message(ra, "alert-success", "Success!", message, parameters);
    }
    
    public static void warning(RedirectAttributes ra, String message, Object... parameters) {
        message(ra, "alert-warning", "Warning!", message, parameters);
    }
}
