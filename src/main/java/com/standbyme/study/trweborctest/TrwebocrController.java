package com.standbyme.study.trweborctest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("trwebocr")
public class TrwebocrController {


    @RequestMapping("fileUpload")
    public String fileUpLoad(MultipartFile multipartFile,Integer compress) throws IOException {


        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType("multipart/form-data");
        httpHeaders.setContentType(mediaType);

        File file = File.createTempFile("test", "jpg");
        multipartFile.transferTo(file);
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        LinkedMultiValueMap<String, Object> form
                = new LinkedMultiValueMap<>();

        form.add("file",fileSystemResource);
        form.add("filename","test.jpg");
        form.add("compress", compress);

        HttpEntity<LinkedMultiValueMap<String, Object>> linkedMultiValueMapHttpEntity = new HttpEntity<>(form, httpHeaders);

        String responseEntity = restTemplate.postForObject("http://192.168.2.135:8089/api/tr-run/", linkedMultiValueMapHttpEntity, String.class);


        List<String> strings = responseBodyToString(responseEntity);

        System.out.println(strings);

        return strings.toString();

    }

    public List<String> responseBodyToString(String response) throws JsonProcessingException {

        JsonMapper jsonMapper = new JsonMapper();
        JsonNode jsonNode = jsonMapper.readTree(response);

        JsonNode data = jsonNode.get("data");

        JsonNode raw_out = data.get("raw_out");

        List<String> str = new ArrayList<>();
        for (JsonNode node : raw_out) {
            System.out.println(node.toString());
            String s = node.get(1).toString();
            str.add(s);
        }

        return str;
    }






}
