package com.kis.serializer;


import com.kis.domain.TreeNode;
import com.kis.printer.PrinterManager;
import com.kis.utils.PathConstant;
import com.kis.utils.PropertyReader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class SqlSerializer implements Serializer {

    private static Logger logger = Logger.getLogger(SqlSerializer.class.getName());

    private PropertyReader propertyReader;

    static final Serializer INSTANCE = new SqlSerializer();

    private SqlSerializer() {
        propertyReader = new PropertyReader(PathConstant.languagePath);
    }

    /**
     * @param treeNode - treeNode, which we have to serialize
     * @param file     - output file. If null - tree will be written on console
     * @return text of the SQL-file
     */
    @Override
    public String serialize(TreeNode treeNode, File file) {
        String script = PrinterManager.print(treeNode);
        output(script, file);
        return script;
    }

    /**
     * @param script - text of the SQL-script
     * @param file   - output file.
     */
    private void output(String script, File file) {
        if (file != null) {
            try {
                if (!file.getName().endsWith(".sql")) {
                    throw new IllegalArgumentException(propertyReader.getBundle().getString("IncorrectFileName"));
                }
                FileWriter writer = new FileWriter(file);
                writer.write(script);
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println(script);
        }
    }
}