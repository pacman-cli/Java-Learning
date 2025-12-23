# Student Exam Dashboard - Frontend

This is a Next.js frontend application that visualizes student exam data from a Spring Boot backend.

## Prerequisites

- Node.js (v18+)
- Backend service running on `http://localhost:8080` (default)

## Setup

1.  Navigate to the `frontend` directory:
    ```bash
    cd frontend
    ```

2.  Install dependencies:
    ```bash
    npm install
    ```

3.  Configure Environment:
    -   The API URL is set in `.env` or `.env.local` as `NEXT_PUBLIC_API_BASE_URL`.
    -   Default: `http://localhost:8080/api`

## Running Locally

Start the development server:

```bash
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.

## Project Structure

-   `src/app`: Page routes (`/` for overview, `/results` for details).
-   `src/components`: Reusable UI components (Charts, Tables, Cards).
-   `src/services`: API integration using Axios.
-   `src/types`: TypeScript interfaces matching backend DTOs.
