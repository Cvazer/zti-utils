package by.zti.main.utils;

import by.zti.main.serializer.SimpleXmlConfig;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is a simplest in a world implementation of basic logging functionality. Create an instance of this class
 * to use it. Set it's parameters and invoke {@link #log(LogLevel, String)} method to write something in a {@link #log}
 * file.
 */
public class SimpleLogger implements ZTILogger{
    /** Instance of {@link java.text.SimpleDateFormat} class to create dated logs **/
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    /** Path to your log folder **/
    private String logDirPath = "logs/";
    /** Name of your log file **/
    private String logFileName = "log.txt";
    /** Instance of file to represent your logfile **/
    private File log;
    /** Boolean flag for console printing **/
    private boolean printing;

    /**
     * Use this constructor to create default log file in directory logs/ called log.txt
     */
    public SimpleLogger() {
        createLog();
    }

    /**
     * Use this constructor to create default log file in directory logs/ called log.txt
     *
     * @param printing console synchronous log printing flag.
     */
    public SimpleLogger(boolean printing) {
        this();
        this.printing = printing;
    }

    /**
     * Use this constructor to create logger bound to specific file.
     * @param log File to witch logging would be performed.
     */
    public SimpleLogger(File log) {
        this.log = log;
        check(log);
    }

    /**
     * Use this constructor to create logger bound to specific file.
     * @param log File to witch logging would be performed.
     * @param printing console synchronous log printing flag.
     */
    public SimpleLogger(File log, boolean printing) {
        this(log);
        this.printing = printing;
    }

    /**
     * Use this constructor to create logger bound to specific file at given file name.
     * @param logFileName Name of a file to witch logging would be performed.
     */
    public SimpleLogger(String logFileName) {
        log = new File(logFileName);
        check(log);
    }

    /**
     * Use this constructor to create logger bound to specific file at given file name and set printing value.
     * @param logFileName Name of a file to witch logging would be performed.
     * @param printing console synchronous log printing flag.
     */
    public SimpleLogger(String logFileName, boolean printing) {
        this(logFileName);
        this.printing = printing;
    }

    /**
     * Inner method to preform checking of file existence.
     * @param log File to check.
     */
    private void check(File log){
        if(!log.exists()){
            try {
                log.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inner method for defining {@link #log} parameter.
     */
    private void createLog() {
        log = new File(logDirPath + logFileName);
    }

    /**
     * Invoke this method on logger instance to perform logging to a log file.
     *
     * @param level Level prefix for the message.
     * @param message Text of actual message.
     */
    @Override
    public void log(LogLevel level, String message) {
        String text = "["+SDF.format(new Date())+"]"+" "+"["+level+"]"+" "+message;
        this.log(text);
    }

    /**
     * Invoke this method on logger instance to perform logging to a log file without decorations.
     *
     * @param message Text of actual message.
     */
    @Override
    public void log(String message) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter out = null;
        try {
            fw = new FileWriter(log, true);
            bw = new BufferedWriter(fw);
            out = new PrintWriter(bw);
            out.println(message);
            if(printing){System.out.println(message);}
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(out != null)
                out.close();
            try {
                if(bw != null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(fw != null)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * invoke this method on logger object to set it's parameters from {@link SimpleXmlConfig} object. Note: You have
     * to create config file with logdir and logname tags.
     * @param cfg Instance of config file.
     */
    public void set(SimpleXmlConfig cfg){
        logDirPath = cfg.get("logdir");
        logFileName = cfg.get("logname");
        createLog();
    }

    public String getLogDirPath() {
        return logDirPath;
    }

    public void setLogDirPath(String logDirPath) {
        this.logDirPath = logDirPath;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public File getLog() {
        return log;
    }

    public void setLog(File log) {
        this.log = log;
    }

    public boolean isPrinting() {
        return printing;
    }

    public void setPrinting(boolean printing) {
        this.printing = printing;
    }
}
