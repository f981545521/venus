package cn.acyou.base.controller;

import cn.acyou.base.mapper.BossMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author youfang
 * @version [1.0.0, 2018-09-29 下午 04:22]
 **/
@Controller
@RequestMapping("hello")
public class HelloWordController {

    @Autowired
    private BossMapper bossMapper;

    @RequestMapping
    @ResponseBody
    public Object hello(){
        bossMapper.selectAll();
        return "hello";
    }
}
