import { Link } from 'react-router-dom';
import { EmptyState, ErrorState, LoadingState } from '../components/PageStates.tsx';
import { useApiData } from '../hooks/useApiData.ts';
import { getCategories } from '../services/categoryService.ts';

/**
 * Route `/categories`: every category with its description (user story #25).
 * Each name links to `/categories/:id/quizzes`, the category's quiz page (user story #26).
 */
export function CategoryListPage() {
  const { data: categories, loading, error, reload } = useApiData(getCategories);

  return (
    <>
      <div className="page-header">
        <h2 className="page-title">Categories</h2>
        <button
          type="button"
          className="btn-refresh"
          onClick={reload}
          disabled={loading}
          aria-label="Refresh category list"
        >
          {loading ? 'Refreshing...' : '↻ Refresh'}
        </button>
      </div>

      {loading && <LoadingState title="Loading Categories..." message="Fetching categories from the server." />}

      {error && <ErrorState title="Failed to Load Categories" message={error.message} onRetry={reload} />}

      {categories && categories.length === 0 && (
        <EmptyState icon="🗂️" title="No Categories Yet" onRetry={reload}>
          Teachers have not added any categories. Once they do, you can browse quizzes by category here.
        </EmptyState>
      )}

      {categories && categories.length > 0 && (
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
