import { NavLink, Outlet } from 'react-router-dom';
import { BACKEND_URL } from '../services/api.ts';

/** Shared page frame: brand header, main navigation menu, routed page content, footer. */
export function Layout() {
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
            Teacher Portal ↗
          </a>
        </div>
      </header>

      <nav className="main-nav" aria-label="Main">
        {/* `end` keeps "Quizzes" from staying active on nested routes. */}
        <NavLink to="/" end className="nav-link">
          Quizzes
        </NavLink>
        <NavLink to="/categories" className="nav-link">
          Categories
        </NavLink>
      </nav>

      <main className="app-main">
        <Outlet />
      </main>

      <footer className="app-footer">
        <p>
          Quizzer Platform &bull; Developed by <strong>Team DevElite</strong> &bull; SOF005AS3AE
        </p>
      </footer>
    </div>
  );
}

export default Layout;
