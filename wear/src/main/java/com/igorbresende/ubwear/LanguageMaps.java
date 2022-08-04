package com.igorbresende.ubwear;

import java.util.HashMap;
import java.util.Map;

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
        map.put("initialMessage", "Abra o App UbWear no seu smartphone para configurar o relógio");
        map.put("deviceConnected", "Aparelho conectado!\nFaça login no UbWear do celular para configurar o relógio");
        map.put("estimatedTimeMessage", "Tempo estimado do motorista mais próximo:");
        map.put("noGpsMessage", "Peça um Uber pelo app e acompanhe a corrida também por aqui.");
        map.put("driverOnTheWay", "Motorista a caminho!");
        map.put("waitingForDriver", "Aguarde até que um motorista aceite a corrida...");
        map.put("estimatedTripTime", "Viagem iniciada!\nTempo estimado de chegada:");
        map.put("requestingUber", "Pedindo Uber!");
        map.put("errorMessage", "Algo deu errado. Tente novamente!");

        return map;
    }

    private Map<String, String> createMapEn() {
        Map<String, String> map = new HashMap<>();
        map.put("initialMessage", "Open UbWear App on your smartphone to setup your watch");
        map.put("deviceConnected", "Mobile device is connected!\nLog in on the mobile UbWear app to setup your watch.");
        map.put("estimatedTimeMessage", "Estimated time of nearest driver:");
        map.put("noGpsMessage", "Request an Uber through the app and follow the ride here too.");
        map.put("driverOnTheWay", "Driver on the way!");
        map.put("waitingForDriver", "Wait for a driver to accept the ride...");
        map.put("estimatedTripTime", "On trip!\nEstimated time of arrival:");
        map.put("requestingUber", "Requesting Uber!");
        map.put("errorMessage", "something went wrong. Please try again!");

        return map;
    }

    public Map<String, String> getMap(String language) {
        return supportedLanguages.getOrDefault(language, supportedLanguages.get("en"));
    }
}