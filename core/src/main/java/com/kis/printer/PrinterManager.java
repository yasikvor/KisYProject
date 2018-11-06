package com.kis.printer;

import com.kis.domain.TreeNode;
import com.kis.printer.annotation.PrinterInfo;
import com.kis.utils.ReflectionUtils;
import org.apache.log4j.Logger;

import java.util.*;

public class PrinterManager {

    private static Logger logger = Logger.getLogger(PrinterManager.class.getName());

    private static Map<String, Printer> printers = new HashMap<>();

    static {
        registerPrinters();
    }

    public static String print(TreeNode element) {
        return printers.get(element.getType()) == null
                ? ""
                : printers.get(element.getType()).print(element);
    }

    private static void registerPrinters() {
        printers = ReflectionUtils.getAnnotatedClasses(PrinterInfo.class);
    }

}
