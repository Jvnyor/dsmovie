package com.Jvnyor.dsmovie.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Jvnyor.dsmovie.dto.MovieDTO;
import com.Jvnyor.dsmovie.dto.ScoreDTO;
import com.Jvnyor.dsmovie.entities.Movie;
import com.Jvnyor.dsmovie.entities.Score;
import com.Jvnyor.dsmovie.entities.User;
import com.Jvnyor.dsmovie.repositories.MovieRepository;
import com.Jvnyor.dsmovie.repositories.ScoreRepository;
import com.Jvnyor.dsmovie.repositories.UserRepository;

@Service
public class ScoreService {

	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ScoreRepository scoreRepository;
	
	@Transactional
	public MovieDTO saveScore(ScoreDTO dto) {
		User user = userRepository.findByEmail(dto.getEmail());
		if (user == null) {
			user = new User();
			user.setEmail(dto.getEmail());
			user = userRepository.saveAndFlush(user);
		}
		Movie movie = movieRepository.findById(dto.getMovieId()).get();
		
		Score score = new Score();
		score.setMovie(movie);
		score.setUser(user);
		score.setValue(dto.getScore());
		
		score = scoreRepository.saveAndFlush(score);
		
		double sum = 0.0;
		for (Score s : movie.getScores()) {
			sum = sum + s.getValue();
		}
		
		int quantity = movie.getScores().size();
		
		double avg = sum / quantity;
		
		movie.setScore(avg);
		movie.setCount(quantity);
		
		movie = movieRepository.saveAndFlush(movie);
		
		return new MovieDTO(movie);
	}
}
