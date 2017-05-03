
package com.dicoding.menirukanmu;

import com.google.gson.Gson;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.Response;
import java.util.Collections;

import java.util.List;
import java.util.ArrayList;

import com.linecorp.bot.model.message.*;
import com.linecorp.bot.model.message.template.*;
import com.linecorp.bot.model.action.*;
import java.io.IOException;

/* JSON Reader */
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/* End JSON Reader */


@RestController
@RequestMapping(value="/linebot")
public class LineBotController
{
    @Autowired
    @Qualifier("com.linecorp.channel_secret")
    String lChannelSecret;
    
    @Autowired
    @Qualifier("com.linecorp.channel_access_token")
    String lChannelAccessToken;

    @RequestMapping(value="/callback", method=RequestMethod.POST)
    public ResponseEntity<String> callback(
        @RequestHeader("X-Line-Signature") String aXLineSignature,
        @RequestBody String aPayload)
    {
		
		// Otentifikasi LINE
        final String text=String.format("The Signature is: %s",
            (aXLineSignature!=null && aXLineSignature.length() > 0) ? aXLineSignature : "N/A");
        System.out.println(text);

        final boolean valid=new LineSignatureValidator(lChannelSecret.getBytes()).validateSignature(aPayload.getBytes(), aXLineSignature);
        System.out.println("The signature is: " + (valid ? "valid" : "tidak valid"));
        if(aPayload!=null && aPayload.length() > 0)
        {
            System.out.println("Payload: " + aPayload);
        }
        Gson gson = new Gson();
        Payload payload = gson.fromJson(aPayload, Payload.class);

        String msgText = " ";
        String idTarget = " ";
        String eventType = payload.events[0].type;

		
		if (eventType.equals("message")){

			String subuh = " ";
			String dzuhur = " ";
			String ashr = " ";
			String maghrib = " ";
			String isya = " ";
		
			/* JSON Reader */
			final String filePath = "BotProduktif.json";
				
				try {
				// read the json file
				FileReader reader = new FileReader(filePath);

				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

				// get a String from the JSON object
				subuh = (String) jsonObject.get("subuh");
				dzuhur = (String) jsonObject.get("dzuhur");
				ashr = (String) jsonObject.get("ashr");
				maghrib = (String) jsonObject.get("maghrib");
				isya = (String) jsonObject.get("isya");

			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ParseException ex) {
				ex.printStackTrace();
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}
			/* End JSON Reader */
			
			if (payload.events[0].source.type.equals("group")){
                idTarget = payload.events[0].source.groupId;
            } else if (payload.events[0].source.type.equals("room")){
                idTarget = payload.events[0].source.roomId;
            } else if (payload.events[0].source.type.equals("user")){
                idTarget = payload.events[0].source.userId;
            }

			String dataAdzan = new StringBuilder().append("Subuh: ").append(subuh).append("\nDzuhur: ").append(dzuhur).append("\nAshr: ").append(ashr).append("\nMaghrib: ").append(maghrib).append("\nIsya: ").append(isya).toString();

//			"Subuh: " + subuh + "\nDzuhur: " + dzuhur + "\nAshr: " + ashr + "\nMaghrib: " + maghrib + "\nIsya: " + isya;
			String msg = payload.events[0].message.text;
			String[] parts = msg.split(" ");
			
			if(parts[0].equals("bot")){
				if(parts[1].equals("informasi")){
					try {
                        getMessageData(dataAdzan, idTarget);
                    } catch (IOException e) {
                        System.out.println("Exception is raised ");
                        e.printStackTrace();
                    }
				}
				else if(parts[1].equals("reminder")){
					try {
						/* Button Template */
						// List<Action> actions = new ArrayList<Action>();
						// Action action = new URIAction("Google", "http://google.com");
						// actions.add(action);
						// Template temp = new ButtonsTemplate("https://storage.googleapis.com/gweb-uniblog-publish-prod/static/blog/images/google-200x200.7714256da16f.png","Google","Ini alamat google", actions);
						// TemplateMessage tempMsg = new TemplateMessage("ini altText", temp);

						/* Carousel Template */
						// List<Action> actions = new ArrayList<Action>();
						// Action action1 = new URIAction("Google", "http://google.com");
						// Action action2 = new URIAction("Google", "http://google.com");
						// Action action3 = new URIAction("Google", "http://google.com");
						// actions.add(action1);
						// actions.add(action2);
						// actions.add(action3);
						// CarouselColumn cColumn1 = new CarouselColumn("https://storage.googleapis.com/gweb-uniblog-publish-prod/static/blog/images/google-200x200.7714256da16f.png","Google","Ini alamat google", actions);
						// CarouselColumn cColumn2 = new CarouselColumn("https://storage.googleapis.com/gweb-uniblog-publish-prod/static/blog/images/google-200x200.7714256da16f.png","Google","Ini alamat google", actions);
						// CarouselColumn cColumn3 = new CarouselColumn("https://storage.googleapis.com/gweb-uniblog-publish-prod/static/blog/images/google-200x200.7714256da16f.png","Google","Ini alamat google", actions);
						// List<CarouselColumn> cColumns = new ArrayList<CarouselColumn>();
						// cColumns.add(cColumn1);
						// cColumns.add(cColumn2);
						// cColumns.add(cColumn3);
						// Template temp = new CarouselTemplate(cColumns);
						// TemplateMessage tempMsg = new TemplateMessage("ini altText", temp);

						/* Confirm Template */
						List<Action> actions = new ArrayList<Action>();
						Action action = new URIAction("Google", "http://google.com");
						actions.add(action);
						Action actionLeft = new URIAction("Action Left", "http://google.com");
						Action actionRight = new URIAction("Action Right", "http://google.com");
						Template temp = new ConfirmTemplate("google Text", actionLeft, actionRight);
						TemplateMessage tempMsg = new TemplateMessage("ini altText", temp);
						
						sendButtonTempalte(tempMsg, idTarget);
                    } catch (Exception e) {
                        System.out.println("Exception is raised ");
                        e.printStackTrace();
                    }
				}
			}
		}
		
		/* changable */
        // if (eventType.equals("join")){
            // if (payload.events[0].source.type.equals("group")){
                // replyToUser(payload.events[0].replyToken, "Hello Group");
            // }
            // if (payload.events[0].source.type.equals("room")){
                // replyToUser(payload.events[0].replyToken, "Hello Room");
            // }
        // } else if (eventType.equals("message")){
            // if (payload.events[0].source.type.equals("group")){
                // idTarget = payload.events[0].source.groupId;
            // } else if (payload.events[0].source.type.equals("room")){
                // idTarget = payload.events[0].source.roomId;
            // } else if (payload.events[0].source.type.equals("user")){
                // idTarget = payload.events[0].source.userId;
            // }

            // if (!payload.events[0].message.type.equals("text")){
                // replyToUser(payload.events[0].replyToken, "Unknown message");
            // } else {
                // msgText = payload.events[0].message.text;
                // msgText = msgText.toLowerCase();

                // if (!msgText.contains("bot leave")){
                    // try {
                        // getMessageData(msgText, idTarget);
                    // } catch (IOException e) {
                        // System.out.println("Exception is raised ");
                        // e.printStackTrace();
                    // }
                // } else {
                    // if (payload.events[0].source.type.equals("group")){
                        // leaveGR(payload.events[0].source.groupId, "group");
                    // } else if (payload.events[0].source.type.equals("room")){
                        // leaveGR(payload.events[0].source.roomId, "room");
                    // }
                // }

            // }
        // }
         
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    private void getMessageData(String message, String targetID) throws IOException{
        if (message!=null){
            pushMessage(targetID, message);
        }
    }

    private void replyToUser(String rToken, String messageToUser){
        TextMessage textMessage = new TextMessage(messageToUser);
        ReplyMessage replyMessage = new ReplyMessage(rToken, textMessage);
        try {
            Response<BotApiResponse> response = LineMessagingServiceBuilder
                .create(lChannelAccessToken)
                .build()
                .replyMessage(replyMessage)
                .execute();
            System.out.println("Reply Message: " + response.code() + " " + response.message());
        } catch (IOException e) {
            System.out.println("Exception is raised ");
            e.printStackTrace();
        }
    }
	
    private void sendButtonTempalte(TemplateMessage message, String to){
        PushMessage pushMessage = new PushMessage(to,message);
        try {
            Response<BotApiResponse> response = LineMessagingServiceBuilder
            .create(lChannelAccessToken)
            .build()
            .pushMessage(pushMessage)
            .execute();
            System.out.println(response.code() + " " + response.message());
        } catch (IOException e) {
            System.out.println("Exception is raised ");
            e.printStackTrace();
        }
    }
	
    private void pushMessage(String sourceId, String txt){
        TextMessage textMessage = new TextMessage(txt);
        PushMessage pushMessage = new PushMessage(sourceId,textMessage);
        try {
            Response<BotApiResponse> response = LineMessagingServiceBuilder
            .create(lChannelAccessToken)
            .build()
            .pushMessage(pushMessage)
            .execute();
            System.out.println(response.code() + " " + response.message());
        } catch (IOException e) {
            System.out.println("Exception is raised ");
            e.printStackTrace();
        }
    }

    private void leaveGR(String id, String type){
        try {
            if (type.equals("group")){
                Response<BotApiResponse> response = LineMessagingServiceBuilder
                    .create(lChannelAccessToken)
                    .build()
                    .leaveGroup(id)
                    .execute();
                System.out.println(response.code() + " " + response.message());
            } else if (type.equals("room")){
                Response<BotApiResponse> response = LineMessagingServiceBuilder
                    .create(lChannelAccessToken)
                    .build()
                    .leaveRoom(id)
                    .execute();
                System.out.println(response.code() + " " + response.message());
            }
        } catch (IOException e) {
            System.out.println("Exception is raised ");
            e.printStackTrace();
        }
    }
}
