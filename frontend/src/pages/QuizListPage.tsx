import { QuizList } from '../components/QuizList.tsx';
import { EmptyState, ErrorState, LoadingState } from '../components/PageStates.tsx';
import { useApiData } from '../hooks/useApiData.ts';
import { getPublishedQuizzes } from '../services/quizService.ts';

/** Route `/`: published quizzes (user story #21). */
export function QuizListPage() {
  const { data: quizzes, loading, error, reload } = useApiData(getPublishedQuizzes);

  return (
    <>
      <div className="page-header">
        <h2 className="page-title">Quizzes</h2>
        <button
          type="button"
          className="btn-refresh"
          onClick={reload}
          disabled={loading}
          aria-label="Refresh quiz list"
        >
          {loading ? 'Refreshing...' : '↻ Refresh'}
        </button>
      </div>

      {loading && (
        <LoadingState title="Loading Quizzes..." message="Fetching active published quizzes from the server." />
      )}

      {error && <ErrorState title="Failed to Load Quizzes" message={error.message} onRetry={reload} />}

      {quizzes && quizzes.length === 0 && (
        <EmptyState icon="📝" title="No Published Quizzes Available" onRetry={reload}>
          There are currently no quizzes published for students. Once a teacher
          publishes a quiz, it will automatically appear here.
        </EmptyState>
      )}

      {quizzes && quizzes.length > 0 && <QuizList quizzes={quizzes} />}
    </>
  );
}

export default QuizListPage;
