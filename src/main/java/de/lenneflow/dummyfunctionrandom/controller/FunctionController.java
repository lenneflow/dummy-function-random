package de.lenneflow.dummyfunctionrandom.controller;

import de.lenneflow.dummyfunctionrandom.dto.FunctionPayload;
import de.lenneflow.dummyfunctionrandom.enums.RunStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/dummy/random")
@EnableAsync
@RequiredArgsConstructor
public class FunctionController {

    private static final Logger logger = LoggerFactory.getLogger(FunctionController.class);

    final RestTemplate restTemplate;
    Random random = new Random();


    @GetMapping("/ping")
    public String ping(){
        return "Dummy function random is working fine!";
    }

    @PostMapping("/start")
    @Async
    public void sleep(@RequestBody FunctionPayload functionPayload){
        String callBackUrl = functionPayload.getCallBackUrl();
        try {
            String keyMin = "minValue";
            String keyMax = "maxValue";
            int minValue = (int) functionPayload.getInputData().get(keyMin);
            int maxValue = (int) functionPayload.getInputData().get(keyMax);
            logger.info("Getting a random value between {} and {}", minValue, maxValue);
            int randomValue = random.nextInt(maxValue - minValue) + minValue;
            logger.info("Random value is {}", randomValue);
            functionPayload.setRunStatus(RunStatus.COMPLETED);
            Map<String, Object> output = new HashMap<>();
            output.put("randomValue" , randomValue);
            functionPayload.setOutputData(output);
            logger.info("call the callback url {}", callBackUrl);
            restTemplate.postForObject(callBackUrl, functionPayload, Void.class);
            logger.info("Payload sent successfully");
        }catch (Exception e){
            logger.error(e.getMessage());
            functionPayload.setRunStatus(RunStatus.FAILED);
            functionPayload.setFailureReason(e.getLocalizedMessage());
            restTemplate.postForObject(callBackUrl, functionPayload, Void.class);
        }
    }
}
