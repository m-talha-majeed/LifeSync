package com.example.demo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface ActivitiesInterface extends CrudRepository<ActivityData, Integer> {
    List<ActivityData> findAllByUserID(int userID);
    Optional<ActivityData> findByUserIDAndActivityID(Integer userId,Integer activityId);

    // Example method to update the username by userID
    default void updateActivtyByUserId(Integer userId, Integer activityID,String name,String description,String date,String duration) {
        Optional<ActivityData> optionalLogInData = findByUserIDAndActivityID(userId,activityID);
        // Step 2: Update the fields if the LogInData exists
        optionalLogInData.ifPresent(logInData -> {
            logInData.setName(name);
            logInData.setDescription(description);
            logInData.setDate(date);
            logInData.setDuration(duration);
            // Step 3: Save the updated LogInData
            save(logInData);
        });
    }
    void deleteActivityDataByUserIDAndActivityID(Integer userId,Integer activityId);
}
