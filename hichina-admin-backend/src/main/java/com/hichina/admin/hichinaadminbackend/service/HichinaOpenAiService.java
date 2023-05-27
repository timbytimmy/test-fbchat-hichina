package com.hichina.admin.hichinaadminbackend.service;

import com.lilittlecat.chatgpt.offical.ChatGPT;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.Duration.ofSeconds;

@Service
public class HichinaOpenAiService {
    private static final Logger LOG = LoggerFactory.getLogger(HichinaOpenAiService.class);

    @Value("${chatgpt.model}")
    private String model;

    private OpenAiService service;

    private ChatGPT chatGPT;

    @Value("${openai.token}")
    public void setNameStatic(String tokenAndTimeout){
        String[] args = tokenAndTimeout.split(",");
        // key trick: the timeout here is very important
        LOG.info(String.format("===init openai args: %s, %s",args[0],args[1]));
        service = new OpenAiService(args[0], ofSeconds(Integer.parseInt(args[1])));

        chatGPT = new ChatGPT(args[0]);
    }

    private String generateDescriptionRequest(String destinationName){
        return String.format("Please give me a travel introduction of %s in 50 words in English", destinationName);
    }

    public String generateShortDescriptionV2(String destinationName){
        String ret = chatGPT.ask(generateDescriptionRequest(destinationName));
        return ret;
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
