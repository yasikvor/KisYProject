package com.kis.manager;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;

public class TestSerializeManager {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void caseSendNormalArgsForXMLInBreadth() throws Exception {
        String[] args = new String[20];
        args[0] = "-in";
        args[1] = this.getClass().getClassLoader().getResource("com/kis/manager/input.xml").getFile();
        args[2] = "-out";
        args[3] = this.getClass().getClassLoader().getResource("com/kis/manager/output.xml").getFile();
        args[4] = "-attr";
        args[5] = "value";
        args[6] = "-mode";
        args[7] = "breadth";
        String string1 = SerializeManager.getInstance().setCommands(args);
        String string2 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                        "<root>\r\n" +
                        "  <projectPath value=\"run/TargetPro/MSSQL16-PostgreSQL/MSSQL16-PostgreSQL.sct\" />\r\n" +
                        "  <logFilePath value=\"run\\reports\\MSSQL16-PostgreSQL\\MSSQL16-PostgreSQL_convert.csv\" />\r\n" +
                        "  <testListFile value=\"run\\test-lists\\test-list-postgre_mssql-all.csv\" />\r\n" +
                        "  <reference-file value=\"run/references/MSSQL_PostgreEtalonConvert.xml\" />\r\n" +
                        "  <sqlIgnoreCase value=\"true\" />\r\n" +
                        "  <allowComments value=\"false\" />\r\n" +
                        "  <addNamedReport value=\"false\" />\r\n" +
                        "  <referenceSchema value=\"Schemas.gold_test_ss_pg_dbo\" />\r\n" +
                        "  <sourceSchema value=\"Databases.GOLD_TEST_SS_PG.Schemas.dbo\" />\r\n" +
                        "  <targetSchema value=\"Schemas.gold_test_ss_pg_dbo\" />\r\n" +
                        "  <fileName value=\"AWSSchemaConversionToolBatch.jar\" />\r\n" +
                        "  <fileFolder value=\"run/console/\" />\r\n" +
                        "  <fileName value=\"batch501.xml\" />\r\n" +
                        "  <fileFolder value=\"run\\console\\\" />\r\n" +
                        "  <vendor value=\"MSSQL\" />\r\n" +
                        "  <connectionString value=\"ec2-52-37-30-252.us-west-2.compute.amazonaws.com\" />\r\n" +
                        "  <login value=\"min_privs\" />\r\n" +
                        "  <password value=\"min_privs\" />\r\n" +
                        "  <port value=\"1455\" />\r\n" +
                        "  <sourceVendor value=\"MSSQL\" />\r\n" +
                        "  <vendor value=\"test_ora_pg\" />\r\n" +
                        "  <connectionString value=\"postgresql-9-6-1.cjj06khxetlc.us-west-2.rds.amazonaws.com\" />\r\n" +
                        "  <login value=\"min_privs\" />\r\n" +
                        "  <password value=\"min_privs\" />\r\n" +
                        "  <port value=\"5432\" />\r\n" +
                        "  <targetVendor value=\"POSTGRESQL\" />\r\n" +
                        "</root>\r\n\r\n";
        Assert.assertEquals(string1, string2);

    }

    @Test
    public void caseSendNormalArgsForXMLInDepth() throws Exception {
        String[] args = new String[20];
        args[0] = "-in";
        args[1] = this.getClass().getClassLoader().getResource("com/kis/manager/input.xml").getFile();
        args[2] = "-out";
        args[3] = this.getClass().getClassLoader().getResource("com/kis/manager/output.xml").getFile();
        args[4] = "-attr";
        args[5] = "value";
        args[6] = "-mode";
        args[7] = "depth";
        String string1 = SerializeManager.getInstance().setCommands(args);
        String string2 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                        "<root>\r\n" +
                        "  <fileName value=\"AWSSchemaConversionToolBatch.jar\" />\r\n" +
                        "  <fileFolder value=\"run/console/\" />\r\n" +
                        "  <fileName value=\"batch501.xml\" />\r\n" +
                        "  <fileFolder value=\"run\\console\\\" />\r\n" +
                        "  <projectPath value=\"run/TargetPro/MSSQL16-PostgreSQL/MSSQL16-PostgreSQL.sct\" />\r\n" +
                        "  <logFilePath value=\"run\\reports\\MSSQL16-PostgreSQL\\MSSQL16-PostgreSQL_convert.csv\" />\r\n" +
                        "  <testListFile value=\"run\\test-lists\\test-list-postgre_mssql-all.csv\" />\r\n" +
                        "  <reference-file value=\"run/references/MSSQL_PostgreEtalonConvert.xml\" />\r\n" +
                        "  <sqlIgnoreCase value=\"true\" />\r\n" +
                        "  <allowComments value=\"false\" />\r\n" +
                        "  <addNamedReport value=\"false\" />\r\n" +
                        "  <referenceSchema value=\"Schemas.gold_test_ss_pg_dbo\" />\r\n" +
                        "  <sourceSchema value=\"Databases.GOLD_TEST_SS_PG.Schemas.dbo\" />\r\n" +
                        "  <targetSchema value=\"Schemas.gold_test_ss_pg_dbo\" />\r\n" +
                        "  <vendor value=\"MSSQL\" />\r\n" +
                        "  <connectionString value=\"ec2-52-37-30-252.us-west-2.compute.amazonaws.com\" />\r\n" +
                        "  <login value=\"min_privs\" />\r\n" +
                        "  <password value=\"min_privs\" />\r\n" +
                        "  <port value=\"1455\" />\r\n" +
                        "  <sourceVendor value=\"MSSQL\" />\r\n" +
                        "  <vendor value=\"test_ora_pg\" />\r\n" +
                        "  <connectionString value=\"postgresql-9-6-1.cjj06khxetlc.us-west-2.rds.amazonaws.com\" />\r\n" +
                        "  <login value=\"min_privs\" />\r\n" +
                        "  <password value=\"min_privs\" />\r\n" +
                        "  <port value=\"5432\" />\r\n" +
                        "  <targetVendor value=\"POSTGRESQL\" />\r\n" +
                        "</root>\r\n\r\n";
        Assert.assertEquals(string1, string2);

    }

    @Test
    public void caseSendNullArgs() throws Exception {

        thrown.expect(NullPointerException.class);
        SerializeManager.getInstance().setCommands(null);

    }

    @Test
    public void caseSendWrongArgs()  throws Exception{

        thrown.expect(Exception.class);
        String[] args = {"wrong", "commands"};
        SerializeManager.getInstance().setCommands(args);
    }

}
