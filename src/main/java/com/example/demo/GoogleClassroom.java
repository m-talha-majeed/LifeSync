package com.example.demo;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.googleapis.auth.oauth2.*;
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
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.io.*;

import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.ListAnnouncementsResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
public class GoogleClassroom {
    List<CourseData> courseData=new ArrayList<>();
    private static final String APPLICATION_NAME = "LifeSync";
    private static final String CLIENT_SECRET_FILE = "/client_secret.json";
    private static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/classroom.courses.readonly",
            "https://www.googleapis.com/auth/classroom.announcements.readonly",
            "https://www.googleapis.com/auth/classroom.coursework.me.readonly",
            "https://www.googleapis.com/auth/classroom.student-submissions.me.readonly",
            "https://www.googleapis.com/auth/classroom.topics.readonly"
    );
    public GoogleClassroom(){
    }
    public String getAuthorizationUrl() throws Exception {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        GoogleClientSecrets clientSecrets;
        clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(
                GoogleClassroom.class.getResourceAsStream(CLIENT_SECRET_FILE)));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();

        // Specify the port for the redirect URL
        int redirectPort = 8000;
        VerificationCodeReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(redirectPort)
                .build();
        String redirectUri = "http://localhost:8080/LifeSync/getGoogleAuthentication";
        String authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri).build();
        System.out.println("Authorization URL: " + authorizationUrl);
        return authorizationUrl;
    }
    public Classroom deserializeClassroom(String serialized) {
        try {
            byte[] data = Base64.getDecoder().decode(serialized);
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                return (Classroom) ois.readObject();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle the exception as needed
            return null;
        }
    }

    private static Credential authorize(GoogleAuthorizationCodeFlow flow) throws IOException {
        VerificationCodeReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(8080)
                .build();
        AuthorizationCodeInstalledApp authorizationCodeInstalledApp = new AuthorizationCodeInstalledApp(flow, receiver);
        return authorizationCodeInstalledApp.authorize("user");
    }
    public GoogleTokenResponse exchangeCodeForTokens(String authorizationCode) throws IOException, GeneralSecurityException {
        GoogleAuthorizationCodeFlow flow = createAuthorizationFlow();
        return flow.newTokenRequest(authorizationCode)
                .setRedirectUri("http://localhost:8080/LifeSync/getGoogleAuthentication")
                .execute();
    }

    public Credential buildCredentialFromTokens(GoogleTokenResponse tokenResponse) throws IOException, GeneralSecurityException {
        GoogleClientSecrets clientSecrets;
        JsonFactory jsonFactory=new JacksonFactory();
        clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(
                GoogleClassroom.class.getResourceAsStream(CLIENT_SECRET_FILE)));
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .setJsonFactory(JacksonFactory.getDefaultInstance())
                .setClientSecrets(clientSecrets)
                .build()
                .setAccessToken(tokenResponse.getAccessToken())
                .setRefreshToken(tokenResponse.getRefreshToken());

        // Refresh the token to ensure it's valid
        credential.refreshToken();

        return credential;
    }

    private GoogleAuthorizationCodeFlow createAuthorizationFlow() throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory,
                new InputStreamReader(GoogleClassroom.class.getResourceAsStream(CLIENT_SECRET_FILE)));

        return new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, GoogleClassroom.SCOPES)
                .setAccessType("offline")
                .build();
    }

    public static Classroom buildClassroomService(Credential credential) throws Exception {
        // Initialize the HTTP transport and set up the global Classroom instance
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        return new Classroom.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                jsonFactory,
                credential
        )
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static List<Course> getCourses(Classroom classroomService) throws IOException {
        Classroom.Courses.List request = classroomService.courses().list();
        ListCoursesResponse response = request.execute();

        List<Course> allCourses = response.getCourses();
        List<Course> activeCourses = filterArchivedCourses(allCourses);

        return activeCourses;
    }

    private static List<Course> filterArchivedCourses(List<Course> courses) {
        // Filter out archived courses
        return courses.stream()
                .filter(course -> !"ARCHIVED".equals(course.getCourseState()))
                .toList();
    }


    public List<CourseData> getStudentSubmissions(Classroom classroom) throws Exception {

        // Fetch the list of student submissions
        String courseId, courseWorkId;
        List<Course> courses = getCourses(classroom);
        StringBuilder outputBuilder = new StringBuilder();
        List<CourseWork> courseWorkList=new ArrayList<>();
        for (Course course : courses) {
            courseId = course.getId();
            courseWorkList = retrieveCoursework(classroom, courseId);
            if (courseWorkList != null && !courseWorkList.isEmpty()) {
                for (CourseWork courseWork : courseWorkList) {
                    courseWorkId = courseWork.getId();
                    ListStudentSubmissionsResponse response = classroom.courses().courseWork()
                            .studentSubmissions()
                            .list(courseId, courseWorkId)
                            .execute();
                    // Check if response is not null
                    if (response != null) {
                        List<StudentSubmission> studentSubmissions = response.getStudentSubmissions();
                        // Check if studentSubmissions is not null
                        if (studentSubmissions != null) {
                            for (StudentSubmission submission : studentSubmissions) {
                                CourseData a;
                                if (!"TURNED_IN".equals(submission.getState()) && !"RETURNED".equals(submission.getState())) {
                                    a=new CourseData(course.getName(),courseWork.getTitle(),courseWork.getDescription(),
                                            courseWork.getDueTime().toString(),courseWork.getDueDate().toString());
                                    courseData.add(a);
                                }
                            }
                        } else {
                            System.out.println("No student submissions found for coursework ID: " + courseWorkId);
                        }
                    } else {
                        System.out.println("No response for coursework ID: " + courseWorkId);
                    }
                }
            } else {
                System.out.println("No coursework found for the given course.");
            }
        }
        return courseData;
    }

    private static List<CourseWork> retrieveCoursework(Classroom classroom, String courseId) throws IOException {
        ListCourseWorkResponse response = classroom.courses().courseWork().list(courseId).execute();
        if (response != null) {
            return response.getCourseWork();
        } else {
            System.out.println("No response for retrieving coursework.");
            return null;
        }
    }

}
