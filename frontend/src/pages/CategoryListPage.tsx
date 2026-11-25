import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { EmptyState, ErrorState, LoadingState } from '../components/PageStates.tsx';
import { getCategories } from '../services/categoryService.ts';

/**
 * Route `/categories`: every category with its description (user story #25).
 * Each name links to `/categories/:id/quizzes`, the category's quiz page (user story #26).
 */
export function CategoryListPage() {
  const {
    data: categories,
    isPending,
    isFetching,
    error,
    refetch,
  } = useQuery({
    // Unique cache key identifying the categories list in TanStack Query's cache.
    queryKey: ['categories'],
    // TanStack Query injects an AbortSignal so in-flight requests are cancelled on unmount/refetch.
    queryFn: ({ signal }) => getCategories(signal),
  });

  // Show the full-page spinner on initial load or when retrying after an error with no cached data.
  const isInitialLoading = isPending || (isFetching && !categories);

  return (
    <>
      <div className="page-header">
        <h2 className="page-title">Categories</h2>
        <button
          type="button"
          className="btn-refresh"
          onClick={() => void refetch()}
          disabled={isFetching}
          aria-label="Refresh category list"
        >
          {isFetching ? 'Refreshing...' : '↻ Refresh'}
        </button>
      </div>

      {isInitialLoading && (
        <LoadingState title="Loading Categories..." message="Fetching categories from the server." />
      )}

      {!isInitialLoading && error && (
        <ErrorState title="Failed to Load Categories" message={error.message} onRetry={() => void refetch()} />
      )}

      {!isInitialLoading && !error && categories && categories.length === 0 && (
        <EmptyState icon="🗂️" title="No Categories Yet" onRetry={() => void refetch()}>
          Teachers have not added any categories. Once they do, you can browse quizzes by category here.
        </EmptyState>
      )}

      {!isInitialLoading && !error && categories && categories.length > 0 && (
        <ul className="category-grid">
          {categories.map((category) => (
            <li key={category.id} className="category-card">
              <h3 className="category-name">
                <Link to={`/categories/${category.id}/quizzes`}>{category.name}</Link>
              </h3>
              <p className="category-description">
                {category.description || 'No description provided.'}
              </p>
            </li>
          ))}
        </ul>
      )}
    </>
  );
}

export default CategoryListPage;
