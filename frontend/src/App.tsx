import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Layout } from './components/Layout.tsx';
import { QuizListPage } from './pages/QuizListPage.tsx';
import { CategoryListPage } from './pages/CategoryListPage.tsx';
import { CategoryQuizListPage } from './pages/CategoryQuizListPage.tsx';
import { NotFoundPage } from './pages/NotFoundPage.tsx';
import './App.css';

/**
 * Student dashboard routes. Add new pages as <Route>s inside the Layout route
 * so they get the shared header, navigation menu and footer:
 *   /quizzes/:id            quiz page            (stories #22, #23)
 *   /quizzes/:id/results    quiz results         (story #24)
 *   /categories/:id/quizzes quizzes of category  (story #26)
 */
export function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route index element={<QuizListPage />} />
          <Route path="categories" element={<CategoryListPage />} />
          <Route path="categories/:id/quizzes" element={<CategoryQuizListPage />} />
          <Route path="*" element={<NotFoundPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
