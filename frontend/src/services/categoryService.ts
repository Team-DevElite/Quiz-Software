import type { Category } from '../types.ts';
import { getJson } from './api.ts';

/** GET /api/categories: every category. */
export function getCategories(signal?: AbortSignal): Promise<Category[]> {
  return getJson<Category[]>('/api/categories', signal);
}

/** GET /api/categories/{id}: rejects with ApiError status 404 if it does not exist. */
export function getCategory(id: number, signal?: AbortSignal): Promise<Category> {
  return getJson<Category>(`/api/categories/${id}`, signal);
}
