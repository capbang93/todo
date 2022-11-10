package com.example.todo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.TodoDTO;
import com.example.todo.model.TodoEntity;
import com.example.todo.service.TodoService;

import lombok.extern.slf4j.Slf4j;


/*@CrossOrigin(origins = "*")
 * 
 */
@Slf4j
@RestController
@RequestMapping("todo")
public class TodoController {
	
	@Autowired
	private TodoService service;
	

	@PostMapping
	public ResponseEntity<?> createTodo(
			@AuthenticationPrincipal String userId,
			@RequestBody TodoDTO dto) {
		try {
		/* POST localhost:8080/todo
		 * {
		 * 		"title" : "My first todo",
		 * 		"done" : false
		 * }
		 */

		// dto 를 이용해 테이블에 저장하기 위한 entity를 생성한다.
		TodoEntity entity = TodoDTO.toEntity(dto);
		// entity userId를 임시로 지정한다.
		entity.setId(null);
		entity.setUserId(userId);
		
		// service.create 를 통해 repository 에 entity를 저장한다.
		// 이때 넘어노는 값이 없을 수도 있으므로 List가 아닌 Optional 로 한다.
		List<TodoEntity> entities = service.create(entity);
		
		// entities 를 dtos 로 스트림 변환한다.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		log.info("Log:entities => dtos ok!");		

		// Response DTO를 생성한다.
		/* {
		 * 		"error" : null,
		 * 		"data": [
		 * 			{
		 * 				"id" : "402809817ed71ddf017ed71dfe720000",
		 * 				"title" : "My first todo",
		 * 				"done" : false
		 * 			}
		 * } 
		 */
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		log.info("Log:responsedto ok!");
		
		// HTTP Status 200 상태로 response 를 전송한다.
		return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping
	public ResponseEntity<?> retrieveTodo(@AuthenticationPrincipal String userId) {
		
		List<TodoEntity> entities = service.retrieve(userId);
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// HTTP Status 200 상태로 response 를 전송한다.
		return ResponseEntity.ok().body(response);
		
	}
	

	@PutMapping
	public ResponseEntity<?> updateTodo(
			@AuthenticationPrincipal String userId,
			@RequestBody TodoDTO dto) {
		try {
		/* PUT localhost:8080/todo
		 * {
		 * 		"id" : "????????"
		 * 		"title" : "Update first todo",
		 * 		"done" : true
		 * }
		 */
		// dto 를 이용해 테이블에 저장하기 위한 entity를 생성한다.
		TodoEntity entity = TodoDTO.toEntity(dto);

		// entity userId를 임시로 지정한다.
		entity.setUserId(userId);
		
		// service.create 를 통해 repository 에 entity를 저장한다.
		// 이때 넘어노는 값이 없을 수도 있으므로 List가 아닌 Optional 로 한다.
		List<TodoEntity> entities = service.update(entity);
		
		// entities 를 dtos 로 스트림 변환한다.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
	
		// Response DTO를 생성한다.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// HTTP Status 200 상태로 response 를 전송한다.
		return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
		
	}
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(
			@AuthenticationPrincipal String userId,
			@RequestBody TodoDTO dto) {
		try {
			TodoEntity entity = TodoDTO.toEntity(dto);

			// entity userId를 임시로 지정한다.
			entity.setUserId(userId);

			List<TodoEntity> entities = service.delete(entity);
			
			// entities 를 dtos 로 스트림 변환한다.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
			// Response DTO를 생성한다.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// HTTP Status 200 상태로 response 를 전송한다.
			return ResponseEntity.ok().body(response);
			} catch (Exception e) {
				String error = e.getMessage();
				ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
				return ResponseEntity.badRequest().body(response);
			}			
	
	}
}
