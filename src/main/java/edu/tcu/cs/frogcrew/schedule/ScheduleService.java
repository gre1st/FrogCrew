package edu.tcu.cs.frogcrew.schedule;

import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.GameRepository;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GameRepository gameRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, GameRepository gameRepository) {
        this.scheduleRepository = scheduleRepository;
        this.gameRepository = gameRepository;
    }

    public Schedule findById(Integer scheduleId) {
        return this.scheduleRepository.findById(scheduleId).orElseThrow(() -> new ObjectNotFoundException("schedule", scheduleId));
    }

    public List<Schedule> findBySeason(String season) {
        return this.scheduleRepository.findSchedulesBySeason(season);
    }

    public Game addGameToSchedule(Integer scheduleId, Game game) {
        Schedule schedule = this.scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ObjectNotFoundException("schedule", scheduleId));
        game.setSchedule(schedule);
        schedule.getGames().add(game);
        return gameRepository.save(game);
    }

    public Schedule save(Schedule schedule) {
        return this.scheduleRepository.save(schedule);
    }

    public Schedule update(Integer scheduleId, Schedule update) {
        return this.scheduleRepository.findById(scheduleId)
                .map(oldSchedule -> {
                    oldSchedule.setSport(update.getSport());
                    oldSchedule.setSeason(update.getSeason());
                    oldSchedule.setGames(update.getGames());
                    return this.scheduleRepository.save(oldSchedule);
                }).orElseThrow(() -> new ObjectNotFoundException("schedule", scheduleId));
    }

    public void publishSchedule(Integer scheduleId) {
        Schedule schedule = this.scheduleRepository.findById(scheduleId).orElseThrow(() -> new ObjectNotFoundException("schedule", scheduleId));
        schedule.setPublished(true);
        this.scheduleRepository.save(schedule);
    }

    public List<String> getAllSports() {
        return this.scheduleRepository.findDistinctSports();
    }


}
