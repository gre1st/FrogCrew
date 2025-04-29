package edu.tcu.cs.frogcrew.schedule;

import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.converter.GameDtoToGameConverter;
import edu.tcu.cs.frogcrew.game.converter.GameToGameDtoConverter;
import edu.tcu.cs.frogcrew.game.dto.GameDto;
import edu.tcu.cs.frogcrew.schedule.converter.ScheduleDtoToScheduleConverter;
import edu.tcu.cs.frogcrew.schedule.converter.ScheduleToScheduleDtoConverter;
import edu.tcu.cs.frogcrew.schedule.dto.ScheduleDto;
import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/gameSchedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleToScheduleDtoConverter scheduleToScheduleDtoConverter;
    private final ScheduleDtoToScheduleConverter scheduleDtoToScheduleConverter;
    private final GameDtoToGameConverter gameDtoToGameConverter;
    private final GameToGameDtoConverter gameToGameDtoConverter;

    public ScheduleController(ScheduleService scheduleService, ScheduleToScheduleDtoConverter scheduleToScheduleDtoConverter, ScheduleDtoToScheduleConverter scheduleDtoToScheduleConverter, GameDtoToGameConverter gameDtoToGameConverter, GameToGameDtoConverter gameToGameDtoConverter) {
        this.scheduleService = scheduleService;
        this.scheduleToScheduleDtoConverter = scheduleToScheduleDtoConverter;
        this.scheduleDtoToScheduleConverter = scheduleDtoToScheduleConverter;
        this.gameDtoToGameConverter = gameDtoToGameConverter;
        this.gameToGameDtoConverter = gameToGameDtoConverter;
    }

    @PostMapping("/{scheduleId}/games")
    public Result addGameToSchedule(@PathVariable Integer scheduleId,@Valid @RequestBody GameDto gameDto) {
        Game game = this.gameDtoToGameConverter.convert(gameDto);
        Game savedGame = this.scheduleService.addGameToSchedule(scheduleId, game);
        GameDto savedGameDto = gameToGameDtoConverter.convert(savedGame);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedGameDto);
    }

    @PostMapping
    public Result addSchedule(@Valid @RequestBody ScheduleDto scheduleDto) {
        Schedule schedule = this.scheduleDtoToScheduleConverter.convert(scheduleDto);
        Schedule savedSchedule = this.scheduleService.save(schedule);
        ScheduleDto savedScheduleDto = this.scheduleToScheduleDtoConverter.convert(savedSchedule);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedScheduleDto);
    }

    @GetMapping("/{scheduleId}")
    public Result findScheduleById(@PathVariable Integer scheduleId) {
        Schedule foundSchedule = this.scheduleService.findById(scheduleId);
        ScheduleDto foundScheduleDto = this.scheduleToScheduleDtoConverter.convert(foundSchedule);
        return new Result(true, StatusCode.SUCCESS, "Find Success", foundScheduleDto);
    }

    @PutMapping("/{scheduleId}")
    public Result updateSchedule(@PathVariable Integer scheduleId, @Valid @RequestBody ScheduleDto scheduleDto) {
        Schedule foundSchedule = this.scheduleDtoToScheduleConverter.convert(scheduleDto);
        Schedule updatedSchedule = this.scheduleService.update(scheduleId, foundSchedule);
        ScheduleDto updatedScheduleDto = this.scheduleToScheduleDtoConverter.convert(updatedSchedule);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedScheduleDto);
    }

    @GetMapping("/season/{season}")
    public Result findSchedulesBySeason(@PathVariable String season) {
        List<Schedule> foundSchedules = this.scheduleService.findBySeason(season);
        List<ScheduleDto> foundScheduleDtos = foundSchedules.stream()
                .map(scheduleToScheduleDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find Success", foundScheduleDtos);
    }

    @PutMapping("/publish/{scheduleId}")
    public Result publishSchedule(@PathVariable Integer scheduleId) {
        this.scheduleService.publishSchedule(scheduleId);
        return new Result(true, StatusCode.SUCCESS, "Publish Success", true);
    }

    @GetMapping("/sports")
    public Result findAllSports() {
        List<String> sports = this.scheduleService.getAllSports();
        return new Result(true, StatusCode.SUCCESS, "Find Success", sports);
    }


}
