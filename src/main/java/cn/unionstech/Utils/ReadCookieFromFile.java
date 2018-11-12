package cn.unionstech.Utils;


import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @author WXY
 * @version 创建时间：2018/11/1
 */
public class ReadCookieFromFile {
    private final static Logger logger = Logger.getLogger(ReadCookieFromFile.class);

    public static String[] getTimeFromTxt(String filePath) {
        String[] resultMap = new String[2];
        try {
            File file = new File(filePath);
            BufferedReader reader = null;
            if (file.isFile() && file.exists()) {
                reader = new BufferedReader(new FileReader(file));
                String tempString = "";
                while ((tempString = reader.readLine()) != null) {
                    String[] tempString2 = tempString.split(",");
                    resultMap[0] = tempString2[0];
                    resultMap[1] = tempString2[1];
                }
                if (reader != null)
                    reader.close();
            } else {
                logger.error("找不到指定的文件");
            }
        } catch (Exception e) {
            logger.error("读取文件内容出错。 " + e.getMessage());
        }
        return resultMap;
    }

}
