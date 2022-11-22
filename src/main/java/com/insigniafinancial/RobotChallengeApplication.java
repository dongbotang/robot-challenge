package com.insigniafinancial;
import com.insigniafinancial.controller.ApplicationController;
import com.insigniafinancial.component.Command;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class RobotChallengeApplication {
    public static void main(String[] args) {
        log.debug ("main starts()");
        System.out.println("***********************************");
        System.out.println("** Please place a Robot to start **");
        System.out.println("***********************************");
        ApplicationController controller = new ApplicationController();
        Scanner scanner = new Scanner(System.in);
        String line, message;
        while (scanner.hasNext()) {
            line = scanner.nextLine().toUpperCase().trim();
            if (line.equals(Command.EXIT.name())) {
                break;
            } else {
                message = controller.processCommand(line);
                if (message != null) {
                    log.debug(message);
                    System.out.println(message);
                }
            }
        }
        System.out.println("***********************************");
        System.out.println("**     See you next time ^_^     **");
        System.out.println("***********************************");
        log.debug("main ends()");
    }
}