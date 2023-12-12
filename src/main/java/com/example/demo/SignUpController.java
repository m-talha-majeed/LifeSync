package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import ch.qos.logback.core.joran.conditional.IfAction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.*;
import com.google.api.services.classroom.model.Announcement;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.classroom.model.ListAnnouncementsResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.repository.CrudRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.ListAnnouncementsResponse;

import static com.example.demo.GoogleClassroom.buildClassroomService;
import org.springframework.jdbc.*;
@Controller
@RequestMapping(path="/LifeSync")
public class SignUpController {
    @Autowired
    private final LoginInfo l;

    public SignUpController(LoginInfo l) {
        this.l = l;
    }

    @PostMapping("/signup")
    public String processSignUpForm(@RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    @RequestParam("email") String email,
                                    @RequestParam("fullName") String fullName) {
        List<LogInData> log=l.findAllByUsernameAndPassword(username,password);
        if(!log.isEmpty()){
            return "/SignUp";
        }
        else{
            LogInData a = new LogInData();
            a.setPassword(password);
            a.setName(fullName);
            a.setEmail(email);
            a.setUsername(username);
            a.setExpenses(0);
            a.setBudget(0);
            a.setIncome(0);
            a.setToken("");
            a.setSavings(0);
            a.setInvestments(0);
            a.setIp("");
            a.setCity("");
            a.setRegion("");
            a.setCountry("");
            a.setLatitude(0);
            a.setLongitude(0);
            a.setTimezone("");
            a.setPostal("");
            a.setWeatherCity("");
            a.setWeatherRegion("");
            a.setWeatherCountry("");
            a.setLastUpdated("");
            a.setTemperatureF(0);
            a.setTemperatureC(0);
            a.setCondition("");
            a.setClassroomCredentials("");
            a.setGroceryType("");
            a.setGroceryTitle("");
            a.setGroceryDesc("");
            a.setGroceryAmount(0);
            l.save(a);
            return "/LogIn";
        }
    }
    @RequestMapping("/signup")
    public String showSignUpForm(){
        return "SignUp";
    }
    @RequestMapping("/SignUp")
    public String showSignUp(){
        return "SignUp";
    }
}
