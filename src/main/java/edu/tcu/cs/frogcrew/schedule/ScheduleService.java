package edu.tcu.cs.frogcrew.schedule;

import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.GameRepository;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GameRepository gameRepository;

    public ScheduleService(ScheduleRepository scheduleRepository,
                           GameRepository gameRepository) {
        this.scheduleRepository = scheduleRepository;
        this.gameRepository     = gameRepository;
    }

    public Schedule findById(Integer scheduleId) {
        return scheduleRepository.findById(scheduleId)
                 .orElseThrow(() -> new ObjectNotFoundException("schedule", scheduleId));
    }

    public List<Schedule> findBySeason(String season) {
        return scheduleRepository.findSchedulesBySeason(season);
    }

    public Game addGameToSchedule(Integer scheduleId, Game game) {
        Schedule sched = scheduleRepository.findById(scheduleId)
                            .orElseThrow(() -> new ObjectNotFoundException("schedule", scheduleId));
        game.setSchedule(sched);
        sched.getGames().add(game);
        return gameRepository.save(game);
    }

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule update(Integer scheduleId, Schedule update) {
        return scheduleRepository.findById(scheduleId)
            .map(old -> {
                old.setSport(update.getSport());
                old.setSeason(update.getSeason());
                old.setGames(update.getGames());
                return scheduleRepository.save(old);
            })
            .orElseThrow(() -> new ObjectNotFoundException("schedule", scheduleId));
    }

    public List<String> getAllSports() {
        return scheduleRepository.findDistinctSports();
    }

    /** Return every Game entity in the DB */
    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }
}
