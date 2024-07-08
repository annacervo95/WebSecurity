package com.example.progettoSS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.progettoSS.repository.UserR;
import com.example.progettoSS.entity.Booking;
import java.io.IOException;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.Base64;
import org.json.JSONObject;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import com.example.progettoSS.repository.BookingR;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import com.example.progettoSS.entity.User;
import jakarta.servlet.http.Cookie;

import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest; //Nb qualsiasi cosa javax.servelet usa jakarta
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
@Controller
public class BookingController {

    @Autowired
    private BookingR bookingr;

    @Autowired
    private UserR userr;

@RequestMapping(value = "/user/addbooking", method = RequestMethod.POST)
@ResponseBody
public String addBooking(@RequestBody String received_json_data, HttpSession session) throws IOException{

    String value = (String) session.getAttribute("id");
    System.out.println(value);
    JSONObject jsonObject = new JSONObject(received_json_data);
    bookingr.addBooking( value,jsonObject.getString("namePlant"), jsonObject.getString("quantity"));
    return "ok";
       
}


@RequestMapping("/admin/getbookings")
public String getBookings(Model model) {
    List<Booking> bookings = bookingr.getAllBookings();
    List<User> users = new ArrayList<>();
    
    for (Booking booking : bookings) {
        User user = userr.findUserById(booking.getIdUser());
        users.add(user);
    }
    
    model.addAttribute("bookings", bookings);
    model.addAttribute("users", users);
    System.out.println(bookings);
    return "bookingAdmin";
}


@RequestMapping("/user/getbookings")
public String getUserBooking(Model model, HttpSession session)  {
     String value = (String) session.getAttribute("id");
     System.out.println("value"+value);
     List<Booking> bookings = bookingr.getUserBookings(value);
     model.addAttribute("bookingsuser", bookings);
    return "bookingUser";
    
}

@RequestMapping("/admin/deletebooking/{id}")

public ResponseEntity<String> deleteBooking(@PathVariable("id") String id) {
 
     
        int rowsAffected = bookingr.deleteBooking(id);
        boolean success = (rowsAffected > 0);
        String message = success ? "Eliminazione riuscita. Numero di righe eliminate: " + rowsAffected : "Nessuna riga eliminata.";

        // Creazione dell'oggetto JSON per la risposta
        JSONObject response = new JSONObject();
        response.put("success", success);
        response.put("message", message);
 // Restituisci la risposta come JSON con lo status appropriato
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
 
}

@PostMapping("/user/aggiungiprodotto")
public void aggiungiCookieCarrello(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> requestBody)throws JsonProcessingException {


    ArrayList<String> namePlants = (ArrayList<String>) requestBody.get("namePlant");
    ArrayList<String> quantita = (ArrayList<String>) requestBody.get("quantita");
    ArrayList<String> prezzi = (ArrayList<String>) requestBody.get("prices");

    System.out.println("sono nella post");
    
    // Creazione dell'oggetto JSON per rappresentare le informazioni del prodotto
    JSONObject prodottoJSON = new JSONObject();
    prodottoJSON.put("namePlant", namePlants);
    prodottoJSON.put("quantita", quantita);
    prodottoJSON.put("prezzi", prezzi);

    //SPOTBUGS
    // Codifica del valore JSON in Base64
    //String valoreCodificato = Base64.getEncoder().encodeToString(prodottoJSON.toString().getBytes()); //non sicuro: la stringa viene convertita in array di byte utilizzando l'encoding predefinito del sistema, che potrebbe variare in base all'ambiente di esecuzione. Questo può portare a problemi di compatibilità o risultati imprevisti se l'encoding predefinito non corrisponde all'encoding effettivo dei dati.
    
    //Base64.getEncoder().encodeToString() è un metodo fornito dalla classe Base64 del pacchetto Java java.util che converte un array di byte in una stringa codificata in Base64.
    //Il metodo encodeToString() prende in input un array di byte e restituisce una stringa che rappresenta l'array di byte codificato in Base64. 
    String valoreCodificato = Base64.getEncoder().encodeToString(prodottoJSON.toString().getBytes(StandardCharsets.UTF_8)); //sicuro: specificare esplicitamente l'encoding UTF-8 quando si converte la stringa in array di byte. 
    
    // Creazione del cookie del carrello
    Cookie carrelloCookie = new Cookie("carrello", valoreCodificato);
    
    // Impostazione di altre proprietà del cookie, ad esempio il percorso e la durata
    carrelloCookie.setPath("/");
    carrelloCookie.setMaxAge(3600); // Durata in secondi
    
    // Aggiunta del cookie alla risposta HTTP
    response.addCookie(carrelloCookie);
}




}
