import type { Quiz } from '../types.ts';
import { getJson } from './api.ts';

/** GET /api/quizzes: published quizzes only. */
export function getPublishedQuizzes(signal?: AbortSignal): Promise<Quiz[]> {
  return getJson<Quiz[]>('/api/quizzes', signal);
}

/** GET /api/categories/{id}/quizzes: published quizzes of one category; rejects with ApiError 404 if the category does not exist. */
export function getQuizzesByCategory(categoryId: number, signal?: AbortSignal): Promise<Quiz[]> {
  return getJson<Quiz[]>(`/api/categories/${categoryId}/quizzes`, signal);
}