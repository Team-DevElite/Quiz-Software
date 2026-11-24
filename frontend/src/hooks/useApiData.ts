import { useCallback, useEffect, useState } from 'react';

/** Loads data; receives an AbortSignal that fires when the page unmounts or reloads. */
export type Loader<T> = (signal: AbortSignal) => Promise<T>;

interface Settled<T> {
  load: Loader<T>;
  version: number;
  data: T | null;
  error: Error | null;
}

/**
 * Runs `load` and tracks loading / error / data for a page.
 *
 * `load` must be stable between renders: pass a module-level service function
 * directly, or wrap it in useCallback with its route params as dependencies, e.g.
 *   const load = useCallback((s: AbortSignal) => getCategory(id, s), [id]);
 * When `load` changes (new route param) or `reload()` is called, the hook goes
 * back to loading and stale requests are aborted.
 */
export function useApiData<T>(load: Loader<T>) {
  const [version, setVersion] = useState(0);
  const [settled, setSettled] = useState<Settled<T> | null>(null);

  useEffect(() => {
    const controller = new AbortController();
    load(controller.signal).then(
      (data) => {
        if (!controller.signal.aborted) setSettled({ load, version, data, error: null });
      },
      (err: unknown) => {
        if (controller.signal.aborted) return;
        const error = err instanceof Error ? err : new Error(String(err));
        setSettled({ load, version, data: null, error });
      }
    );
    return () => controller.abort();
  }, [load, version]);

  const reload = useCallback(() => setVersion((v) => v + 1), []);

  // Only trust a result produced for the current loader and reload count;
  // anything else means a request is still in flight.
  const current = settled && settled.load === load && settled.version === version ? settled : null;

  return {
    data: current?.data ?? null,
    error: current?.error ?? null,
    loading: current === null,
    reload,
  };
}
