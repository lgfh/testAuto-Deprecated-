package cn.unionstech.service;

import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author WXY
 * @version 创建时间：2018/11/5
 */
public class AutoBySingleThread {
    private final static Logger logger = Logger.getLogger(AutoByMutiThread.class);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        logger.info("创建单线程池");
        int i = 1;
        while (i <= 2) {
            RunnableThreadDelDB t = new RunnableThreadDelDB();
            executorService.execute(t);
            i++;
        }

        executorService.shutdown();

    }
}



