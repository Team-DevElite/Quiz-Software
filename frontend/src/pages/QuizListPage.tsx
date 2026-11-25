import { useQuery } from '@tanstack/react-query';
import { QuizList } from '../components/QuizList.tsx';
import { EmptyState, ErrorState, LoadingState } from '../components/PageStates.tsx';
import { getPublishedQuizzes } from '../services/quizService.ts';

/** Route `/`: published quizzes (user story #21). */
export function QuizListPage() {
  const {
    data: quizzes,
    isPending,
    isFetching,
    error,
    refetch,
  } = useQuery({
    // Unique cache key identifying this query in TanStack Query's cache.
    queryKey: ['quizzes', 'published'],
    // TanStack Query injects an AbortSignal so in-flight requests are cancelled on unmount/refetch.
    queryFn: ({ signal }) => getPublishedQuizzes(signal),
  });

  // Show the full-page spinner on initial load or when retrying after an error with no cached data.
  const isInitialLoading = isPending || (isFetching && !quizzes);

  return (
    <>
      <div className="page-header">
        <h2 className="page-title">Quizzes</h2>
        <button
          type="button"
          className="btn-refresh"
          onClick={() => void refetch()}
          disabled={isFetching}
          aria-label="Refresh quiz list"
        >
          {isFetching ? 'Refreshing...' : '↻ Refresh'}
        </button>
      </div>

      {isInitialLoading && (
        <LoadingState title="Loading Quizzes..." message="Fetching active published quizzes from the server." />
      )}

      {!isInitialLoading && error && (
        <ErrorState title="Failed to Load Quizzes" message={error.message} onRetry={() => void refetch()} />
      )}

      {!isInitialLoading && !error && quizzes && quizzes.length === 0 && (
        <EmptyState icon="📝" title="No Published Quizzes Available" onRetry={() => void refetch()}>
          There are currently no quizzes published for students. Once a teacher
          publishes a quiz, it will automatically appear here.
        </EmptyState>
      )}

      {!isInitialLoading && !error && quizzes && quizzes.length > 0 && <QuizList quizzes={quizzes} />}
    </>
  );
}

export default QuizListPage;
