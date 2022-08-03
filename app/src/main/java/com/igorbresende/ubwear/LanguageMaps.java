package com.igorbresende.ubwear;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LanguageMaps {

    Map<String, Map<String, String>> supportedLanguages = new HashMap<>();

    public LanguageMaps() {
        createSupportedLanguagesMap();
    }

    private void createSupportedLanguagesMap() {
        supportedLanguages.put("pt", createMapPt());
        supportedLanguages.put("en", createMapEn());
    }

    private Map<String, String> createMapPt() {
        Map<String, String> map = new HashMap<>();
        map.put("tutorialUrl", "https://docs.google.com/document/d/1Zt9f8IZidLdL3nNSMKF7_1Pf8uCy4Tv-bjdB-xesT8g/edit?usp=sharing");
        map.put("cookiesIncorrect", "sid e/ou csid estão errados. Verifique se copiou todos os dados e tente novamente!");
        map.put("cookiesEmpty", "Mensagem vazia. Insira os dados novamente para continuar");
        map.put("devicePaired", "Relógio pareado com sucesso. Clique no botão \"Enviar dados para relógio\" para terminar a configuração.");
        map.put("checkConnectedDevicesButton", "Procurar aparelhos conectados");
        map.put("checkConnectedDevicesButtonBefore", "Estou logado!");
        map.put("appTitle", "Faça login na sua conta da Uber");
        map.put("successfulConfigurationToast", "Sucesso!!!\nSeu relógio está configurado!");
        map.put("openWearApp", "Abra o app UbWear no relógio e tente novamente");
        map.put("noWearableFound", "Nenhum relógio pareado. Conecte o relógio ao smartphone e tente novamente.");
        map.put("sendMessageButton", "Enviar");
        map.put("uberUrl", "https://m.uber.com/");
        map.put("notLoggedIn", "Faça login na janela acima e tente novamente!");

        return map;
    }

    private Map<String, String> createMapEn() {
        Map<String, String> map = new HashMap<>();
        map.put("tutorialUrl", "https://docs.google.com/document/d/1wzEY-3krffwy8aEHr3jRhHmtFp5KOliOfDtfQ7eTC6E/edit?usp=sharing");
        map.put("cookiesIncorrect", "sid and/or csid are wrong. Make sure you copied all the data and try again!");
        map.put("cookiesEmpty", "Message content is empty. Please enter some message and proceed");
        map.put("devicePaired", "Wearable device paired and app is open. Tap the \"Send Message to Wearable\" button to send the message to your wearable device.");
        map.put("checkConnectedDevicesButton", "Check for connected wearables");
        map.put("checkConnectedDevicesButtonBefore", "I'm logged in!");
        map.put("appTitle", "Log in to your Uber account");
        map.put("successfulConfigurationToast", "Success!!!\nYour watch is configured!");
        map.put("openWearApp", "Open the UbWear app on the watch and try again");
        map.put("noWearableFound", "No wearable device paired. Pair a wearable device to your phone and try again.");
        map.put("sendMessageButton", "Send");
        map.put("uberUrl", "https://m.uber.com/");
        map.put("notLoggedIn", "Login in the window above and try again!");

        return map;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getLanguage(String language, String key) {
        Map<String, String> mapOfCorrectLanguage = supportedLanguages.getOrDefault(language, supportedLanguages.get("en"));

        return Objects.requireNonNull(mapOfCorrectLanguage).getOrDefault(key, "translation not found");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, String> getMap(String language) {
        return supportedLanguages.getOrDefault(language, supportedLanguages.get("en"));
    }

}
