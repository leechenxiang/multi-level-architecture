package com.itzixi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rest")
public class ThisIsRestController {

    @Value("${server.port}")
    private String SERVER_PORT;

    @GetMapping("get")
    public String getStu() {
        return "查询操作 port = " + SERVER_PORT;
    }

    @PostMapping("create")
    public String createStu() {
        return "保存操作 port = " + SERVER_PORT;
    }

    @PutMapping("update")
    public String updateStu() {
        return "修改操作 port = " + SERVER_PORT;
    }

    @DeleteMapping("delete")
    public String deleteStu() {
        return "删除操作 port = " + SERVER_PORT;
    }
}
