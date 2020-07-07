package holidayrequest;

import com.alibaba.fastjson.JSON;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoachLeaveRequest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// 创建流程引擎配置
		ProcessEngineConfiguration  cfg = new StandaloneProcessEngineConfiguration();
		cfg.setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
		.setJdbcUsername("sa")
		.setJdbcPassword("")
		.setJdbcDriver("org.h2.Driver")
		.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		
//
//		ProcessEngineConfiguration  cfg = new StandaloneProcessEngineConfiguration();
//		cfg.setJdbcUrl("jdbc:mysql://localhost:3306/test-flowable?characterEncoding=UTF-8&useUnicode=true&useSSL=false")
//		.setJdbcUsername("root")
//		.setJdbcPassword("123456")
//		.setJdbcDriver("com.mysql.jdbc.Driver")
//		.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		
		// 创建流程引擎
		ProcessEngine processEngine = cfg.buildProcessEngine();
		
		// 获取注册服务，并发布流程定义模板
		RepositoryService repositoryService = processEngine.getRepositoryService();
		Deployment deployment = repositoryService.createDeployment().addClasspathResource("CoachLeaveRequest.bpmn20.xml").deploy();
		
		// 验证流程定义模板是否注册成功
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		System.out.println("Found process definition : " + processDefinition.getName());
		
		// 获取请假申请启动任务
		RuntimeService runtimeService = processEngine.getRuntimeService();
		
		ManagementService managementService = processEngine.getManagementService();
		// 获取任务管理服务
		TaskService taskService = processEngine.getTaskService();
		HistoryService historyService = processEngine.getHistoryService();
		List<Task> tasks = taskService.createTaskQuery().list();
		
		
		if (true) {
			/**判断流程是否结束，查询正在执行的执行对象表*/
	        ProcessInstance rpi2 = runtimeService.createProcessInstanceQuery()//创建流程实例查询对象
	                        .processInstanceId("50005").singleResult();
	        System.out.println("rpi2: " + rpi2);
	        
//	        runtimeService.deleteProcessInstance("50005", "删除流程50005"); 
//	        historyService.deleteHistoricProcessInstance("50005");
	        
			// 我的任务
	        List<HistoricTaskInstance> hTasks = historyService.createHistoricTaskInstanceQuery()
	        		.or().taskAssignee("headmasterApprovalGroup")
	        		.taskCandidateUser("headmasterApprovalGroup")
	        		.taskCandidateGroup("headmasterApprovalGroup")
	        		.endOr().orderByTaskCreateTime().asc().list();
			System.out.println("headmasterApprovalGroup: You have " + hTasks.size() + " historic tasks:");
			for (int i = 0; i < hTasks.size(); i++) {
				HistoricTaskInstance task = hTasks.get(i);
//				System.out.println((i + 1) + ") " + " rpi.id: " + task.getProcessInstanceId() + " taskId: " + task.getId() + "    " + task.getName() + " endTime: " + task.getEndTime());
			}
		
			
//			System.out.println("createHistoricVariableInstanceQuery: " + JSON.toJSONString( historyService.createHistoricVariableInstanceQuery().processInstanceId(task.getProcessInstanceId()).taskIds(hTasks.).variableName("approvalResult").list() ));
			
			for (int i = 0; i < hTasks.size(); i++) {
				HistoricTaskInstance task = hTasks.get(i);
				if (task.getDeleteReason() == null && task.getEndTime() != null) {
					System.out.println((i + 1) + ") " 
						+ " taskId: " + task.getId() 
						+ " key: "+ task.getTaskDefinitionKey() 
						+ " : "+ task.getName() 
						+ " Assignee: " + task.getAssignee()
						+ " 审批结果: " + JSON.toJSONString(historyService.createHistoricVariableInstanceQuery().processInstanceId(task.getProcessInstanceId()).taskId(task.getId()).variableName("approvalResult").singleResult().getValue())
						+ " 审批意见: " + JSON.toJSONString(taskService.getTaskComments(task.getId()).get(0).getFullMessage() )
					);
				}
			} 
			
			
			// 我的任务
			tasks = taskService.createTaskQuery()
	        		.or().taskAssignee("headmasterApprovalGroup")
	        		.taskCandidateUser("headmasterApprovalGroup")
	        		.taskCandidateGroup("headmasterApprovalGroup")
	        		.endOr().orderByTaskCreateTime().asc().list();
			System.out.println("headmasterApprovalGroup: You have " + tasks.size() + " tasks:");
			for (int i = 0; i < tasks.size(); i++) {
				Task task = tasks.get(i);
				System.out.println((i + 1) + ") " + " rpi.id: " + task.getProcessInstanceId() + " taskId: " + task.getId() + "    " + task.getName() + " getOwner: " + task.getOwner());
			}
			
//			return;
		}
		
		
		// 设置请假流程参数
