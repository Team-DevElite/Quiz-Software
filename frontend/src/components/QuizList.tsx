import { useState, useMemo } from 'react';
import type { Quiz } from '../types.ts';
import { QuizCard } from './QuizCard.tsx';

interface QuizListProps {
  quizzes: Quiz[];
  onTakeQuiz?: (quiz: Quiz) => void;
}

export function QuizList({ quizzes, onTakeQuiz }: QuizListProps) {
  const [searchTerm, setSearchTerm] = useState('');

  const filteredQuizzes = useMemo(() => {
    const term = searchTerm.trim().toLowerCase();
    if (!term) return quizzes;

    return quizzes.filter((quiz) => {
      const nameMatch = quiz.name?.toLowerCase().includes(term);
      const descMatch = quiz.description?.toLowerCase().includes(term);
      const course = (quiz.courseCode || quiz.course_code || '').toLowerCase();
      const courseMatch = course.includes(term);
      return nameMatch || descMatch || courseMatch;
    });
  }, [quizzes, searchTerm]);

  return (
    <section className="quiz-list-section">
      <div className="quiz-list-toolbar">
        <div className="search-box">
          <input
            type="search"
            placeholder="Search by quiz title, course code, or topic..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
            aria-label="Search quizzes"
          />
          {searchTerm && (
            <button
              type="button"
              className="btn-clear-search"
              onClick={() => setSearchTerm('')}
              aria-label="Clear search"
            >
              &times;
            </button>
          )}
        </div>
        <p className="quiz-count">
          Showing <strong>{filteredQuizzes.length}</strong> of <strong>{quizzes.length}</strong> published {quizzes.length === 1 ? 'quiz' : 'quizzes'}
        </p>
      </div>

      {filteredQuizzes.length === 0 ? (
        <div className="no-results-state">
          <h3>No matching quizzes</h3>
          <p>No published quizzes matched &ldquo;{searchTerm}&rdquo;.</p>
          <button
            type="button"
            className="btn-secondary"
            onClick={() => setSearchTerm('')}
          >
            Clear Search
          </button>
        </div>
      ) : (
        <div className="quiz-grid">
          {filteredQuizzes.map((quiz) => (
            <QuizCard key={quiz.id} quiz={quiz} onTakeQuiz={onTakeQuiz} />
          ))}
        </div>
      )}
    </section>
  );
}

export default QuizList;

