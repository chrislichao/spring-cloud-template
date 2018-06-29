package org.ys.soft.common.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ys.soft.common.model.LogMonitor;
import org.ys.soft.common.service.RedisCommonService;

@RestController
public class CommonController {

	@Autowired
	private RedisCommonService redisCommonSerive;

	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Autowired
	private Queue systemLogQueue;

	@RequestMapping(value = "/getVisitCount")
	public long getVisitCount(long timeout, String visitUrl) {
		return redisCommonSerive.getVisitCount(timeout, visitUrl);
	}

	@RequestMapping(value = "/saveLog")
	public void saveLog(LogMonitor log) {
		rabbitTemplate.convertAndSend(systemLogQueue.getName(), log);
	}
}
