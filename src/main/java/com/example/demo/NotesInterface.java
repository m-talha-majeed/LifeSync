package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NotesInterface  extends CrudRepository<NoteData, Integer> {
    List<NoteData> findAllByUserID(int userID);
    Optional<NoteData> findByUserIDAndNoteID(Integer userId,Integer noteId);
    default void updateNoteDataByUserId(Integer userId, Integer noteID,String name,String description) {
        Optional<NoteData> optionalLogInData = findByUserIDAndNoteID(userId,noteID);
        // Step 2: Update the fields if the LogInData exists
        optionalLogInData.ifPresent(logInData -> {
            logInData.setTitle(name);
            logInData.setDescription(description);
            // Step 3: Save the updated LogInData
            save(logInData);
        });
    }
    void deleteNoteDataByUserIDAndNoteID(Integer userId,Integer noteID);
}