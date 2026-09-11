package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.common.annotation.RequiresPermission;
import com.cbec.entity.dto.StatisticsDTO;
import com.cbec.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @RequiresPermission("dashboard:view")
    @GetMapping("/overview")
    public Result<StatisticsDTO> getOverview() {
        return Result.success(statisticsService.getOverview());
    }
}