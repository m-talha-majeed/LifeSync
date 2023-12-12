package com.example.demo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.HashMap;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.services.classroom.Classroom;
import java.security.GeneralSecurityException;
import java.util.Map;

import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(path = "/LifeSync")
public class User {
    @Autowired
    private final LoginInfo l;
    @Autowired
    private final ActivitiesInterface acts;
    @Autowired
    private final NotesInterface noteInterface;

    public User(LoginInfo l, ActivitiesInterface acts, NotesInterface noteInterface) {
        this.l = l;
        this.acts = acts;
        this.noteInterface = noteInterface;
    }

    public static LogInData a = new LogInData();
    public static boolean NewsShow;
    List<ActivityData> actives;
    List<CourseData> corWork;
    List<ExerciseRecord> Exercise;
    GoogleClassroom room;
    List<NewsArticle> Articles;
    boolean ShowClass;
    List<NoteData> notes;
    public static boolean Fitness;
    public static Music spotify = null;

    @RequestMapping("/login")
    public String showLoginForm() {
        return "LogIn";
    }
    @PostMapping("/login")
    public String processLoginForm(
            @RequestParam("username") String username,
            @RequestParam("password") String password, Model model) throws Exception {
        System.out.println("Received username: " + username);
        System.out.println("Received password: " + password);
        List<LogInData> log = l.findAllByUsernameAndPassword(username, password);
        if (log.isEmpty()) {
            return "/LogIn";
        } else {
            for (LogInData logData : log) {

                a.setAllData(logData.getUserID(), logData.getUsername(), logData.getPassword(), logData.getName(), logData.getEmail(),
                        logData.getExpenses(), logData.getBudget(), logData.getIncome(), logData.getToken(), logData.getSavings(),
                        logData.getInvestments(), logData.getIp(), logData.getCity(), logData.getRegion(), logData.getCountry(),
                        logData.getLatitude(), logData.getLongitude(), logData.getPostal(), logData.getTimezone(),
                        logData.getWeatherCity(), logData.getWeatherRegion(), logData.getWeatherCountry(),
                        logData.getLastUpdated(), logData.getTemperatureC(), logData.getTemperatureF(),
                        logData.getCondition(), logData.getClassroomCredentials(), logData.getGroceryAmount(), logData.getGroceryDesc(),
                        logData.getGroceryTitle(), logData.getGroceryType());
                actives = acts.findAllByUserID(a.getUserID());
                notes = noteInterface.findAllByUserID(a.getUserID());
                room = new GoogleClassroom();
                if (!a.getClassroomCredentials().isEmpty()) {
                    corWork = room.getStudentSubmissions(room.deserializeClassroom(a.getClassroomCredentials()));
                    setClassTrue();
                } else {
                    setClassFalse();
                }
                if (logData.getToken().isEmpty()) {
                    spotify = new Music();
                } else {
                    spotify = new Music(logData.getToken());
                }
                setFitnessFalse();
                setNewsShowFalse();
                getModel(model);
            }
            return "/Homepage";
        }
    }
    @RequestMapping("/LogIn")
    public String tryRun() {
        a = new LogInData();
        spotify = null;
        return "LogIn";
    }
    //Activity Controllers
    @RequestMapping("/ViewActivities")
    public String activityPage(Model model) {
        getModel(model);
        return "/Activities";
    }
    @PostMapping("/editActivity")
    public String editActivity(
            @RequestParam("editActivityId") int editActivityId,
            @RequestParam("editTitle") String editTitle,
            @RequestParam("editdate") String editDate,
            @RequestParam("editduration") String editDuration,
            @RequestParam("editDescription") String editDescription,Model model) {
        acts.updateActivtyByUserId(a.getUserID(),editActivityId,editTitle,editDescription,editDate,editDuration);
        actives = acts.findAllByUserID(a.getUserID());
        getModel(model);

        return "Homepage";
    }
    @PostMapping(path = "/deleteActivity")
    @Transactional
    public String deleteActivities(@RequestParam("id")int id, Model model){
        acts.deleteActivityDataByUserIDAndActivityID(a.getUserID(),id);
        actives = acts.findAllByUserID(a.getUserID());
        getModel(model);
        return "Homepage";
    }
    @PostMapping("/addActivity")
    public String processAddActivityForm(
            @RequestParam("actTitle") String actTitle,
            @RequestParam("actdate") String actDate,
            @RequestParam("actduration") String actDuration,
            @RequestParam("actDescription") String actDescription,
            Model model) {
        ActivityData a1 = new ActivityData();
        a1.setName(actTitle);
        a1.setDescription(actDescription);
        a1.setDate(actDate);
        a1.setDuration(actDuration);
        a1.setUserId(a.getUserID());
        actives.add(a1);
        acts.save(a1);
        getModel(model);
        return "/Activities";
    }
    @RequestMapping("/mainPage")
    public String getMainPage(Model model){
        getModel(model);
        return "mainpage";
    }
    //Notes Controllers
    @PostMapping("/editNotes")
    public String editNote(
            @RequestParam("editActivityId") int noteId,
            @RequestParam("notetit") String noteTitle,
            @RequestParam("noteDescript") String Description,Model model) {
        noteInterface.updateNoteDataByUserId(a.getUserID(),noteId,noteTitle,Description);
        notes=noteInterface.findAllByUserID(a.getUserID());
        getModel(model);
        return "Homepage";
    }
    @PostMapping(path = "/deleteNote")
    @Transactional
    public String deleteNotes(@RequestParam("id")int id, Model model){
        noteInterface.deleteNoteDataByUserIDAndNoteID(a.getUserID(),id);
        notes=noteInterface.findAllByUserID(a.getUserID());
        getModel(model);
        return "Homepage";
    }
    @RequestMapping("/Notes")
    public String activityNotes(Model model) {
        getModel(model);
        return "/Notes";
    }
    @PostMapping("/NoteAdd")
    public String addNote(
            @RequestParam("noteTitle") String noteTitle,
            @RequestParam("noteDescription") String noteDescription,
            Model model
    ) {
        NoteData n1 = new NoteData(noteTitle, noteDescription, a.getUserID());
        notes.add(n1);
        noteInterface.save(n1);
        getModel(model);
        return "Homepage";
    }
    //Spotify Controllers
    @GetMapping("/Homepage")
    public String handleSpotifyRedirect(@RequestParam(name = "code", required = false, defaultValue = "") String code, Model model) {
        if (code != null && !code.isEmpty()) {
            System.out.println("Spotify Code: " + code);
            spotify.GetAccessToken(code);
            String Token = spotify.getToken();
            l.updateTokenByUserId(a.getUserID(), Token);
            a.setToken(Token);
            getModel(model);
            return "Homepage";
        } else {
            getModel(model);
            return "Homepage";
        }

    }
    @RequestMapping("/Previous")
    public String PreviousSong(Model model) {
        spotify.skipToPreviousTrack(a.getToken());
        getModel(model);
        return "/Homepage";
    }

