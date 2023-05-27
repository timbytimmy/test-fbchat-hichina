package com.hichina.admin.hichinaadminbackend.service;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HichinaOpenAiService {
    @Value("${chatgpt.model}")
    private String model;

    private OpenAiService service;

    @Value("${openai.token}")
    public void setNameStatic(String tokenAndTimeout){
        String[] args = tokenAndTimeout.split(",");
        // key trick: the timeout here is very important
        service = new OpenAiService(args[0], Integer.parseInt(args[1]));
    }

    private String generateDescriptionRequest(String destinationName){
        return String.format("Please give me a travel introduction of %s in 50 words in English", destinationName);
    }

    public String generateShortDescription(String destinationName){
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model(model).topP(1.0).temperature(0.0).maxTokens(100)
                .prompt(generateDescriptionRequest(destinationName))
                .echo(false)
                .user("hichinaadmin")
                .build();

        List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();

        if(choices==null || choices.size()<1){
            return "机器人犯迷糊了~";
        }else{
            return choices.get(0).getText();
        }
    }
}
