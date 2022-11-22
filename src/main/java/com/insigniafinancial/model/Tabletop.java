package com.insigniafinancial.model;

import com.insigniafinancial.component.Axis;
import com.insigniafinancial.component.Direction;
import com.insigniafinancial.component.Rotation;
import com.insigniafinancial.config.ConfigProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Tabletop {
    private static Tabletop instance = null;
    private final Map<String, Direction> directionRotationMapping;
    private final Map<Direction, List<Object>> directionActiveAxisIncrementMapping;

    public static Tabletop getInstance() {
        log.debug("getInstance starts");
        if (instance == null) {
            instance = new Tabletop();
            log.debug("instance={}", instance.toString());
        }
        log.debug("getInstance ends");
        return instance;
    }

    private Tabletop() {
        log.debug("Tabletop starts");
        directionRotationMapping = new HashMap<>();
        directionActiveAxisIncrementMapping = new HashMap<>();
        initDirectionRotationMappingData();
        initDirectionActiveAxisMappingData();
        log.debug("Tabletop ends");
    }

    public String calculateKey(Direction direction, Rotation rotation) {
        return direction.name().concat("_").concat(rotation.name());
    }

    public Direction getDirection(Direction direction, Rotation rotation) {
        return directionRotationMapping.get(calculateKey(direction, rotation));
    }

    public List<Object> getActiveAxisAndIncrement(Direction direction) {
        return directionActiveAxisIncrementMapping.get(direction);
    }

    public boolean isSafeMove(Axis axis, Integer value) {
        log.debug("isSafeMove starts");
        log.debug("axis={}", axis);
        log.debug("value={}", value);
        boolean result = false;
        switch (axis) {
            case X ->
                    result = value <= Integer.parseInt(ConfigProperties.getInstance().getProperty("tabletop.dimension.x"))
                            && value >= 0;
            case Y ->
                    result = value <= Integer.parseInt(ConfigProperties.getInstance().getProperty("tabletop.dimension.y"))
                            && value >= 0;
            default -> log.error("invalid axis {}", axis);
        }
        log.debug("result={}", result);
        log.debug("isSafeMove ends");
        return result;
    }

    private void initDirectionRotationMappingData() {
        log.debug("initDirectionRotationMappingData starts");
        directionRotationMapping.put(calculateKey(Direction.NORTH, Rotation.LEFT), Direction.WEST);
        directionRotationMapping.put(calculateKey(Direction.NORTH, Rotation.RIGHT), Direction.EAST);
        directionRotationMapping.put(calculateKey(Direction.SOUTH, Rotation.LEFT), Direction.EAST);
        directionRotationMapping.put(calculateKey(Direction.SOUTH, Rotation.RIGHT), Direction.WEST);
        directionRotationMapping.put(calculateKey(Direction.EAST, Rotation.LEFT), Direction.NORTH);
        directionRotationMapping.put(calculateKey(Direction.EAST, Rotation.RIGHT), Direction.SOUTH);
        directionRotationMapping.put(calculateKey(Direction.WEST, Rotation.LEFT), Direction.SOUTH);
        directionRotationMapping.put(calculateKey(Direction.WEST, Rotation.RIGHT), Direction.NORTH);
        log.debug("directionRotationMapping={}", directionRotationMapping.toString());
        log.debug("initDirectionRotationMappingData ends");
    }

    private void initDirectionActiveAxisMappingData() {
        log.debug("initDirectionActiveAxisMappingData starts");
        directionActiveAxisIncrementMapping.put(Direction.NORTH, Arrays.asList(Axis.Y, 1));
        directionActiveAxisIncrementMapping.put(Direction.SOUTH, Arrays.asList(Axis.Y, -1));
        directionActiveAxisIncrementMapping.put(Direction.EAST, Arrays.asList(Axis.X, 1));
        directionActiveAxisIncrementMapping.put(Direction.WEST, Arrays.asList(Axis.X, -1));
        log.debug("directionActiveAxisIncrementMapping={}", directionActiveAxisIncrementMapping.toString());
        log.debug("initDirectionActiveAxisMappingData ends");
    }
}