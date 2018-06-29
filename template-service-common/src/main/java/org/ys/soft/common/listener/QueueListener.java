package org.ys.soft.common.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ys.soft.common.model.LogMonitor;
import org.ys.soft.common.service.LogMonitorService;

/**
 * [队列监听器,消息的消费者]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
@Component
@RabbitListener(queues = "${system.log.queue}")
public class QueueListener {

	@Autowired
	private LogMonitorService logMonitorService;

	@RabbitHandler
	public void receiveSaveLogMessage(LogMonitor log) {
		logMonitorService.create(log);
	}
}
