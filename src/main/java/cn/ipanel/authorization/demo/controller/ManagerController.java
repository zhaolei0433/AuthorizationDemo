package cn.ipanel.authorization.demo.controller;

import cn.ipanel.authorization.demo.controller.request.ManagerLoginReq;
import cn.ipanel.authorization.demo.controller.respones.Result;
import cn.ipanel.authorization.demo.controller.respones.ManagerVO;
import cn.ipanel.authorization.demo.entity.ManagerInfo;
import cn.ipanel.authorization.demo.service.ManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhaolei
 * createTime 2018年9月17日 下午4:16:44
 */
@Api(tags = "管理员模块")
@RequestMapping(value = "/managerInfo")
@RestController
public class ManagerController {
    private static Logger logger = LoggerFactory.getLogger(ManagerController.class);
    private ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @ApiOperation(value = "根据姓名查找 ManagerInfo")
    @ApiImplicitParam(name = "name", value = "用户名", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public Result<List<ManagerInfo>> queryUsersByName(@RequestParam(value = "name") String name) {
        return new Result<>(managerService.queryUserInfo(name));
    }

    @CrossOrigin
    @ApiOperation(value = "管理员登录(POST)")
    @ApiImplicitParam(name = "req", value = "管理员登录", dataType = "ManagerLoginReq", required = true, paramType = "body")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<ManagerVO> userLoginP(@RequestBody ManagerLoginReq req, HttpServletRequest httpServletRequest) throws Exception {
        logger.info("ManagerLoginReq ==" + req);
        req.setIp(httpServletRequest.getRemoteHost());
        return new Result<>(managerService.pcLogin(req));
    }

    @Value("${test.username}")
    private String name;

    @ApiOperation(value = "测试获取配置文件信息")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Result<String> queryUsersByName() {
        return new Result<>(name);
        //return  null;
    }

}
