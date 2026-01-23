export type BackendErrorResponse = {
    timestamp: string;
    status: number;
    error: string;
    message: string;
    path: string;
};

export class HttpError extends Error {
    constructor(
        public status: number,
        public statusText: string,
        public url: string,
        public body?: unknown,
    ) {
        super(`HTTP ${status} ${statusText}: ${url}`);
        this.name = "HttpError";
    }

    getBackendError(): BackendErrorResponse | null {
        if (
            this.body &&
            typeof this.body === "object" &&
            "message" in this.body
        ) {
            return this.body as BackendErrorResponse;
        }
        return null;
    }
}

export function getErrorMessage(error: unknown): string {
    if (error instanceof HttpError) {
        const backendError = error.getBackendError();
        if (backendError) {
            return backendError.message;
        }
        return `HTTP ${error.status}: ${error.statusText}`;
    }
    if (error instanceof Error) {
        return error.message;
    }
    return "Ein unbekannter Fehler ist aufgetreten";
}

export async function fetchWithErrorHandling(
    input: RequestInfo | URL,
    init?: RequestInit,
): Promise<Response> {
    const response = await fetch(input, init);

    if (!response.ok) {
        let errorBody: unknown;
        try {
            const contentType = response.headers.get("content-type");
            if (contentType?.includes("application/json")) {
                errorBody = await response.json();
            } else {
                errorBody = await response.text();
            }
        } catch {
            errorBody = undefined;
        }

        throw new HttpError(
            response.status,
            response.statusText,
            response.url,
            errorBody,
        );
    }

    return response;
}
