package com.insigniafinancial.component;

import com.insigniafinancial.model.Tabletop;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Builder
@Data
public class Robot {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private Integer positionX;
    @NonNull
    private Integer positionY;
    @NonNull
    private Direction direction;
    private Axis activeAxis;
    private Integer increment;
    public Robot init() {
        log.debug("init starts");
        setDirection(direction);
        log.debug("init ends");
        return this;
    }

    public void setDirection(Rotation rotation) {
        log.debug("setDirection starts");
        log.debug("rotation={}", rotation);
        setDirection(Tabletop.getInstance().getDirection(direction, rotation));
        log.debug("setDirection ends");
    }

    public void setDirection(Direction direction) {
        log.debug("setDirection starts");
        log.debug("direction={}", direction);
        List<Object> list = Tabletop.getInstance().getActiveAxisAndIncrement(direction);
        if (list == null) {
            log.error("invalid facing {}", direction);
            return;
        }
        this.direction = direction;
        activeAxis = (Axis) list.get(0);
        increment = (Integer) list.get(1);
        log.debug("direction={}", direction);
        log.debug("activeAxis={}", activeAxis);
        log.debug("increment={}", increment);
        log.debug("setDirection ends");
    }

    public void move() {
        log.debug("setDirection starts");
        switch (activeAxis) {
            case X:
                if (Tabletop.getInstance().isSafeMove(Axis.X, positionX + increment)) {
                    positionX += increment;
                    log.debug("positionX={}", positionX);
                }
                break;
            case Y:
                if (Tabletop.getInstance().isSafeMove(Axis.Y, positionY + increment)) {
                    positionY += increment;
                    log.debug("positionY={}", positionY);
                }
                break;
            default:
                log.warn("invalid Axis {}", activeAxis);
                break;
        }
        log.debug("setDirection ends");
    }
}