package fr.preto_back;

import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.api_response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin
@AllArgsConstructor
public class TestController {

    @GetMapping
    public ResponseEntity<ApiResponse<TestDTO>> test() {

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(ApiCode.TEST.name(), ApiCode.TEST.getMessage(), new TestDTO(1, "This is a test!")));
    }
}

