package com.kis.listener;

import com.kis.manager.SerializeManager;

import java.sql.Connection;

public class Main {


    public static void main(String[] args) throws Exception {

//        args = new String[20];
//        args[0] = "-in";
//        args[1] = URL;
//        args[2] = "-user";
//        args[3] = USERNAME;
//        args[4] = "-pass";
//        args[5] = PASSWORD;
//        args[6] = "-nodeType";
//        args[7] = "sql";

        SerializeManager.getInstance().setCommands(args);

    }


}