//		Map<String, Object> variables6 = new HashMap<String, Object>();
//		variables6.put("requestAthlete", "六天");
//		variables6.put("requestDate", "2020-03-20");
//		variables6.put("fromDate", "2020-03-21");
//		variables6.put("toDate", "2020-03-26");
//		variables6.put("requestDays", "6");
//		variables6.put("approvalCoach", "教练员");
//		variables6.put("approvalTeacher", "班主任");
//		variables6.put("isNeedClassTeacherApproval", true);
//		variables6.put("isNeedTrainingApproval", true);

		// 设置请假流程参数
//		Map<String, Object> variables7 = new HashMap<String, Object>();
//		variables7.put("requestAthlete", "七天");
//		variables7.put("requestDate", "2020-03-20");
//		variables7.put("fromDate", "2020-03-21");
//		variables7.put("toDate", "2020-03-27");
//		variables7.put("requestDays", "7");
//		variables7.put("approvalCoach", "教练员");
//		variables7.put("approvalTeacher", "班主任");
//		variables7.put("isNeedClassTeacherApproval", true);
//		variables7.put("isNeedTrainingApproval", true);
		
		// 设置请假流程参数
//		Map<String, Object> variables30 = new HashMap<String, Object>();
//		variables30.put("requestAthlete", "三十天");
//		variables30.put("requestDate", "2020-03-20");
//		variables30.put("fromDate", "2020-03-21");
//		variables30.put("toDate", "2020-03-26");
//		variables30.put("requestDays", "30");
//		variables30.put("approvalCoach", "教练员");
//		variables30.put("approvalTeacher", "班主任");
//		variables30.put("isNeedClassTeacherApproval", true);
//		variables30.put("isNeedTrainingApproval", true);

		Map<String, Object> variables30 = new HashMap<String, Object>();
		variables30.put("requestCoach", "三十天");
		variables30.put("requestDate", "2020-03-20");
		variables30.put("fromDate", "2020-03-21");
		variables30.put("toDate", "2020-03-26");
		variables30.put("requestDays", "30");
//		variables30.put("approvalCoach", "教练员");
//		variables30.put("approvalTeacher", "班主任");
//		variables30.put("isNeedClassTeacherApproval", true);
//		variables30.put("isNeedTrainingApproval", true);
		
		// 创建请假流程，并设置流程发起人
		Authentication.setAuthenticatedUserId(variables30.get("requestCoach").toString());
