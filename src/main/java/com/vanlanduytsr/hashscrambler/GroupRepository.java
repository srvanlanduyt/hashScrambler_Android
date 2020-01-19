package com.vanlanduytsr.hashscrambler;

import android.app.Application;

import java.util.List;

class GroupRepository {

    private static List<Group> mAllGroups;
    private GroupDao groupDao;

    GroupRepository(Application application) {
        GroupDatabase db = GroupDatabase.getDatabase(application);
        groupDao = db.groupDao();
        mAllGroups = groupDao.getAllGroups();
    }

    static List<Group> getAllGroups() {
        return mAllGroups;
    }

    void insert(Group group) {
        GroupDatabase.databaseWriterExecutor.execute(() -> {
            groupDao.insert(group);
        });
    }
}