package br.com.bluesburguer.orderingsystem.order.infra.sqs;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.bluesburguer.orderingsystem.domain.event.OrderStatusUpdated;
import br.com.bluesburguer.orderingsystem.order.infra.client.EventPublisher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Profile({"production", "dev", "test"})
public class OrderStatusUpdatedEventPublisher implements EventPublisher {
	
	@Value("${cloud.aws.queue.url}")
    private String queueUrl;

    private final AmazonSQS amazonSQS;

    private final ObjectMapper objectMapper;
    
    public OrderStatusUpdatedEventPublisher(AmazonSQS amazonSQS, ObjectMapper objectMapper) {
    	this.amazonSQS = amazonSQS;
    	this.objectMapper = objectMapper;
    }
	
	@Override	
	public void publish(OrderStatusUpdated event) {
		SendMessageRequest sendMessageRequest = null;
        try {
        	var groupId = alphanumericId();
    		var deduplicationId = alphanumericId();
    		
            sendMessageRequest = new SendMessageRequest().withQueueUrl(this.queueUrl)
                    .withMessageBody(objectMapper.writeValueAsString(event))
                    .withMessageGroupId(groupId)
                    .withMessageDeduplicationId(deduplicationId);
            amazonSQS.sendMessage(sendMessageRequest);
            log.info("Event has been published in SQS with id {}", event.getId());
        } catch (JsonProcessingException e) {
        	log.error("JsonProcessingException e : {} and stacktrace : {}", e.getMessage(), e);
        } catch (Exception e) {
        	log.error("Exception ocurred while pushing event to sqs : {} and stacktrace ; {}", e.getMessage(), e);
        }
	}

	private String alphanumericId() {
		return RandomStringUtils.randomAlphanumeric(10);
	}
}
