/**
 * Shared HTTP helper for the student dashboard. Every backend call goes
 * through here so the base URL and error handling live in one place.
 * Add new endpoints as small functions in a sibling service module
 * (quizService.ts, categoryService.ts, ...), not as fetch calls in components.
 */

export const BACKEND_URL = (import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080').replace(/\/$/, '');

/** Error carrying the HTTP status (undefined for network failures), so pages can special-case 404. */
export class ApiError extends Error {
  readonly status?: number;

  constructor(message: string, status?: number) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
  }
}

/**
 * GETs `${BACKEND_URL}${path}` and parses the JSON body.
 * @throws ApiError on network failure or a non-2xx response
 */
export async function getJson<T>(path: string, signal?: AbortSignal): Promise<T> {
  let response: Response;
  try {
    response = await fetch(`${BACKEND_URL}${path}`, {
      signal,
      headers: { Accept: 'application/json' },
    });
  } catch (err) {
    // Let aborts propagate untouched; callers ignore them.
    if (signal?.aborted) throw err;
    throw new ApiError(`Could not reach the Quizzer backend at ${BACKEND_URL}. Make sure it is running.`);
  }

  if (!response.ok) {
    throw new ApiError(
      `Server returned HTTP ${response.status} (${response.statusText || 'Error'})`,
      response.status
    );
  }

  return (await response.json()) as T;
}
