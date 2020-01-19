package com.vanlanduytsr.hashscrambler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GroupDao {

    @Query("SELECT * FROM group_table")
    List<Group> getAllGroups();

    @Insert
    void insertAll(Group... groups);

    @Insert
    void insert(Group group);

    @Update
    void update(Group group);

    @Delete
    void delete(Group group);




}
