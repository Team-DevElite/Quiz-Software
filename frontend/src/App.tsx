import { useState, useEffect, useCallback } from 'react';
import type { Quiz } from './types.ts';
import { QuizList } from './components/QuizList.tsx';
import './App.css';

const BACKEND_URL = (import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080').replace(/\/$/, '');
const API_ENDPOINT = `${BACKEND_URL}/api/quizzes`;

export function App() {
  const [quizzes, setQuizzes] = useState<Quiz[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchQuizzes = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await fetch(API_ENDPOINT, {
        headers: {
          Accept: 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error(
          `Server returned HTTP ${response.status} (${response.statusText || 'Error'})`
        );
      }

      const data: Quiz[] = await response.json();
      setQuizzes(data);
    } catch (err) {
      const message =
        err instanceof Error
          ? err.message
          : 'Failed to load quizzes. Please check your network connection.';
      setError(
        `${message}. Ensure the Quizzer backend is running at ${API_ENDPOINT}.`
      );
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchQuizzes();
  }, [fetchQuizzes]);

  return (
    <div className="app-container">
      <header className="app-header">
        <div className="header-brand">
          <div className="brand-icon" aria-hidden="true">
            📚
          </div>
          <div>
            <h1>Quizzer Student Dashboard</h1>
            <p className="header-subtitle">
              Browse available course quizzes and test your knowledge
            </p>
          </div>
        </div>

        <div className="header-actions">
          <a
            href={`${BACKEND_URL}/quizzes`}
            target="_blank"
            rel="noopener noreferrer"
            className="btn-link"
            title="Open Teacher Portal"
          >
            Teacher Portal &nearr;
          </a>
          <button
            type="button"
            className="btn-refresh"
            onClick={fetchQuizzes}
            disabled={loading}
            aria-label="Refresh quiz list"
          >
            {loading ? 'Refreshing...' : '↻ Refresh'}
          </button>
        </div>
      </header>

      <main className="app-main">
        {loading && (
          <div className="state-container loading-state" role="status">
            <div className="loading-spinner" aria-hidden="true"></div>
            <h2>Loading Quizzes...</h2>
            <p>Fetching active published quizzes from the server.</p>
          </div>
        )}

        {error && !loading && (
          <div className="state-container error-state" role="alert">
            <div className="error-icon" aria-hidden="true">
              ⚠️
            </div>
            <h2>Failed to Load Quizzes</h2>
            <p className="error-message">{error}</p>
            <div className="error-actions">
              <button
                type="button"
                className="btn-primary"
                onClick={fetchQuizzes}
              >
                Try Again
              </button>
            </div>
          </div>
        )}

        {!loading && !error && quizzes.length === 0 && (
          <div className="state-container empty-state">
            <div className="empty-icon" aria-hidden="true">
              📝
            </div>
            <h2>No Published Quizzes Available</h2>
            <p>
              There are currently no quizzes published for students. Once a teacher
              publishes a quiz, it will automatically appear here.
            </p>
            <button
              type="button"
              className="btn-secondary"
              onClick={fetchQuizzes}
            >
              Check Again
            </button>
          </div>
        )}

        {!loading && !error && quizzes.length > 0 && (
          <QuizList quizzes={quizzes} />
        )}
      </main>

      <footer className="app-footer">
        <p>
          Quizzer Platform &bull; Developed by <strong>Team DevElite</strong> &bull; SOF005AS3AE
        </p>
      </footer>
    </div>
  );
}

export default App;
