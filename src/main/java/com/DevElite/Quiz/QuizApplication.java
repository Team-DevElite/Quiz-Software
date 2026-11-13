package com.DevElite.Quiz;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.DevElite.Quiz.domain.AnswerOption;
import com.DevElite.Quiz.domain.Question;
import com.DevElite.Quiz.domain.Quiz;
import com.DevElite.Quiz.domain.QuizRepository;

@SpringBootApplication
public class QuizApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizApplication.class, args);
	}

	/**
	 * Seeds mock demo data into the database if the repository is empty.
	 *
	 * @param quizRepository Repository used for database persistence
	 * @return CommandLineRunner callback executed on application startup
	 */
	@Bean
	public CommandLineRunner demoData(QuizRepository quizRepository) {
		return args -> {
			if (quizRepository.count() == 0) {
				// Seed Quiz 1: The Scrum Framework (Published)
				Quiz scrumQuiz = new Quiz(
						"The Scrum Framework",
						"Learn the core roles, events, and artifacts of the Scrum agile framework.",
						"SOF005AS3AE",
						true);

				Question q1 = new Question("What is the primary purpose of the Sprint Retrospective?", "Easy", scrumQuiz);
				q1.addAnswerOption(new AnswerOption("To inspect how the last Sprint went and identify process improvements", true, q1));
				q1.addAnswerOption(new AnswerOption("To demonstrate the product increment to stakeholders", false, q1));
				q1.addAnswerOption(new AnswerOption("To assign individual tasks to team members", false, q1));
				scrumQuiz.addQuestion(q1);

				Question q2 = new Question("Who is responsible for maximizing the value of the product in Scrum?", "Normal", scrumQuiz);
				q2.addAnswerOption(new AnswerOption("Product Owner", true, q2));
				q2.addAnswerOption(new AnswerOption("Scrum Master", false, q2));
				q2.addAnswerOption(new AnswerOption("Developers", false, q2));
				scrumQuiz.addQuestion(q2);

				quizRepository.save(scrumQuiz);

				// Seed Quiz 2: Java Fundamentals (Published)
				Quiz javaQuiz = new Quiz(
						"Java Fundamentals",
						"Essential core Java concepts including OOP, types, and JVM mechanics.",
						"CS101",
						true);

				Question jq1 = new Question("Which of the following is NOT a primitive type in Java?", "Easy", javaQuiz);
				jq1.addAnswerOption(new AnswerOption("String", true, jq1));
				jq1.addAnswerOption(new AnswerOption("int", false, jq1));
				jq1.addAnswerOption(new AnswerOption("boolean", false, jq1));
				jq1.addAnswerOption(new AnswerOption("double", false, jq1));
				javaQuiz.addQuestion(jq1);

				Question jq2 = new Question("What is the default value of an uninitialized boolean instance variable in Java?", "Normal", javaQuiz);
				jq2.addAnswerOption(new AnswerOption("false", true, jq2));
				jq2.addAnswerOption(new AnswerOption("true", false, jq2));
				jq2.addAnswerOption(new AnswerOption("null", false, jq2));
				javaQuiz.addQuestion(jq2);

				quizRepository.save(javaQuiz);

				// Seed Quiz 3: Cloud & DevOps (Draft / Unpublished)
				Quiz cloudQuiz = new Quiz(
						"Cloud & DevOps Architecture",
						"Containers, CI/CD pipelines, and cloud native principles.",
						"DO201",
						false);

				Question cq1 = new Question("What is the fundamental unit of deployment in Kubernetes?", "Normal", cloudQuiz);
				cq1.addAnswerOption(new AnswerOption("Pod", true, cq1));
				cq1.addAnswerOption(new AnswerOption("Container", false, cq1));
				cq1.addAnswerOption(new AnswerOption("Virtual Machine", false, cq1));
				cloudQuiz.addQuestion(cq1);

				quizRepository.save(cloudQuiz);
			}
		};
	}
}
