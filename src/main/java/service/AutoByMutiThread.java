package service;

import application.AutoCreateGPUVm;
import application.AutoCreateMysqlAndTestConnection;
import application.AutoCreateVm;
import application.AutoDestroyDB;
import application.AutoDestroyVm;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author WXY
 * @version 创建时间：2018/11/5
 */
public class AutoByMutiThread {
    private final static Logger logger = Logger.getLogger(AutoByMutiThread.class);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        logger.info("创建无界自动回收的Cache线程池");
        RunnableThreadForVM t1 = new RunnableThreadForVM();
        logger.info("初始化创建VM的线程");
        RunnableThreadForDB t2 = new RunnableThreadForDB();

        RunnableThreadDelVM t3 = new RunnableThreadDelVM();

        RunnableThreadDelDB t4 = new RunnableThreadDelDB();

        RunnableThreadForGPU t5 = new RunnableThreadForGPU();


//        logger.info("初始化创建DB的线程");
//        executorService.execute(t1);

        logger.info("创建VM的线程加入线程池");
        executorService.execute(t2);

//        logger.info("删除VM的线程加入线程池");
//        executorService.execute(t3);
//
//        logger.info("删除DB的线程加入线程池");
//        executorService.execute(t4);

        logger.info("创建GPU的线程加入线程池");
        executorService.execute(t5);

//        if (!executorService.isShutdown()) {
//            logger.info("关闭线程池");
//            executorService.shutdown();
//        }
    }
}

class RunnableThreadForVM implements Runnable {
    private final static Logger logger = Logger.getLogger(RunnableThreadForVM.class);


    public void run() {
        logger.info("线程: " + Thread.currentThread().getName() + "执行创建vm任务");
        AutoCreateVm.autoCreateVM();
    }

}

class RunnableThreadForDB implements Runnable {
    private final static Logger logger = Logger.getLogger(RunnableThreadForDB.class);


    public void run() {
        logger.info("线程: " + Thread.currentThread().getName() + "执行创建DB任务");
        AutoCreateMysqlAndTestConnection.autoCreateMysqlAndTestConnection();
    }
}

class RunnableThreadForGPU implements Runnable {
    private final static Logger logger = Logger.getLogger(RunnableThreadForGPU.class);


    public void run() {
        logger.info("线程: " + Thread.currentThread().getName() + "执行创建GPU任务");
        AutoCreateGPUVm.autoCreateGpuVM();
    }
}

class RunnableThreadDelVM implements Runnable {
    private final static Logger logger = Logger.getLogger(RunnableThreadDelVM.class);


    public void run() {
        logger.info("线程: " + Thread.currentThread().getName() + "执行删除VM任务");
        AutoDestroyVm.autoDestroyVM();
    }
}

class RunnableThreadDelDB implements Runnable {
    private final static Logger logger = Logger.getLogger(RunnableThreadDelDB.class);


    public void run() {
        logger.info("线程: " + Thread.currentThread().getName() + "执行删除DB任务");
        AutoDestroyDB.autoDestroyDB();
    }
}
