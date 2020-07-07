package holidayrequest;

import java.util.List;

import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.engine.impl.util.TaskHelper;

public class DeleteOthersTaskCmd implements Command<Void> {

	// 流程实例Id
    private String processInstanceId;
    // 当前任务Id
    private String exceptionTaskId;

    // 构造函数
    public DeleteOthersTaskCmd(String processInstanceId, String exceptionTaskId) {
        this.processInstanceId = processInstanceId;
        this.exceptionTaskId = exceptionTaskId;
    }

    @Override
    public Void execute(CommandContext commandContext) {
    	// 驳回
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager(commandContext);
        List<ExecutionEntity> executionEntities = executionEntityManager.findChildExecutionsByProcessInstanceId(processInstanceId);
        executionEntities.forEach(executionEntity -> {
        	// 包容网关中若有一个驳回，需删除其余的已经审批通过节点
        	Process process = ProcessDefinitionUtil.getProcess(executionEntity.getProcessDefinitionId());
       	 	for (FlowNode flowNode : process.findFlowElementsOfType(FlowNode.class)) {
                if (flowNode.getId().equals(executionEntity.getActivityId())) {
               	 	System.out.println("flowNode.getIdxxx: ) " + flowNode.getId() );
               	 	if (executionEntity.getTasks() == null || executionEntity.getTasks().size() == 0) {
               	 		executionEntityManager.deleteExecutionAndRelatedData(executionEntity, "包容网关某节点驳回，删除其他已审批任务", true);
               	 		executionEntityManager.deleteChildExecutions(executionEntity, "包容网关某节点驳回，删除其他已审批任务", true);
               	 		System.out.println("executionEntity.getTasks() == null, delete flowNode.getId(): ) " + flowNode.getId());
               	 	} else {
                    	executionEntity.getTasks().forEach(task -> {
                    		if (!flowNode.getId().equals(task.getTaskDefinitionKey()) && !task.getId().equals(exceptionTaskId)) {
                       	 		executionEntityManager.deleteExecutionAndRelatedData(executionEntity, "包容网关某节点驳回，删除其他已审批任务", true);
                       	 		executionEntityManager.deleteChildExecutions(executionEntity, "包容网关某节点驳回，删除其他已审批任务", true);
                       	 		TaskHelper.deleteTask(task, "包容网关某节点驳回，删除其他未审批任务", true, true, true);
                       	 		System.out.println("executionEntity.getTasks() : " + task.getTaskDefinitionKey() + "delete flowNode.getId(): ) " + flowNode.getId());
                    		}
                    	});
               	 	}
                }
            }
       	 
       	 	// 包容网关中若有一个驳回，需删除其余的未审批节点
        	executionEntity.getTasks().forEach(task -> {
    			System.out.println("ready to delete ) " + task.getTaskDefinitionKey() + " : "+ task.getName() + " taskId: " + task.getId() + "    Assignee: " + task.getAssignee());
        		if (!task.getId().equals(exceptionTaskId)) {
               	 	executionEntityManager.deleteExecutionAndRelatedData(executionEntity, "网关某节点驳回，删除其他未审批任务", true);
               	 	executionEntityManager.deleteChildExecutions(executionEntity, "网关某节点驳回，删除其他未审批任务", true);
        			TaskHelper.deleteTask(task, "包容网关某节点驳回，删除其他未审批任务", true, true, true);
        			System.out.println("executionEntity.getTasks() : " + task.getTaskDefinitionKey() + "delete Task: ) " + task.getTaskDefinitionKey());
        		}
        	});
        });
        
        return null;
    }
}
