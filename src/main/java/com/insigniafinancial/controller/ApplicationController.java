package com.insigniafinancial.controller;

import com.insigniafinancial.component.Command;
import com.insigniafinancial.component.Direction;
import com.insigniafinancial.component.Robot;
import com.insigniafinancial.component.Rotation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
public class ApplicationController {
    private final String REGEX_COMMAND_PLACE = "^PLACE\\s+([0-5]{1})\\s*,\\s*([0-5]{1})\\s*,\\s*("
            .concat(Direction.NORTH.name()).concat("|").concat(Direction.SOUTH.name()).concat("|")
            .concat(Direction.EAST.name()).concat("|").concat(Direction.WEST.name()).concat(")$");
    private  final String REGEX_COMMAND_ROBOT = "^ROBOT\\s+(\\d+)$";
    private final String MESSAGE_INVALID_COMMAND = "  command is ignored since it is invalid";
    private final String MESSAGE_NO_ACTIVE_ROBOT = " command ignored since no active robot";
    private final String MESSAGE_ROBOT_NOT_ACTIVATED = " is not activated as it does not exist";
    private Integer robotCount;
    private Robot activeRobot;
    private final Map<Integer, Robot> robots;

    public ApplicationController() {
        log.debug("ApplicationController starts");
        robotCount = 0;
        robots = new HashMap<>();
        log.debug("ApplicationController ends");
    }

    public String processCommand(String line) {
        log.debug("processCommand starts");
        log.debug("line={}", line);
        String message = null;
        if (line.startsWith(Command.PLACE.name())) {
            message = processCommandPlace(line);
        } else if (line.startsWith(Command.ROBOT.name())) {
            message = setActiveRobot(line);
        } else if (line.equals(Command.MOVE.name())) {
            message = processCommandMove();
        } else if (line.equals(Command.REPORT.name())) {
            message = processCommandReport();
        } else if (EnumUtils.isValidEnum(Direction.class, line)) {
            message = processDirection(line);
        } else if (EnumUtils.isValidEnum(Rotation.class, line)) {
            message = processRotation(line);
        } else {
            message = line.concat(MESSAGE_INVALID_COMMAND);
        }
        log.debug("processCommand ends");
        return message;
    }

    private String processCommandPlace(String line) {
        log.debug("processCommandPlace starts");
        String message = null;
        Matcher matcher = Pattern.compile(REGEX_COMMAND_PLACE).matcher(line);
        if (matcher.find()) {
             robotCount += 1;
             Robot robot = Robot.builder()
                    .id(robotCount)
                    .name("Robot ".concat(String.valueOf(robotCount)))
                    .positionX(Integer.parseInt(matcher.group(1)))
                    .positionY(Integer.parseInt(matcher.group(2)))
                    .direction(Direction.valueOf(matcher.group(3))).build().init();
             log.debug("robot={}", robot);
             robots.put(robot.getId(), robot);
             log.debug("robots={}", robots.toString());
             activeRobot = robot;
             log.debug("activeRobot={}", activeRobot);
        } else {
            message = line.concat(MESSAGE_INVALID_COMMAND);
            log.warn(message);
        }
        log.debug("processCommandPlace ends");
        return  message;
    }

    private String setActiveRobot(String line) {
        log.debug("setActiveRobot starts");
        String message = null;
        Matcher matcher = Pattern.compile(REGEX_COMMAND_ROBOT).matcher(line);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            log.debug("id={}", id);
            if (robots.containsKey(id)) {
                activeRobot = robots.get(id);
                log.debug("activeRobot={}", activeRobot);
            } else {
                message = line.concat(MESSAGE_ROBOT_NOT_ACTIVATED);
                log.warn(message);
            }
        } else {
            message = line.concat(MESSAGE_INVALID_COMMAND);
            log.warn(message);
        }
        log.debug("setActiveRobot ends");
        return message;
    }

    private String processCommandMove() {
        log.debug("processCommandMove starts");
        String message = null;
        if (activeRobot != null) {
            activeRobot.move();
        } else {
            message = Command.MOVE.name().concat(MESSAGE_NO_ACTIVE_ROBOT);
            log.warn(message);
        }
        log.debug("processCommandMove ends");
        return message;
    }

    private String processCommandReport() {
        log.debug("processCommandReport starts");
        String message;
        if (activeRobot != null) {
            message = activeRobot.getName().concat(": ")
                    .concat(String.valueOf(activeRobot.getPositionX())).concat(",")
                    .concat(String.valueOf(activeRobot.getPositionY())).concat(",")
                    .concat(activeRobot.getDirection().name());
            log.debug("message={}", message);
        } else {
            message = Command.REPORT.name().concat(MESSAGE_NO_ACTIVE_ROBOT);
            log.warn(message);
        }
        log.debug("processCommandReport ends");
        return message;
    }

    private String processDirection(String line) {
        log.debug("processDirectionChange starts");
        String message = null;
        if (activeRobot != null) {
            activeRobot.setDirection(Direction.valueOf(line));
        } else {
            message = line.concat(MESSAGE_NO_ACTIVE_ROBOT);
            log.warn(message);
        }
        log.debug("processDirectionChange ends");
        return message;
    }

    private String processRotation(String line) {
        log.debug("processRotation starts");
        String message = null;
        if (activeRobot != null) {
            activeRobot.setDirection(Rotation.valueOf(line));
        } else {
            message = line.concat(MESSAGE_NO_ACTIVE_ROBOT);
            log.warn(message);
        }
        log.debug("processRotation ends");
        return message;
    }
}