    @RequestMapping("/Next")
    public String NextSong(Model model) {
        spotify.skipToNextTrack(a.getToken());
        getModel(model);
        return "/Homepage";
    }
    @RequestMapping("/PausePlay")
    public String PausePlay(Model model) {
        spotify.pauseOrResumePlayback(a.getToken());
        getModel(model);
        return "Homepage";
    }

    @RequestMapping("/MusicPlayer")
    public RedirectView MusicAuthentication() throws URISyntaxException, IOException {
        String Url = spotify.authenticator();
        return new RedirectView(Url);
    }
    //Google Classroom Controllers
    @GetMapping("/getGoogleAuthentication")
    public String handleGoogleRedirect(@RequestParam(name = "code", required = false, defaultValue = "") String code, Model model) throws Exception {
        GoogleTokenResponse tokenResponse;
        try {
            tokenResponse = room.exchangeCodeForTokens(code);
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        Credential credential = room.buildCredentialFromTokens(tokenResponse);
        Classroom classroom = GoogleClassroom.buildClassroomService(credential);
        corWork = room.getStudentSubmissions(classroom);
        //l.updateClassroomByUserId(a.getUserID(),room.serializeClassroom(classroom));
        setClassTrue();
        getModel(model);
        return "Homepage";
    }
    @RequestMapping("/StudentSubmission")
    public String run1() {
        return "/Homepage";
    }
    @RequestMapping("/ClassroomCredential")
    public RedirectView getClassroom(Model model) throws Exception {
        room = new GoogleClassroom();
        getModel(model);
        return new RedirectView(room.getAuthorizationUrl());
    }
    //Fitness Controller
    @PostMapping("/FitnessGetter")
    public String submitFitness(String muscle, Model model) throws Exception {
        Fitness a = new Fitness();
        a.getExerciseData(muscle, null);
        Exercise = a.getExcercises();
        setFitnessTrue();
        getModel(model);
        return "Homepage";
    }
    //News Controller
    @PostMapping("/NewsGetter")
    public String submitNews(String country, String newsType, Model model) {
        NewsApiService newsApiService = new NewsApiService();
        newsApiService.searchTopHeadlines(country, newsType);
        Articles = newsApiService.getTopHeadlines();
        setNewsShowTrue();

        getModel(model);
        return "Homepage";
    }

    //Finances Controller
    @PostMapping("/Finances")
    public String EditFinances(@RequestParam("editexp") int expenses,
                               @RequestParam("editinc") int income,
                               @RequestParam("editbdg") int budget,
                               @RequestParam("editsav") int savings,
                               @RequestParam("editinvest") int investment, Model model) {
        int UserID = a.getUserID();
        l.updateFinancesByUserId(UserID, savings, expenses, investment, budget, income);
        a.setExpenses(expenses);
        a.setSavings(savings);
        a.setBudget(budget);
        a.setIncome(income);
        a.setInvestments(investment);
        getModel(model);
        return "/Homepage";
    }
    //Location Controller
    @RequestMapping("/RefreshLocation")
    public String RefreshLocation(Model model) {
        Location location = new Location();
        location.FetchLocation();
        int UserID = a.getUserID();
        l.updateLocationByUserId(UserID, location.getIp(), location.getCity(), location.getRegion(), location.getCountry(), location.getLatitude(), location.getLongitude(), location.getPostal(), location.getTimezone());

        a.setIp(location.getIp());
        a.setCity(location.getCity());
        a.setRegion(location.getRegion());
        a.setCountry(location.getCountry());
        a.setLongitude(location.getLatitude());
        a.setLatitude(location.getLongitude());
        a.setPostal(location.getPostal());
        a.setTimezone(location.getTimezone());
        getModel(model);
        return "/Homepage";
    }
    //Grocery Controller
    @RequestMapping("/grocery")
    public String showGrocery(Model model) {
        getModel(model);
        return "/grocery";
    }

    @RequestMapping("/GroceryEditior")
    public String groceryEditior(@RequestParam("Amount") int amount,
                                 @RequestParam("Type") String Type,
                                 @RequestParam("description") String description,
                                 @RequestParam("title") String Title, Model model) {
        int userID = a.getUserID();
        l.updateGroceryByUserId(userID, description, Type, Title, amount);
        a.setGroceryType(Type);
        a.setGroceryAmount(amount);
        a.setGroceryTitle(Title);
        a.setGroceryDesc(description);
        getModel(model);
        return "/grocery";

    }

    //Weather Controller
    @RequestMapping("/UpdateWeather")
    public String RefreshWeather(Model model) {
        int userID = a.getUserID();
        Location location = new Location();
        location.FetchLocation();
        Weather weather = new Weather();
        weather.fetchWeatherData(location.getCity());
        l.updateWeatherByUserId(userID, weather.getCity(), weather.getRegion(), weather.getCountry(), weather.getLastUpdated(), weather.getTemperatureC(), weather.getCondition(), weather.getTemperatureF());
        a.setWeatherCity(weather.getCity());
        a.setWeatherRegion(weather.getRegion());
        a.setWeatherCountry(weather.getCountry());
        a.setLastUpdated(weather.getLastUpdated());
        a.setTemperatureC(weather.getTemperatureC());
        a.setTemperatureF(weather.getTemperatureF());
        a.setCondition(weather.getCondition());
        getModel(model);
        return "Homepage";
    }
    //Other Functions/Database Functions
    @RequestMapping("/Homepage")
    public String runHompepage(Model model) {
        getModel(model);
        return "/Homepage";
    }
    @RequestMapping("/Profile")
    public String getProfile(Model model) {
        getModel(model);
        return "profile";
    }
    private void setNewsShowFalse() {
        NewsShow = false;
    }

    private void setNewsShowTrue() {
        NewsShow = true;
    }

    public void setFitnessTrue() {
        Fitness = true;
    }

    public void setFitnessFalse() {
        Fitness = false;
    }

    public void setClassTrue() {
        ShowClass = true;
    }

    public void setClassFalse() {
        ShowClass = false;
    }

    private void getModel(Model model) {
        if (Fitness) {
            model.addAttribute("Exercise", Exercise);
        }
        if (NewsShow) {
            Map<String, String> ArticlesInfo = new HashMap<>();
            int i = 0;
            for (NewsArticle article : Articles) {
                i++;
                if (i == 5) {
                    break;
                }
                ArticlesInfo.put(article.getTitle(), article.getUrl());
            }
            if (i == 0) {
                ArticlesInfo.put("No News To Show", "");
            }
            model.addAttribute("articlesInfo", ArticlesInfo);
        }
        if (ShowClass) {
            model.addAttribute("CourseData", corWork);
        }
        model.addAttribute("ShowClass", ShowClass);
        model.addAttribute("showClassroom", a.getClassroomCredentials());
        model.addAttribute("activity", actives);
        model.addAttribute("notes", notes);
        model.addAttribute("Fitness", Fitness);
        model.addAttribute("NewsShow", NewsShow);
        model.addAttribute("token", a.getToken());
        model.addAttribute("username", a.getUsername());
        model.addAttribute("password", a.getPassword());
        model.addAttribute("email", a.getEmail());
        model.addAttribute("name", a.getName());
        model.addAttribute("userID", a.getUserID());
        model.addAttribute("city", a.getCity());
        model.addAttribute("wCity", a.getWeatherCity());
        model.addAttribute("wRegion", a.getWeatherRegion());
        model.addAttribute("wCountry", a.getWeatherCountry());
        model.addAttribute("lastUpdated", a.getLastUpdated());
        model.addAttribute("temperatureC", a.getTemperatureC());
        model.addAttribute("temperatureF", a.getTemperatureF());
        model.addAttribute("weatherCondition", a.getCondition());
        model.addAttribute("ip", a.getIp());
        model.addAttribute("region", a.getRegion());
        model.addAttribute("country", a.getCountry());
        model.addAttribute("latitude", a.getLatitude());
        model.addAttribute("longitude", a.getLongitude());
        model.addAttribute("postal", a.getPostal());
        model.addAttribute("income", a.getIncome());
        model.addAttribute("savings", a.getSavings());
        model.addAttribute("investments", a.getInvestments());
        model.addAttribute("expenses", a.getExpenses());
        model.addAttribute("budget", a.getBudget());
        model.addAttribute("GroceryTitle", a.getGroceryTitle());
        model.addAttribute("GroceryDesc", a.getGroceryDesc());
        model.addAttribute("GroceryAmount", a.getGroceryAmount());
        model.addAttribute("GroceryType", a.getGroceryType());

    }
}
