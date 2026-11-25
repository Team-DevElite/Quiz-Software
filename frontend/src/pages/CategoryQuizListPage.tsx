import { useQuery } from '@tanstack/react-query';
import { Link, useParams } from 'react-router-dom';
import { QuizList } from '../components/QuizList.tsx';
import { EmptyState, ErrorState, LoadingState } from '../components/PageStates.tsx';
import { ApiError } from '../services/api.ts';
import { getCategory } from '../services/categoryService.ts';
import { getQuizzesByCategory } from '../services/quizService.ts';

/** Route `/categories/:id/quizzes`: published quizzes of one category (user story #26). */
export function CategoryQuizListPage() {
  const { id } = useParams();
  const categoryId = Number(id);
  const validId = Number.isInteger(categoryId) && categoryId > 0;

  const category = useQuery({
    queryKey: ['category', categoryId],
    queryFn: ({ signal }) => validId ? getCategory(categoryId, signal) : rejectNotFound(),
  });
  const quizzes = useQuery({
    queryKey: ['category-quizzes', categoryId],
    queryFn: ({ signal }) => validId ? getQuizzesByCategory(categoryId, signal) : rejectNotFound(),
  });

  const loading = category.isPending || quizzes.isPending;
  const error = category.error ?? quizzes.error;
  const notFound = error instanceof ApiError && error.status === 404;

  const reload = () => {
    void category.refetch();
    void quizzes.refetch();
  };

  return (
    <>
      <div className="page-header">
        <h2 className="page-title">
          {category.data ? `Quizzes in ${category.data.name}` : 'Quizzes in category'}
        </h2>
        <Link to="/categories" className="btn-secondary">
          ← All categories
        </Link>
      </div>

      {category.data?.description && <p className="category-description">{category.data.description}</p>}

      {loading && !error && (
        <LoadingState title="Loading Quizzes..." message="Fetching the quizzes of this category." />
      )}

      {error && notFound && (
        <EmptyState icon="🗂️" title="Category Not Found">
          This category does not exist. It may have been removed by a teacher.
        </EmptyState>
      )}

      {error && !notFound && (
        <ErrorState title="Failed to Load Quizzes" message={error.message} onRetry={reload} />
      )}

      {!error && quizzes.data && quizzes.data.length === 0 && (
        <EmptyState icon="📝" title="No Quizzes in This Category" onRetry={reload}>
          There are no published quizzes in this category yet.
        </EmptyState>
      )}

      {!error && quizzes.data && quizzes.data.length > 0 && <QuizList quizzes={quizzes.data} />}
    </>
  );
}

/** Loader used for a non-numeric route id. */
function rejectNotFound(): Promise<never> {
  return Promise.reject(new ApiError('Category not found', 404));
}

export default CategoryQuizListPage;