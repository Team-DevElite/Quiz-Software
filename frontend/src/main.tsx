import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ApiError } from './services/api.ts';
import './index.css';
import App from './App.tsx';

// Central TanStack Query client holding the shared in-memory cache and default query behavior.
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      // Skip retries for 4xx client errors (e.g. 404); retry transient network/5xx failures once.
      retry: (failureCount, error) =>
        !(error instanceof ApiError && error.status !== undefined && error.status < 500) &&
        failureCount < 1,
    },
  },
});

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    {/* Provide the QueryClient to all components so useQuery hooks share one cache. */}
    <QueryClientProvider client={queryClient}>
      <App />
    </QueryClientProvider>
  </StrictMode>
);
