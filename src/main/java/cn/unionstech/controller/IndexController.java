package cn.unionstech.controller;

import cn.unionstech.application.AutoCreateGPUVm;
import cn.unionstech.application.AutoCreateMysqlAndTestConnection;
import cn.unionstech.application.AutoCreateRedisAndTest;
import cn.unionstech.application.AutoCreateVm;
import cn.unionstech.application.AutoDestroyDB;
import cn.unionstech.application.AutoDestroyVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WXY
 * @version 创建时间：2018/11/12
 */
@RestController
public class IndexController {

    @Autowired
    AutoCreateVm autoCreateVm;

    @Autowired
    AutoCreateGPUVm autoCreateGPUVm;

    @Autowired
    AutoCreateMysqlAndTestConnection autoCreateMysqlAndTestConnection;

    @Autowired
    AutoDestroyDB autoDestroyDB;

    @Autowired
    AutoDestroyVm autoDestroyVm;

    @Autowired
    AutoCreateRedisAndTest autoCreateRedisAndTest;

    @RequestMapping(path = "/CreateVm")
//    @ResponseBody
    public String getVmCreateResult(@RequestParam(value = "zone", defaultValue = "华东一区") String zone) {

        return autoCreateVm.autoCreateVM(zone);
//        return "Hello";
    }

    @RequestMapping(path = "/CreateTestVm")
    public String getVmCreateResult() {

        return autoCreateVm.autoCreateVM("华中二区");
    }

    @RequestMapping(path = "/CreateGpuVm")
    @ResponseBody
    public String getGpuVmCreateResult() {

        return autoCreateGPUVm.autoCreateGpuVM();
    }


    @RequestMapping("/DelVm")
    public String getVmDelResult() {
        return autoDestroyVm.autoDestroyVM();
    }

    @RequestMapping("/DelDB")
    public String getDbDelResult(@RequestParam(value = "zone", defaultValue = "华东一区") String zone) {
        return autoDestroyDB.autoDestroyDB(zone);
    }

    @RequestMapping("/CreateMysqlTest")
    public String getMysqlTestResult(@RequestParam(value = "zone", defaultValue = "华东一区") String zone) {
        return autoCreateMysqlAndTestConnection.autoCreateMysqlAndTestConnection(zone);
    }

    @RequestMapping("/CreateRedisTest")
    public String getRedisTestResult(@RequestParam(value = "zone", defaultValue = "华东一区") String zone) {
        return autoCreateRedisAndTest.autoCreateRedisAndTest(zone);
    }


}
