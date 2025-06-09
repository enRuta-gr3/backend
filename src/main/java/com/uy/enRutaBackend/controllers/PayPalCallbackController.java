//package com.uy.enRutaBackend.controllers;
//
//import java.net.URI;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/paypal-callback")
//public class PayPalCallbackController {
//
//    @GetMapping
//    public ResponseEntity<Void> handleCallback(@RequestParam("token") String orderId, @RequestParam("PayerID") String payerId,
//                                               @RequestHeader(value = "User-Agent", required = false) String userAgent) {
//        System.out.println("Callback recibido - OrderID: " + orderId+ ", PayerID: " + payerId);
//        
//        String redirectUrl;
//        if (userAgent != null && (userAgent.contains("Mobile") || userAgent.contains("Android") || userAgent.contains("iPhone"))) {
//            redirectUrl = "myapp://pago-exitoso?orderId=" + orderId;
//        } else {
//            redirectUrl = "https://tuapp.com/web-pago-confirmado?orderId=" + orderId;
//        }
//
//        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl)).build();
//    }
//}