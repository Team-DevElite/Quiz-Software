package com.DevElite.Quiz.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.DevElite.Quiz.domain.AnswerOption;
import com.DevElite.Quiz.domain.AnswerOptionRepository;
import com.DevElite.Quiz.domain.Question;
import com.DevElite.Quiz.domain.QuestionRepository;

/**
 * Service layer handling business logic and transactions for {@link AnswerOption} entities.
 */
@Service
public class AnswerOptionService {

    private final AnswerOptionRepository answerOptionRepository;
    private final QuestionRepository questionRepository;

    /**
     * Constructor injection for repository dependencies.
     * 
     * @param answerOptionRepository Spring Data repository for answer options
     * @param questionRepository Spring Data repository for questions
     */
    public AnswerOptionService(AnswerOptionRepository answerOptionRepository,
                               QuestionRepository questionRepository) {
        this.answerOptionRepository = answerOptionRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * Retrieves all answer options belonging to a specific question.
     * 
     * @param questionId The ID of the parent question
     * @return List of associated AnswerOption records
     */
    public List<AnswerOption> getOptionsByQuestionId(Long questionId) {
        return answerOptionRepository.findByQuestionId(questionId);
    }

    /**
     * Retrieves a Question entity by ID, or throws an IllegalArgumentException if not found.
     * 
     * @param questionId The ID of the question
     * @return The Question entity
     */
    public Question getQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + questionId));
    }

    /**
     * Retrieves an AnswerOption entity by ID, or throws an IllegalArgumentException if not found.
     * 
     * @param id The ID of the answer option
     * @return The AnswerOption entity
     */
    public AnswerOption getOptionById(Long id) {
        return answerOptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Option not found with id: " + id));
    }

    /**
     * Adds and persists a new AnswerOption attached to a parent Question.
     * 
     * @param questionId ID of the parent question
     * @param option The new AnswerOption to persist
     * @return The saved AnswerOption entity
     */
    @Transactional
    public AnswerOption addOption(Long questionId, AnswerOption option) {
        Question question = getQuestionById(questionId);
        option.setQuestion(question);
        if (option.getIsCorrect() == null) {
            option.setIsCorrect(false);
        }
        return answerOptionRepository.save(option);
    }

    /**
     * Updates an existing AnswerOption's text and correctness flag.
     * 
     * @param id ID of the answer option to update
     * @param updatedOption The incoming option values
     * @return The updated and saved AnswerOption entity
     */
    @Transactional
    public AnswerOption updateOption(Long id, AnswerOption updatedOption) {
        AnswerOption existing = getOptionById(id);
        existing.setOptionText(updatedOption.getOptionText());
        existing.setIsCorrect(updatedOption.getIsCorrect() != null ? updatedOption.getIsCorrect() : false);
        return answerOptionRepository.save(existing);
    }

    /**
     * Deletes an AnswerOption by its ID.
     * 
     * @param id ID of the answer option to delete
     */
    @Transactional
    public void deleteOption(Long id) {
        answerOptionRepository.deleteById(id);
    }
}
