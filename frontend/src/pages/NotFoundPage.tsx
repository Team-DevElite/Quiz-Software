import { Link } from 'react-router-dom';

/** Fallback for unknown routes. */
export function NotFoundPage() {
  return (
    <div className="state-container empty-state">
      <div className="empty-icon" aria-hidden="true">
        🧭
      </div>
      <h2>Page Not Found</h2>
      <p>The page you are looking for does not exist (yet).</p>
      <Link to="/" className="btn-secondary">
        Back to Quizzes
      </Link>
    </div>
  );
}

export default NotFoundPage;
