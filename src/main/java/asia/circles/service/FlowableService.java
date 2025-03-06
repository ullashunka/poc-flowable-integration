package asia.circles.service;

import java.util.List;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlowableService {

	private final RuntimeService runtimeService;

	private final TaskService taskService;

	public FlowableService(RuntimeService runtimeService, TaskService taskService) {
		this.runtimeService = runtimeService;
		this.taskService = taskService;
	}

	@Transactional
	public String createProcess() {
		ProcessInstance instance = runtimeService.startProcessInstanceByKey("oneTaskProcess");
		return instance.getActivityId();
	}

	@Transactional
	public List<Task> getTasks(String assignee) {
		return taskService.createTaskQuery().taskAssignee(assignee).list();
	}

	public Task getTaskById(String id) {
		
		List<Task> list = taskService.createTaskQuery().taskId(id).list();
		return list.get(0);
	}

	public List<Task> getTaskByState(String state) {
		return taskService.createTaskQuery().processInstanceId(state).list();
	}
}