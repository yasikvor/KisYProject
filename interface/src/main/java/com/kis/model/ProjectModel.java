package com.kis.model;

import java.io.File;

public class ProjectModel {
    private static File projectFile;

    public static File getProjectFile() {
        return projectFile;
    }

    public static void setProjectFile(File projectFile) {
        ProjectModel.projectFile = projectFile;
    }
}
