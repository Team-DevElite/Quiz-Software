import type { ReactNode } from 'react';

/** Shared loading / error / empty views so every page handles these states the same way. */

export function LoadingState({ title, message }: { title: string; message?: string }) {
  return (
    <div className="state-container loading-state" role="status">
      <div className="loading-spinner" aria-hidden="true"></div>
      <h2>{title}</h2>
      {message && <p>{message}</p>}
    </div>
  );
}

export function ErrorState({
  title,
  message,
  onRetry,
}: {
  title: string;
  message: string;
  onRetry?: () => void;
}) {
  return (
    <div className="state-container error-state" role="alert">
      <div className="error-icon" aria-hidden="true">
        ⚠️
      </div>
      <h2>{title}</h2>
      <p className="error-message">{message}</p>
      {onRetry && (
        <div className="error-actions">
          <button type="button" className="btn-primary" onClick={onRetry}>
            Try Again
          </button>
        </div>
      )}
    </div>
  );
}

export function EmptyState({
  icon,
  title,
  children,
  onRetry,
}: {
  icon: string;
  title: string;
  children?: ReactNode;
  onRetry?: () => void;
}) {
  return (
    <div className="state-container empty-state">
      <div className="empty-icon" aria-hidden="true">
        {icon}
      </div>
      <h2>{title}</h2>
      {children && <p>{children}</p>}
      {onRetry && (
        <button type="button" className="btn-secondary" onClick={onRetry}>
          Check Again
        </button>
      )}
    </div>
  );
}
