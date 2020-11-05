package com.djoker.ars.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *  @ControllerAdvice là một chuyên ngành của chú thích @Component 
 *  cho phép xử lý các ngoại lệ trên toàn bộ ứng dụng trong một thành phần xử lý chung.
 *  Nó có thể được xem như một phương thức chặn các ngoại lệ được ném ra bởi các phương thức được chú thích bằng @RequestMapping và tương tự.
*/

/**
 * @ExceptionHandler: để xử lý các ngoại lệ trong các lớp xử lý cụ thể và (/hoặc) các phương thức xử lý.
 */

/**
 * ResponseEntity: Đây là đối tượng cha của mọi response (ngoài ra nó còn là kiểu Generic) và sẽ wrapper các object trả về
 * ResponseEntity: Đại diện cho toàn bộ HTTP response(status code, headers, and body)
 *                 do đó, ta có thể sử dụng nó để cấu hình đầy đủ HTTP response.
 */

@ControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler(CustomerException.class)
	public ResponseEntity<ErrorResponse> exceptionCustomerHandler(Exception exception) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(HttpStatus.NOT_FOUND.value());
		errorResponse.setMessage(exception.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception exception) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		error.setMessage("The request could not be managed by the server due to incorrect data");
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}
	
}
