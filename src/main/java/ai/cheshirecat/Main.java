package ai.cheshirecat;

import ai.cheshirecat.exceptions.WaitingResponseException;
import ai.cheshirecat.logic.CatClient;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        var client = new CatClient("localhost", 1865, "mike");

        Scanner keyboard = new Scanner(System.in);
        String userPrompt;

        try {

            client.connectBlocking();

            client.onMessage((message) -> {
                System.out.println("MSG: " + message);
            });

            while (true) {
                userPrompt = keyboard.nextLine();
                try {
                    client.send(userPrompt);
                } catch (WaitingResponseException e) {
                    System.out.println(e.getMessage());
                }
            }


        } catch (InterruptedException e) {
            client.close();
        } finally {
            client.close();
        }


    }
}