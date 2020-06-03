package com.phones.phones.service;

import com.phones.phones.dto.LineStatusDto;
import com.phones.phones.exception.line.LineNotExistException;
import com.phones.phones.exception.line.LineNumberAlreadyExistException;
import com.phones.phones.exception.user.UserNotExistException;
import com.phones.phones.model.Line;
import com.phones.phones.model.User;
import com.phones.phones.repository.LineRepository;
import com.phones.phones.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final UserRepository userRepository;

    @Autowired
    public LineService(final LineRepository lineRepository,
                       final UserRepository userRepository) {
        this.lineRepository = lineRepository;
        this.userRepository = userRepository;
    }


    public Line create(Line newLine) throws LineNumberAlreadyExistException {
        Optional<Line> line = lineRepository.findByNumber(newLine.getNumber());
        if (line.isPresent()) {
            throw new LineNumberAlreadyExistException();
        }
        return lineRepository.save(newLine);
    }

    public List<Line> findAll() {
        return lineRepository
                .findAll()
                .stream()
                .filter(Line::isEnabled)
                .collect(Collectors.toList());
    }

    public Line findById(Long id) throws LineNotExistException {
        Optional<Line> line = lineRepository.findById(id);
        if (line.isEmpty() || line.get().isDisabled()) {
            throw new LineNotExistException();
        }
        return line.get();
    }

    public List<Line> findByUserId(Long id) throws UserNotExistException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotExistException();
        }
        return lineRepository.findAllByUserId(id);
    }

    public Line updateLineStatusByIdLine(Long id,
                                         LineStatusDto lineStatus) throws LineNotExistException {
        Optional<Line> line = lineRepository.findById(id);
        if (line.isEmpty()) {
            throw new LineNotExistException();
        }
        line.get().setStatus(lineStatus.getStatus());
        return lineRepository.save(line.get());
    }

}
