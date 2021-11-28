package com.example.APS_SD.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping("/abastecimento")
public class AbastecimentoController {

    private static String token;


    @GetMapping("/racao")
    public void abreFechadura(){
        // Run a java app in a separate system process
        Process proc = null;
        try {
            System.out.println("chamei o fechadura.py");
            proc = Runtime.getRuntime().exec("python3 fechadura.py");
          //  proc = Runtime.getRuntime().exec("java -jar C:\\Users\\evert\\Desktop\\TCC\\teste.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }
// Then retreive the process output
        InputStream in = proc.getInputStream();
        InputStream err = proc.getErrorStream();
    }

    @GetMapping("/fecha")
    public void fechaFechadura(){
        // Run a java app in a separate system process
        Process proc = null;
        try {
            System.out.println("chamei o fechafechadura.py");
            proc = Runtime.getRuntime().exec("python3 fechafechadura.py");
            //  proc = Runtime.getRuntime().exec("java -jar C:\\Users\\evert\\Desktop\\TCC\\teste.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }
// Then retreive the process output
        InputStream in = proc.getInputStream();
        InputStream err = proc.getErrorStream();
    }

}
