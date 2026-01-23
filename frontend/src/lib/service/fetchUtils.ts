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
