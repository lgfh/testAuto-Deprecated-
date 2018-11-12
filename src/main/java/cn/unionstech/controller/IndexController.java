package cn.unionstech.controller;

import cn.unionstech.application.AutoCreateGPUVm;
import cn.unionstech.application.AutoCreateVm;
import cn.unionstech.application.AutoDestroyDB;
import cn.unionstech.application.AutoDestroyVm;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WXY
 * @version 创建时间：2018/11/12
 */
@RestController
public class IndexController {

    @RequestMapping(path = "/CreateVm")
//    @ResponseBody
    public String getVmCreateResult() {

        return AutoCreateVm.autoCreateVM();
//        return "Hello";
    }

    @RequestMapping(path = "/CreateGpuVm")
    @ResponseBody
    public String getGpuVmCreateResult() {

        return AutoCreateGPUVm.autoCreateGpuVM();
    }


    @RequestMapping("/DelVm")
    public String getVmDelResult() {
        return AutoDestroyVm.autoDestroyVM();
    }

    @RequestMapping("/DelDB")
    public String getDbDelResult() {
        return AutoDestroyDB.autoDestroyDB();
    }
}
