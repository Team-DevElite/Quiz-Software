import type { Quiz } from '../types.ts';
import { getJson } from './api.ts';

/** GET /api/quizzes: published quizzes only. */
export function getPublishedQuizzes(signal?: AbortSignal): Promise<Quiz[]> {
  return getJson<Quiz[]>('/api/quizzes', signal);
}
