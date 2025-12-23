# Debugging Dashboard Data Loading

If you see "Failed to load dashboard data", it is likely a connection issue between the Next.js Frontend (port 3000) and Spring Boot Backend (port 8080).

## 1. The Fix (Proxy Method)
Since we are using Next.js, the best way to fix this **without modifying backend code** is to use a Proxy. This avoids CORS issues entirely.

### Correct Configuration

**Frontend: `next.config.ts`**
Ensure you have this rewrite rule. It tells Next.js to forward any request starting with `/api` to your backend.
```typescript
const nextConfig: NextConfig = {
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'http://localhost:8080/api/:path*', // Forward to Backend
      },
    ];
  },
};
```

**Frontend: `.env.local`**
Set the base URL to `/api` so it hits the Next.js proxy, not the backend directly.
```bash
NEXT_PUBLIC_API_BASE_URL=/api
```

**Frontend: `src/services/api.ts`**
Ensure your API service uses the environment variable and matches backend paths.
```typescript
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || '/api';
// Requests become: GET /api/dashboard/summary
```

### Action Required
**Restart the Next.js server** (`npm run dev`) for these changes to take effect. Rewrites are loaded at startup.

---

## 2. Common Causes & Fixes

### A. CORS Error (Cross-Origin Resource Sharing)
**Symptoms:** Browser Console shows `Access to fetch at '...' has been blocked by CORS policy`.
**Cause:** Browsers block requests from `localhost:3000` to `localhost:8080` for security.
**Fix:**
1.  **Use the Proxy (Recommended)**: As shown above.
2.  **Enable CORS in Spring Boot**: If you want to connect directly (without proxy), add this to your backend:

```java
// src/main/java/com/puspo/codearena/project/config/CorsConfig.java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // Allow Frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
```

### B. Endpoint Mismatch
**Symptoms:** `404 Not Found` in Network Tab.
**Cause:** Frontend is calling `/api/detailed` but Backend expects `/api/dashboard/detailedExamResults`.
**Fix:** Check your Backend Controller (`@RequestMapping`) and ensure Frontend matches exactly.

**Backend:**
```java
@RequestMapping("/api/dashboard")
// ...
@GetMapping("/summary")
```
**Frontend:**
Should request: `/api/dashboard/summary`

### C. Backend Not Running / Port Issue
**Symptoms:** `ERR_CONNECTION_REFUSED`.
**Fix:**
1.  Check if backend is running: `curl http://localhost:8080/api/dashboard/summary`
2.  Verify Port: Check `application.properties` for `server.port=8080`.

---

## 3. Debugging Checklist

1.  **Inspect Network Tab**:
    -   Open Chrome DevTools -> **Network**.
    -   Filter by "Fetch/XHR".
    -   Refresh page.
    -   Click the red failed request.
    -   **Check URL**: Is it fetching `http://localhost:3000/api/...` (Proxy) or `http://localhost:8080/...` (Direct)?
    -   **Check Status**: `404` (Wrong URL), `500` (Server Error), `CORS Blocked`?

2.  **Verify Backend Response**:
    -   Open a separate browser tab.
    -   Go to `http://localhost:8080/api/dashboard/summary`.
    -   Do you see JSON? If yes, backend is fine.

3.  **Check Logs**:
    -   **Next.js Terminal**: Look for "Proxy error" or "Rewrite".
    -   **Spring Boot Console**: Look for "DispatcherServlet no handler found" (404) or Exceptions (500).