//		Authentication.setAuthenticatedUserId(variables30.get("requestAthlete").toString());
		ProcessInstance processInstance6 = runtimeService.startProcessInstanceByKey("coachLeave", "businessKey", variables30);
		Authentication.setAuthenticatedUserId(null);
		System.out.println("processInstance6.getId: " + processInstance6.getId());
		System.out.println("processInstance6.getProcessVariables: " + processInstance6.getProcessVariables());
		
		int cnt = 0;
		/**判断流程是否结束，查询正在执行的执行对象表*/
        ProcessInstance rpi = runtimeService.createProcessInstanceQuery()//创建流程实例查询对象
                        .processInstanceId(processInstance6.getId()).singleResult();
        
        
        boolean isCoachReject = true;
        boolean isTeacherReject = true;
        boolean isTrainingReject = true;
        boolean isHeadmasterReject = true;
        boolean isAdminReject = true;
        while (rpi != null) {
			System.out.println("cnt: " + cnt);
			cnt++;
			if (cnt>=50) {
				break;
			}
			
			tasks = taskService.createTaskQuery().processInstanceId(processInstance6.getId()).list();
			System.out.println("You have " + tasks.size() + " tasks:");
			for (int i = 0; i < tasks.size(); i++) {
				Task task = tasks.get(i);
				System.out.println((i + 1) + ") " 
						+ task.getTaskDefinitionKey() + " : "+ task.getName() 
						+ " taskId: " + task.getId() 
						+ "    Assignee: " + task.getAssignee()
						+ " ExecutionId" + task.getExecutionId());
				

//				BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(task.getProcessDefinitionId());      	 
//				ExecutionEntity executionEntity = (ExecutionEntity) processEngine.getRuntimeService().createExecutionQuery()
//						.executionId(task.getExecutionId()).singleResult();
//				// 当前节点
//				FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(executionEntity.getActivityId());
//				// 输出连线
//		        List<SequenceFlow> outFlows = flowNode.getOutgoingFlows();
//		        outFlows.forEach(sequenceFlow -> {
//		        	 // 下一个审批节点
//		            FlowElement targetFlow = sequenceFlow.getTargetFlowElement();
//		            System.out.println("当前节点"
//		            		+ " sequenceFlow.getId: " + sequenceFlow.getId()
//		            		+ " sequenceFlow.getName: " + sequenceFlow.getName());
//		            System.out.println("下一节点"
//		            		+ " targetFlow.getId: " + targetFlow.getId()
//		            		+ " targetFlow.getName: " + targetFlow.getName());
//		        });
		        
				
				Map<String, Object> processVariables = taskService.getVariables(task.getId());
				
				if (task.getTaskDefinitionKey().equals("coachRequest")) {
					taskService.setVariable(task.getId(), "approvalResult", "申请");
					taskService.setVariableLocal(task.getId(), "approvalResult", "申请");
					taskService.setVariableLocal(task.getId(), "assignee", "六天");
					taskService.addComment(task.getId(), processInstance6.getId(), "六天的请假申请。");
					taskService.claim(task.getId(), processVariables.get("requestCoach").toString());
					taskService.addCandidateUser(task.getId(), processVariables.get("requestCoach").toString());
					taskService.complete(task.getId());
				}
//				if (task.getTaskDefinitionKey().equals("coachApprovalTask")) {
//					if (isCoachReject) {
//						isCoachReject = false;
//						taskService.setVariable(task.getId(), "approvalResult", "驳回");
//						taskService.setVariableLocal(task.getId(), "approvalResult", "驳回");
//						taskService.addComment(task.getId(), processInstance6.getId(), "六天的请假申请，教练员驳回。");
//						taskService.claim(task.getId(), processVariables.get("approvalCoach").toString());
//						managementService.executeCommand(new DeleteOthersTaskCmd(processInstance6.getId(), task.getId()));
//						taskService.complete(task.getId());
//						break;
//					} else {
//						taskService.setVariable(task.getId(), "approvalResult", "通过");
//						taskService.setVariableLocal(task.getId(), "approvalResult", "通过");
//						taskService.addComment(task.getId(), processInstance6.getId(), "六天的请假申请，教练员同意。");
//						taskService.complete(task.getId());
//					}
//				}
//				if (task.getTaskDefinitionKey().equals("teacherApprovalTask")) {
//					if (isTeacherReject) {
//						isTeacherReject = false;
//						taskService.setVariable(task.getId(), "approvalResult", "驳回");
//						taskService.setVariableLocal(task.getId(), "approvalResult", "驳回");
//						taskService.addComment(task.getId(), processInstance6.getId(), "六天的请假申请，教练员同意，班主任驳回。");
//						managementService.executeCommand(new DeleteOthersTaskCmd(processInstance6.getId(), task.getId()));
//						taskService.complete(task.getId(), processVariables);
//						System.out.println("processVariables: " + processVariables);
//						break;
//					} else {
//						taskService.setVariable(task.getId(), "approvalResult", "通过");
//						taskService.setVariableLocal(task.getId(), "approvalResult", "通过");
//						taskService.addComment(task.getId(), processInstance6.getId(), "六天的请假申请，教练员同意，班主任同意。");
//						taskService.complete(task.getId());
//					}
//				}
				/**
				 * 训练员审批
				 */
				if (task.getTaskDefinitionKey().equals("trainingApprovalTask")) {
					if (isTrainingReject) {
						isTrainingReject = false;
						taskService.setVariable(task.getId(), "approvalResult", "驳回");
						taskService.setVariableLocal(task.getId(), "approvalResult", "驳回");
						taskService.addComment(task.getId(), processInstance6.getId(), "六天的请假申请，训练科驳回。");
						managementService.executeCommand(new DeleteOthersTaskCmd(processInstance6.getId(), task.getId()));
						taskService.complete(task.getId());
						break;
					} else {
						taskService.setVariable(task.getId(), "approvalResult", "通过");
						taskService.setVariableLocal(task.getId(), "approvalResult", "通过");
						taskService.addComment(task.getId(), processInstance6.getId(), "六天的请假申请，训练科同意。");
						taskService.complete(task.getId());
					}
				}
				/**
				 * 副校长审批
				 */
				if (task.getTaskDefinitionKey().equals("viceHeadmasterApprovalTask")) {
					if (isHeadmasterReject) {
						isHeadmasterReject = false;
						taskService.setVariable(task.getId(), "approvalResult", "驳回");
						taskService.setVariableLocal(task.getId(), "approvalResult", "驳回");
						taskService.addComment(task.getId(), processInstance6.getId(), "六天的请假申请，副校长驳回。");
						managementService.executeCommand(new DeleteOthersTaskCmd(processInstance6.getId(), task.getId()));
						taskService.complete(task.getId());
						break;
					} else {
						taskService.setVariable(task.getId(), "approvalResult", "通过");
						taskService.setVariableLocal(task.getId(), "approvalResult", "通过");
						taskService.addComment(task.getId(), processInstance6.getId(), "六天的请假申请，副校长同意。");
						taskService.complete(task.getId());
					}

				}


				/**
				 * 校长审批
				 */

				if (task.getTaskDefinitionKey().equals("headmasterApprovalTask")) {
					if (isHeadmasterReject) {
						isHeadmasterReject = false;
						taskService.setVariable(task.getId(), "approvalResult", "驳回");
						taskService.setVariableLocal(task.getId(), "approvalResult", "驳回");
						taskService.addComment(task.getId(), processInstance6.getId(), "六天的请假申请，校长驳回。");
						managementService.executeCommand(new DeleteOthersTaskCmd(processInstance6.getId(), task.getId()));
						taskService.complete(task.getId());
						break;
					} else {
						taskService.setVariable(task.getId(), "approvalResult", "通过");
						taskService.setVariableLocal(task.getId(), "approvalResult", "通过");
						taskService.addComment(task.getId(), processInstance6.getId(), "六天的请假申请，校长同意。");
						taskService.complete(task.getId());
					}
					
				}
//				if (task.getTaskDefinitionKey().equals("adminApprovalTask")) {
//					if (isAdminReject) {
//						isAdminReject = false;
//						taskService.setVariable(task.getId(), "approvalResult", "驳回");
//						taskService.setVariableLocal(task.getId(), "approvalResult", "驳回");
//						taskService.addComment(task.getId(), processInstance6.getId(), "六天的请假申请，教练员同意，班主任同意，训练科同意，管理员驳回。");
//						managementService.executeCommand(new DeleteOthersTaskCmd(processInstance6.getId(), task.getId()));
//						taskService.complete(task.getId());
//						break;
//					} else {
////						taskService.setVariable(task.getId(), "approvalResult", "通过");
////						taskService.setVariableLocal(task.getId(), "approvalResult", "通过");
////						taskService.addComment(task.getId(), processInstance6.getId(), "六天的请假申请，教练员同意，班主任同意，训练科同意，管理员同意。");
////						taskService.complete(task.getId());
//					}
//
//				}
				
			}
			
			/**判断流程是否结束，查询正在执行的执行对象表*/
	       rpi = runtimeService.createProcessInstanceQuery()//创建流程实例查询对象
                    .processInstanceId(processInstance6.getId()).singleResult();
		}
		
		
		
		List<HistoricTaskInstance> hTasks = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstance6.getId())
				.orderByTaskCreateTime().asc()
				.listPage(0, 50);
		
		hTasks.forEach(task -> {
			if (task.getDeleteReason() == null) {
				List<HistoricVariableInstance> historicVariableInstance = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance6.getId()).taskId(task.getId()).list();
				historicVariableInstance.forEach(variable -> {
					System.out.println(" taskId: " + task.getId() + "    "+ variable.getValue()  + "    " +  JSON.toJSONString(variable));
				});
			}
		});
		
		for (int i = 0; i < hTasks.size(); i++) {
			HistoricTaskInstance task = hTasks.get(i);
			if (task.getDeleteReason() == null && task.getEndTime() != null) {
				System.out.println((i + 1) + ") " 
					+ " taskId: " + task.getId() 
					+ " key: "+ task.getTaskDefinitionKey() 
					+ " : "+ task.getName() 
					+ " Assignee: " + task.getAssignee()
					+ " 审批结果: " + JSON.toJSONString(historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance6.getId()).taskId(task.getId()).variableName("approvalResult").singleResult().getValue())
					+ " 审批意见: " + JSON.toJSONString(taskService.getTaskComments(task.getId()).get(0).getFullMessage() )
				);
			}
		} 
		
		// managers task;
		
		
		
		// 我的任务
		hTasks = historyService.createHistoricTaskInstanceQuery().or().taskAssignee("headmasterApprovalGroup").taskCandidateUser("headmasterApprovalGroup").taskCandidateGroup("headmasterApprovalGroup").endOr().orderByTaskCreateTime().asc().list();
		System.out.println("班主任: You have " + hTasks.size() + " historic tasks:");
		for (int i = 0; i < hTasks.size(); i++) {
			HistoricTaskInstance task = hTasks.get(i);
			System.out.println((i + 1) + ") " + " taskId: " + task.getId() + "    " + task.getName() + " endTime: " + task.getEndTime());
		}
		
	}
	
	
	
	

}
