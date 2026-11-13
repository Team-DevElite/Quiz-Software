import type { Quiz } from '../types.ts';

interface QuizCardProps {
  quiz: Quiz;
  onTakeQuiz?: (quiz: Quiz) => void;
}

export function QuizCard({ quiz, onTakeQuiz }: QuizCardProps) {
  const course = quiz.courseCode || quiz.course_code || 'General';

  const handleTakeQuiz = () => {
    if (onTakeQuiz) {
      onTakeQuiz(quiz);
    } else {
      alert(`Starting quiz: "${quiz.name}" (Quiz player feature is under development).`);
    }
  };

  return (
    <article className="quiz-card" data-testid={`quiz-card-${quiz.id}`}>
      <div className="quiz-card-header">
        <span className="course-badge">{course}</span>
        <span className="status-badge published">Published</span>
      </div>

      <h3 className="quiz-title">{quiz.name}</h3>
      <p className="quiz-description">{quiz.description}</p>

      <div className="quiz-card-footer">
        {quiz.createdAt && (
          <span className="quiz-date">
            Added on {quiz.createdAt}
          </span>
        )}
        <button
          type="button"
          className="btn-take-quiz"
          onClick={handleTakeQuiz}
          aria-label={`Take quiz ${quiz.name}`}
        >
          Take Quiz &rarr;
        </button>
      </div>
    </article>
  );
}

export default QuizCard;

