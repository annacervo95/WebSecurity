package com.example.progettoSS;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns("/admin/getprofile", "/user/getprofile", 
                "/admin/logout","/user/logout", "/admin/addplant","/admin/uploadplant", "/user/getplants", "/admin/getplants","/admin/deleteplant/", "/user/addbooking","/admin/getbookings","/user/getbookings",
                "/admin/deletebooking/", "/user/aggiungiprodotto") // Aggiungi qui i percorsi che desideri proteggere
                .excludePathPatterns("/", "/login/register", "/userLogin", "/user/registerUser","/access-denied"); // Escludi i percorsi per la pagina di login e registrazione
   

    }
}

