package com.kis.settings;

import com.kis.constant.SettingsConstant;

import java.io.*;
import java.util.Properties;

public class Settings {

    private static String projectFolderPath = getDocumentFolder();

    private static String propertyPath;

    private static Properties properties = new Properties();

    static {
        try {

            propertyPath = projectFolderPath + "\\settings.properties";
            File rootFolder = new File(projectFolderPath);
            if(!rootFolder.exists()) {
                rootFolder.mkdirs();
            }
            File file = new File(propertyPath);
            if(!file.exists()) {
                file.createNewFile();
            }
            FileInputStream stream = new FileInputStream(propertyPath);
            properties.load(stream);
            stream.close();
            if(properties.entrySet().size() == 0) {
                commonSettings();
            }
            File schemaFolder = new File(properties.getProperty(SettingsConstant.SCHEMA_PATH));
            if(!schemaFolder.exists())
                schemaFolder.mkdirs();

            File projectFolder = new File(properties.getProperty(SettingsConstant.PROJECT_PATH));
            if(!projectFolder.exists())
                projectFolder.mkdirs();
            System.setProperty("logfile.name", properties.getProperty(SettingsConstant.LOGGER_PATH));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void commonSettings() {
        properties.setProperty(SettingsConstant.PROJECT_PATH, projectFolderPath + "\\Project Folder");
        properties.setProperty(SettingsConstant.SCHEMA_PATH, projectFolderPath + "\\Script Folder");
        properties.setProperty(SettingsConstant.CONNECTION_PATH, projectFolderPath + "\\connections.xml");
        properties.setProperty(SettingsConstant.LOGGER_PATH, projectFolderPath + "\\log.log");
        store();
    }

    private static String getDocumentFolder() {
        String userFolder = null;

        try {
            Process p =  Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
            p.waitFor();

            InputStream in = p.getInputStream();
            byte[] b = new byte[in.available()];
            in.read(b);
            in.close();

            userFolder = new String(b).split("\\s\\s+")[4] + "\\KisYProject";

        } catch(Throwable t) {
            t.printStackTrace();
        }

        return userFolder;
    }

    public static void store() {

        try {
            FileOutputStream outputStream = new FileOutputStream(propertyPath);
            properties.store(outputStream, null);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
