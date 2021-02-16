package com.tracker.portfolio.controller;

import com.tracker.portfolio.dto.PositionDTO;
import com.tracker.portfolio.entity.Position;
import com.tracker.portfolio.service.PositionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("position")
@AllArgsConstructor
@CrossOrigin("*")
public class PositionController {

    private final PositionService positionService;

    @GetMapping("/{positionId}")
    public @ResponseBody
    Position getPosition(@PathVariable int positionId) {
        return positionService.getPosition(positionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Position
    savePosition(@RequestBody PositionDTO positionDTO) {
        return positionService.save(positionDTO);
    }
}
