package com.example.taskStorage.controller;

import com.example.taskStorage.model.Project;
import com.example.taskStorage.model.Task;
import com.example.taskStorage.service.ProjectService;
import com.example.taskStorage.service.TaskStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.HttpResource;

import javax.validation.Valid;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class TaskStorageController {

	private final TaskStorageService taskStorageService;
	private final ProjectService projectService;

	public TaskStorageController(TaskStorageService taskStorageService, ProjectService projectService) {
		this.taskStorageService = taskStorageService;
		this.projectService = projectService;
	}

	/**
	 * Method of the controller allows to get a list of existing projects.
	 *
	 * @return List of projects
	 */
	@GetMapping("/project")
	public List<Project> getProjects() {
		return projectService.findAll();
	}

	/**
	 * Method of the controller returns a project by ID.
	 *
	 * @param id index of existing project
	 * @return JSON object type of <bold>Project</bold>
	 * @see Project
	 */
	@GetMapping("/project/{id}")
	public Project getProject(@PathVariable Long id) {
		return projectService.findById(id);
	}

	/**
	 * Method of the controller for <bold>creating</bold> a new project.
	 *
	 * @param project - JSON representation of a project using as request body.
	 * @return Map where key is "id" type of string and value is index type of Long of a <bold>saved</bold> project.
	 */
	@PostMapping("/createProject")
	public Map<String, Long> createProject(@RequestBody @Valid Project project) {
		Project savedProject = projectService.createProject(project);
		return Map.of("id", savedProject.getId());
	}

	/**
	 * Method of the controller allows to <bold>change</bold> existing project.
	 *
	 * @param id index of an existing project
	 * @param project body of a new project
	 * @return Map where key is "id" type of string and value is index type of Long of a <bold>changed</bold> project.
	 */
	@PutMapping("/project/{id}")
	public Map<String, Long> changeProject(@PathVariable Long id, @RequestBody @Valid Project project) {
		Project savedProject = projectService.changeProject(id, project);
		return Map.of("id", savedProject.getId());
	}

	/**
	 * Method of the controller allows to <bold>delete</bold> an existing project.
	 *
	 * @param id index of existing project
	 * @return Map containing time and the message
	 */
	@DeleteMapping("/project/{id}")
	public Map<String, String> deleteProject(@PathVariable Long id) {
		projectService.deleteProject(id);

		return Map.of(
				"timestamp", LocalDateTime.now().toString(),
				"message", "Project was successfully deleted"
		);
	}

	/**
	 * Method of the controller allows to create a tasks within a project.
	 *
	 * @param projectId The ID of the project for which required to create tasks.
	 * @param task The body of a new task
	 * @return Message of success
	 */
	@PostMapping("/project/{projectId}/createTask")
	public Map<String, String> createTask(@PathVariable Long projectId, @RequestBody @Valid Task task) {
		projectService.addTask(projectId, task);
		return Map.of(
				"timestamp", LocalDateTime.now().toString(),
				"message", "Task was successfully added to the project"
		);
	}

	/**
	 * Method of the controller allows to delete a task from a project.
	 *
	 * @param projectId The ID of the project which for required to delete a task.
	 * @param taskId The ID of the task which required to delete.
	 * @return Message of success
	 */
	@DeleteMapping("/project/{projectId}/deleteTask/{taskId}")
	public Map<String, String> deleteTask(@PathVariable Long projectId, @PathVariable int taskId) {
		projectService.deleteTask(projectId, taskId);
		return Map.of("message", "Task was successfully deleted");
	}
}