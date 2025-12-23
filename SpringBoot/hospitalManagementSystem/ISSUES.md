## Hospital Management System - Issues & Fixes Log

Keep track of discovered issues and their resolutions. Add new entries at the top.

### 2025-10-09

- **Bug Fixed**: Infinite loop in backend data retrieval for patients and doctors pages.
  - Issue: Multiple data fetching mechanisms were running simultaneously causing infinite API calls.
  - Root Cause: `usePatients`/`useDoctors`/`useAppointments` hooks were calling `fetch*()` on mount, while components were also calling `performSearch()` on mount.
  - Fix: Removed automatic `useEffect` calls from hooks and fixed `useCallback` dependencies to prevent infinite loops.
  - Files: All hook files and list component files updated.
  - Status: ✅ Complete

- **Feature Added**: Advanced filtering and bulk operations for all list components.
  - Advanced Filters: Collapsible filter panel with date ranges, status filters, and custom fields.
  - Bulk Actions: Selection management with export (CSV/JSON/XLSX) and bulk delete functionality.
  - Enhanced PatientCard: Added selection checkbox with visual feedback.
  - Export Utils: Comprehensive export functionality with field mapping and filename generation.
  - Files: `AdvancedFilters.tsx`, `BulkActions.tsx`, `exportUtils.ts`, updated all list components.
  - Status: ✅ Complete

- **Feature Added**: Pagination and search for patients, doctors, and appointments.
  - Backend: Added pageable endpoints `/api/{resource}/page` with query params `q`, `page`, `size`, `sort`.
  - Frontend: Implemented `SearchBar` and `Pagination` components with debounced search.
  - Files: Updated repositories, services, controllers, API clients, hooks, and all list components.
  - Status: ✅ Complete

- Issue: Doctor creation Location header path typo in `DoctorController` used `/api/docotors/{id}`.
  - Impact: Clients following the `Location` header after POST would hit 404.
  - Fix: Corrected to `/api/doctors/{id}`.
  - File: `hospital/src/main/java/com/pacman/hospital/domain/doctor/controller/DoctorController.java`

- Issue: Frontend linter warnings due to unnecessary escapes in phone regex.
  - Impact: Build warnings in `frontend/src/utils/formatters.ts`.
  - Fix: Simplified character class to remove unnecessary escapes.
  - File: `frontend/src/utils/formatters.ts`

- **Feature Added**: Dark theme with CSS variables and theme toggle.
  - Added theme state management in `Layout.tsx` with localStorage persistence.
  - Created theme toggle button in `Navbar.tsx`.
  - Refactored `Layout.css` and `Navbar.css` to use CSS variables.
  - Files: `frontend/src/index.css`, `Layout.tsx`, `Navbar.tsx`, `Layout.css`, `Navbar.css`.
  - Status: ✅ Complete

- **Feature Added**: Centralized API client with environment-based base URL.
  - Created `apiClient.ts` using `REACT_APP_API_BASE_URL` (fallback: `http://localhost:8080`).
  - Refactored `patientApi.ts`, `doctorApi.ts`, `appointmentApi.ts` to use shared client.
  - Files: `frontend/src/services/apiClient.ts`, updated service files.
  - Status: ✅ Complete

- Observation: Frontend form types contain fields (email, experience, etc.) not present in backend DTOs.
  - Impact: Extra fields are ignored by backend; transformations map to supported fields.
  - Status: OK due to `transform*FormToBackend` functions, but consider aligning types if backend expands.


