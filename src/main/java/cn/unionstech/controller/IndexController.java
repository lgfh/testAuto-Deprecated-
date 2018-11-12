package cn.unionstech.controller;

import cn.unionstech.application.AutoCreateGPUVm;
import cn.unionstech.application.AutoCreateVm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author WXY
 * @version 创建时间：2018/11/12
 */
@Controller
public class IndexController {

    @RequestMapping(path = "/CreateVm")
    @ResponseBody
    public String getVmCreateResult() {

        return AutoCreateVm.autoCreateVM();
//        return "Hello";
    }

    @RequestMapping(path = "/CreateGpuVm")
    @ResponseBody
    public String getGpuVmCreateResult() {

        return AutoCreateGPUVm.autoCreateGpuVM();
    }
}
